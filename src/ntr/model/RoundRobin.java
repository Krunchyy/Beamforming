package ntr.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.OFDM;
import ntr.signal.Packet;
import ntr.signal.PacketFragment;


public class RoundRobin extends AbstractOrdonnanceur {
	
	private int internTick;
	private Mobile lastModel;
	
	public RoundRobin(ConcurrentHashMap<Mobile, Queue<Packet>> map, OFDM ofdm) {
		super(map, ofdm);
		this.lastModel = null;
		this.internTick = 0;
	}

	@Override
	public void tick() { 
		this.internTick = 0;
		for(int i = 0 ; i < this.getOfdm()._nb_time_slot ; i++) {
			this.internTick();
			this.internTick++;
		}
	}
	
	private void internTick() {
		Set<Entry<Mobile, Queue<Packet>>> entryset = this.getMap().entrySet();
		
		Iterator<Entry<Mobile, Queue<Packet>>> iter = entryset.iterator();
		boolean lastModelFound = false;
		
		ArrayList<PacketFragment> packets = new ArrayList<>();
		
		while(this.hasMorePackets() && packets.size() != this.getOfdm()._nb_sub_carrier) {//tant que il reste de la place dans ofdm
			if(!iter.hasNext())
				iter = entryset.iterator();
			
			Entry<Mobile, Queue<Packet>> entry = iter.next();
			
			if(this.lastModel != null && this.getMap().containsKey(this.lastModel)) {
				if(lastModelFound) {
					this.allow(entry.getKey(), packets);
				}
				if(entry.getKey() == this.lastModel) {
					lastModelFound = true;
				}
			}
			else {
				this.allow(entry.getKey(), packets);
			}
		}
		
		this.updateOFDM(packets);
	}
	
	private void allow(Mobile model, ArrayList<PacketFragment> packetFragments) {
		int slot = (this.getOfdm()._currentIndex + this.internTick) % this.getOfdm()._nb_time_slot;
		this.lastModel = model;
		
		Queue<Packet> buffer = this.getMap().get(model);
		
		for(int i= packetFragments.size() ; i< this.getOfdm()._nb_sub_carrier ; i++) {
			if(this.hasNextPacket(buffer)) {
				Packet packet = this.getNextPacket(buffer);
				PacketFragment fragment = new PacketFragment(packet);
				packet.addFragment(fragment);
				
				/*
				 * Set Mkn and set data on packet fragment
				 */
				double mkn = fragment.parent._receiver.getSNR(this.getOfdm()._agent, i, slot);
				fragment.setMkn((int) Math.round(mkn));
				fragment.addData();
				
				packetFragments.add(fragment);
			}
			else {
				break;
			}
		}
		
		//this.getMap().put(model, buffer);
		
	}
	
	/**
	 * Send packets of user into ofdm current Timeslot and fill blank in timeslot if not enough buffers
	 * @param packetFragments to send to OFDM current timeslot
	 */
	private void updateOFDM(ArrayList<PacketFragment> packetFragments) {
		int slot = (this.getOfdm()._currentIndex + this.internTick) % this.getOfdm()._nb_time_slot;
		
		if(packetFragments.size() < this.getOfdm()._nb_sub_carrier) {
			for(int i = packetFragments.size() ; i < this.getOfdm()._nb_sub_carrier ; i++)
				packetFragments.add(null);
		}
			
		
		PacketFragment[] array = new PacketFragment[packetFragments.size()];
		
		this.getOfdm().setTimeSlot(slot, (PacketFragment[]) packetFragments.toArray(array));
	}
	
	private boolean hasMorePackets() {
		
		Set<Entry<Mobile, Queue<Packet>>> entryset = this.getMap().entrySet();
		
		Iterator<Entry<Mobile, Queue<Packet>>> iter = entryset.iterator();
		
		while(iter.hasNext()) {
			Entry<Mobile, Queue<Packet>> entry = iter.next();
			if(this.hasNextPacket(entry.getValue())) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Vérifie si un Packet est coupable en PacketFragment
	 * @param buffer
	 * @return true|false
	 */
	private boolean hasNextPacket(Queue<Packet> buffer) {
		if(buffer.size() == 0)
			return false;
		
		Iterator<Packet> iterator = buffer.iterator();
		
		while(iterator.hasNext()) {
			Packet p = iterator.next();
			if(!p.isFragmented())
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param buffer
	 * @return null|Packet
	 */
	private Packet getNextPacket(Queue<Packet> buffer) {
		if(buffer.size() == 0)
			throw new ArrayIndexOutOfBoundsException("Impossible de récupérer un packet non-envoyé dans le buffer");
		
		Iterator<Packet> iterator = buffer.iterator();
		
		while(iterator.hasNext()) {
			Packet p = iterator.next();
			if(!p.isFragmented())
				return p;
		}
		throw new ArrayIndexOutOfBoundsException("Impossible de récupérer un packet non-envoyé dans le buffer");
	}
}
