package ntr.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.OFDM;
import ntr.signal.PacketFragment;


public class RoundRobin extends AbstractOrdonnanceur {
	private IModel lastModel;
	public RoundRobin(ConcurrentHashMap<IModel, Queue<PacketFragment>> map, OFDM ofdm) {
		super(map, ofdm);
		this.lastModel = null;
	}

	@Override
	public void tick() { 
		Set<Entry<IModel, Queue<PacketFragment>>> entryset = this.getMap().entrySet();
		
		Iterator<Entry<IModel, Queue<PacketFragment>>> iter = entryset.iterator();
		boolean lastModelFound = false;
		
		ArrayList<PacketFragment> packets = new ArrayList<>();
		
		//FIXME infinite loop cases
		while(this.allMobileBuffersEmpty() && packets.size() != this.getOfdm()._nb_sub_carrier) {
			Entry<IModel, Queue<PacketFragment>> entry = iter.next();
			
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
	
	private void allow(IModel model, ArrayList<PacketFragment> packets) {
		this.lastModel = model;
		
		Queue<PacketFragment> buffer = this.getMap().get(model);
		
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
	private void updateOFDM(ArrayList<PacketFragment> packets) {
		int slot = this.getOfdm()._currentIndex;
		
		if(packets.size() < this.getOfdm()._nb_sub_carrier) {
			for(int i = packets.size() ; i < this.getOfdm()._nb_sub_carrier ; i++)
				packets.add(null);
		}
			
		PacketFragment[] array = new PacketFragment[packets.size()];
		this.getOfdm().setTimeSlot(slot, (PacketFragment[]) packets.toArray(array));
	}
	
	private boolean allMobileBuffersEmpty() {
		while(this.getMap().elements().hasMoreElements()) {
			if(this.getMap().elements().nextElement().size() > 0)
				return true;
		}
		return false;
	}

}
