package ntr.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.OFDM;
import ntr.signal.PacketFragment;

public class MaxSNR extends AbstractOrdonnanceur {

	private Agent agent;
	
	public MaxSNR(ConcurrentHashMap<IModel, Queue<PacketFragment>> map, OFDM ofdm) {
		super(map, ofdm);
		this.agent = this.getOfdm()._agent;
	}

	@Override
	public void tick() {
		//this.getMap().
	}
	
	private allocateTimeslot(int timeslot) {
		int subcarriers = this.getOfdm()._nb_sub_carrier;
		
		ArrayList<PacketFragment> fragments = new ArrayList<>();
		ArrayList<Mobile> emptyBuffersMobile = new ArrayList<Mobile>();
		for(int i = 0 ; i < subcarriers ; i++) {
			if(this.getMap().size() == emptyBuffersMobile.size()) {
				fragments.add(null);
				continue;
			}
			
			Mobile mobile = this.getMobileWithBestSNR(i, emptyBuffersMobile);
			Queue<PacketFragment> buffer = this.getMap().get(mobile);
			
			if(buffer.size() > 0) {
				fragments.add(buffer.poll());
			}
			else {
				emptyBuffersMobile.add(mobile);
				i--;
			}
		}
	}

	private Mobile getMobileWithBestSNR(int subcarrier, ArrayList<Mobile> emptyBuffersMobile) {
		Mobile chosen = null;
		
		Set<Entry<IModel, Queue<PacketFragment>>> entry = this.getMap().entrySet();
		
		double SNR = 0;
		
		for (Iterator<Entry<IModel, Queue<PacketFragment>>> iterator = entry.iterator(); iterator.hasNext();){
			Entry<IModel, Queue<PacketFragment>> iter = iterator.next();
			if(!iter.getKey().isMobile())
				continue;
			
			if(emptyBuffersMobile.contains(iter.getKey()))
				continue;
			
			if(((Mobile) iter.getKey()).getSNR(this.agent, subcarrier) > SNR) {
				SNR = ((Mobile) iter.getKey()).getSNR(this.agent, subcarrier);
				chosen = (Mobile) iter.getKey();
			}
	    }
		
		return chosen;
	}

}
