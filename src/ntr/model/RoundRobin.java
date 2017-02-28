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
		
		while(iter.hasNext()) {
			Entry<IModel, Queue<Packet>> entry = iter.next();
			
			if(this.lastModel != null && this.getMap().containsKey(this.lastModel)) {
				if(lastModelFound) {
					this.allow(entry.getKey());
					return;
				}
				if(entry.getKey() == this.lastModel) {
					lastModelFound = true;
				}
				
				if(!iter.hasNext()) {
					iter = entryset.iterator();
				}
			}
			else {
				this.allow(entry.getKey());
			}
		}
	}
	
	private void allow(IModel model) {
		this.lastModel = model;
		
		Queue<Packet> buffer = this.getMap().get(model);
		System.out.println("buffer start size : " + buffer.size() );
		int slot = this.getOfdm()._currentIndex;;//slot ?
		ArrayList<Packet> packets = new ArrayList<>();
		for(int i=0 ; i< this.getOfdm()._nb_sub_carrier ; i++) {//TODO: if enough size allow next mobile
			if(!buffer.isEmpty())
				packets.add(buffer.poll());
			else {
				packets.add(null);
			}
		}
		Packet[] array = new Packet[packets.size()];
		this.getMap().put(model, buffer);
		this.getOfdm().setTimeSlot(slot, (Packet[]) packets.toArray(array));
	}

	@Override
	public IModel choose() {
		// TODO Auto-generated method stub
		return null;
	}

}
