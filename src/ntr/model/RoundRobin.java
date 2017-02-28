package ntr.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.OFDM;
import ntr.signal.Packet;


public class RoundRobin extends AbstractOrdonnanceur {
	private IModel lastModel;
	public RoundRobin(ConcurrentHashMap<IModel, Queue<Packet>> map, OFDM ofdm) {
		super(map, ofdm);
		this.lastModel = null;
	}

	@Override
	public void tick() { 
		Set<Entry<IModel, Queue<Packet>>> entryset = this.getMap().entrySet();
		
		Iterator<Entry<IModel, Queue<Packet>>> iter = entryset.iterator();
		boolean lastModelFound = false;
		
		ArrayList<Packet> packets = new ArrayList<>();
		while(this.allMobileBuffersEmpty() && packets.size() != this.getOfdm()._nb_sub_carrier) {
			Entry<IModel, Queue<Packet>> entry = iter.next();
			
			if(this.lastModel != null && this.getMap().containsKey(this.lastModel)) {
				if(lastModelFound) {
					this.allow(entry.getKey(), packets);
				}
				if(entry.getKey() == this.lastModel) {
					lastModelFound = true;
				}
				
				if(!iter.hasNext()) {
					iter = entryset.iterator();
				}
			}
			else {
				this.allow(entry.getKey(), packets);
			}
		}
		
		this.updateOFDM(packets);
	}
	
	private void allow(IModel model, ArrayList<Packet> packets) {
		this.lastModel = model;
		
		Queue<Packet> buffer = this.getMap().get(model);
		
		for(int i=0 ; i< this.getOfdm()._nb_sub_carrier ; i++) {
			if(!buffer.isEmpty()) {
				packets.add(buffer.poll());
			}
		}
		
		this.getMap().put(model, buffer);
		
	}
	
	/**
	 * Send packets of user into ofdm current Timeslot and fill blank in timeslot if not enough buffers
	 * @param packets to send to OFDM current timeslot
	 */
	private void updateOFDM(ArrayList<Packet> packets) {
		int slot = this.getOfdm()._currentIndex;
		
		if(packets.size() < this.getOfdm()._nb_sub_carrier) {
			for(int i = packets.size() ; i < this.getOfdm()._nb_sub_carrier ; i++)
				packets.add(null);
		}
			
		Packet[] array = new Packet[packets.size()];
		this.getOfdm().setTimeSlot(slot, (Packet[]) packets.toArray(array));
	}
	
	private boolean allMobileBuffersEmpty() {
		while(this.getMap().elements().hasMoreElements()) {
			if(this.getMap().elements().nextElement().size() > 0)
				return true;
		}
		return false;
	}

}
