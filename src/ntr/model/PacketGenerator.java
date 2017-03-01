package ntr.model;

import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.PacketFragment;
import ntr.utils.Config;
import ntr.utils.RandomUtils;

public class PacketGenerator {
	private Agent _agent;
	
	public PacketGenerator(Agent agent) {
		this._agent = agent;
	}
	
	/**
	 * Generate a random quantity of packets for every mobile connected to the agent
	 */
	public void tick() {
		ConcurrentHashMap<IModel, Queue<PacketFragment>> map = _agent.getMap();
		Set<IModel> keys = map.keySet();
		Iterator<IModel> it = keys.iterator();
		
		while(it.hasNext()) {
			Mobile mobile = (Mobile) it.next();
			
			// number of packets to generate
			int nbPacketsMoyen = mobile.getPacketFlow();
			if(nbPacketsMoyen == -1) {
				int expireDelay = Config.MIN_DELAY + RandomUtils.get(Config.MIN_DELAY, Config.MAX_DELAY);
				nbPacketsMoyen = Config.MIN_AVERAGE + RandomUtils.get(Config.MIN_AVERAGE, Config.MAX_AVERAGE);
				mobile.setPacketFlow(nbPacketsMoyen, expireDelay);
			}
			int nbPackets = nbPacketsMoyen;
			int offset = RandomUtils.get(Config.MIN_OFFSET, Config.MAX_OFFSET);
			nbPackets += offset;
			
			// generation of packets
			for(int i=0; i != nbPackets; i++) {
				long date = System.currentTimeMillis();
				PacketFragment p = new PacketFragment(_agent, mobile, "", date);
				try {
					map.get(mobile).add(p);
				}
				catch(Exception e) {
					
				}
			}	
		}
	}
}