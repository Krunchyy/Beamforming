package ntr.model;

import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.Packet;

public class FlowRateCalculator {
	
	private Agent _agent;
	
	public FlowRateCalculator(Agent agent) {
		_agent = agent;
	}
	
	public void tick() {
		ConcurrentHashMap<Mobile, Queue<Packet>> map = _agent.getMap();
		Set<Mobile> keys = map.keySet();
		Iterator<Mobile> it = keys.iterator();
		
		while(it.hasNext()) {
			Mobile mobile = it.next();
			mobile.computeAllSNR(_agent);
			mobile.computeBeamCarrier(_agent);
		}
	}

}
