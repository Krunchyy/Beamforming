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

public class MaxSNR extends AbstractOrdonnanceur {

	private Agent agent;
	
	public MaxSNR(ConcurrentHashMap<Mobile, Queue<Packet>> map, OFDM ofdm) {
		super(map, ofdm);
		this.agent = this.getOfdm()._agent;
	}

	@Override
	public void tick() {
		for(int i=0 ; i < this.getOfdm()._nb_time_slot ; i++) {
			this.allocateTimeslot(this.getOfdm()._currentIndex + i);
		}
	}
	
	private void allocateTimeslot(int timeslot) {
		int subcarriers = this.getOfdm()._nb_sub_carrier;
		
		ArrayList<PacketFragment> fragments = new ArrayList<>();
		ArrayList<Mobile> emptyBuffersMobile = new ArrayList<Mobile>();
		
		for(int i = 0 ; i < subcarriers ; i++) {
			if(this.getMap().size() == emptyBuffersMobile.size()) {
				fragments.add(null);
				continue;
			}
			
			Mobile mobile = this.getMobileWithBestSNR(timeslot, i, emptyBuffersMobile);
			if(mobile == null)
			{
				return;
			}
			
			Queue<Packet> buffer = this.getMap().get(mobile);

			if(this.hasNextPacket(buffer)) {
				Packet packet = this.getNextPacket(buffer);
				PacketFragment fragment = new PacketFragment(packet);
				fragment.setMkn((int) Math.round(mobile.getSNR(this.agent, i, timeslot)));
				fragment.addData();
				packet.addFragment(fragment);
				
				fragments.add(fragment);
			}
			else {
				emptyBuffersMobile.add(mobile);
				i--;
			}
		}
		
		if(subcarriers != fragments.size())
			System.err.println("[MaxSNR:ERROR] allocateTimeslot() not fill ofdm correctly");
		
		PacketFragment[] array = new PacketFragment[fragments.size()];
		
		this.getOfdm().setTimeSlot(timeslot, fragments.toArray(array));
	}

	/*
	 * Pour gérer le Beamforming verifier si un mobile est connecté en Beamforming avec mobile.isBeamforming
	 * récupérer le subcarrier du timeslot avec getBeamSubCarrier qui retourne un BeamSubCarrier contenant le subcarrier et le mkn
	 * pour eviter d'avoir des quantités différentes de paquets générés et en quantité doublée on a placé la queue dans le mobile
	 * pour y acceder -> mobile._filePacketsBeam
	 */
	private Mobile getMobileWithBestSNR(int timeslot, int subcarrier, ArrayList<Mobile> emptyBuffersMobile) {
		Mobile chosen = null;
		
		Set<Entry<Mobile, Queue<Packet>>> entry = this.getMap().entrySet();
		
		double SNR = 0;
		
		for (Iterator<Entry<Mobile, Queue<Packet>>> iterator = entry.iterator(); iterator.hasNext();){
			Entry<Mobile, Queue<Packet>> iter = iterator.next();
			if(!iter.getKey().isMobile())
				continue;
			
			if(emptyBuffersMobile.contains(iter.getKey()))
				continue;
			
			if(iter.getKey().getSNR(this.agent, subcarrier, timeslot) > SNR) {
				SNR =  iter.getKey().getSNR(this.agent, subcarrier, timeslot);
				chosen = iter.getKey();
			}
	    }
		
		return chosen;
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
