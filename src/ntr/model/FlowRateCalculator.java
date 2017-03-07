package ntr.model;

import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.PacketFragment;

public class FlowRateCalculator {
	
	private Agent _agent;
	
	public FlowRateCalculator(Agent agent) {
		this._agent = agent;
	}
	
	public void tick() {
		ConcurrentHashMap<IModel, Queue<PacketFragment>> map = _agent.getMap();
		Set<IModel> keys = map.keySet();
		Iterator<IModel> it = keys.iterator();
		
		while(it.hasNext()) {
			Mobile mobile = (Mobile) it.next();
			mobile.computeSNR(_agent);
		}
	}

}
