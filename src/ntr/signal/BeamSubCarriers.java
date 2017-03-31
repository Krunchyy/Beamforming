package ntr.signal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.model.Agent;
import ntr.model.Mobile;
import ntr.utils.Config;

public class BeamSubCarriers {
	private HashMap<Integer, Integer> mknBySubcarriers;
	
	
	public BeamSubCarriers(Mobile mobile, Agent agent, int timeslot, HashMap<Integer, Integer> mknBySubcarriers) {
		if(mknBySubcarriers == null)
			this.mknBySubcarriers = new HashMap<>();
		else {
			this.mknBySubcarriers = new HashMap<>(mknBySubcarriers);
			//Update the mkn value on the hashMap because currently it's the sum of all the mkn agents.
			Set<Integer> keys = this.mknBySubcarriers.keySet();
			
			Iterator<Integer> iterator = keys.iterator();
			
			while(iterator.hasNext()) {
				Integer key = iterator.next();
				
				this.mknBySubcarriers.put(key, (int)Math.round(mobile.getSNR(agent, timeslot, key)));
			}
		}
	}
	
	
	/**
	 * Return true if the mobile linked to this can be managed in the selected subcarrier
	 * @param subcarrier
	 * @return
	 */
	public boolean isBeamFormingable(int subcarrier) {
		return this.mknBySubcarriers.containsKey(subcarrier);
	}
	
	public int getMkn(int subcarrier) {
		if(Config.DEBUG_FIX_MKN)
			return Config.DEBUG_FIX_MKN_VALUE;
		return this.mknBySubcarriers.get(subcarrier);
	}
}
