package ntr.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.PacketFragment;
import ntr.signal.Packet;
import ntr.utils.Config;
import ntr.utils.RandomUtils;

public class PacketGenerator {
	private Agent _agent;
	public HashMap<Mobile, Double> _modifier = new HashMap<>();
	public ArrayList<Integer> totals = new ArrayList<>();
	
	public PacketGenerator(Agent agent) {
		this._agent = agent;
	}
	
	/**
	 * Generate a random quantity of packets for every mobile connected to the agent
	 */
	public void tick() {
		ConcurrentHashMap<IModel, Queue<Packet>> map = _agent.getMap();
		Set<IModel> keys = map.keySet();
		Iterator<IModel> it = keys.iterator();
		int totalPacket = 0;
		
		while(it.hasNext()) {
			Mobile mobile = (Mobile) it.next();
			
			// number of packets to generate
			int nbPacketsMoyen = mobile.getPacketFlow();
			if(nbPacketsMoyen == -1) {
				int expireDelay;
				if(Config.MIN_DELAY == Config.MAX_DELAY){
					expireDelay = Config.MAX_DELAY;
				}
				else expireDelay = Config.MIN_DELAY + RandomUtils.get(Config.MIN_DELAY, Config.MAX_DELAY);
				if(Config.MIN_AVERAGE == Config.MAX_AVERAGE){
					nbPacketsMoyen = Config.MAX_AVERAGE;
				}
				else nbPacketsMoyen = Config.MIN_AVERAGE + RandomUtils.get(Config.MIN_AVERAGE, Config.MAX_AVERAGE);
				if(_modifier.containsKey(mobile))
				{
					nbPacketsMoyen *= _modifier.get(mobile);
				}
				mobile.setPacketFlow(nbPacketsMoyen, expireDelay);
			}
			int nbPackets = nbPacketsMoyen;
			int offset;
			if(Config.MIN_OFFSET == Config.MAX_OFFSET){
				offset = Config.MAX_OFFSET;
			}
			else offset = RandomUtils.get(Config.MIN_OFFSET, Config.MAX_OFFSET);
			nbPackets += offset;
			
			// generation of packets
			for(int i=0; i != nbPackets; i++) {
				Packet p = new Packet(_agent, mobile, _agent.getEnvironement().getCurrentTick());
				try {
					map.get(mobile).add(p);
				}
				catch(Exception e) {
					
				}
			}	
			totalPacket += nbPackets;
		}
		totals.add(totalPacket);
	}
}