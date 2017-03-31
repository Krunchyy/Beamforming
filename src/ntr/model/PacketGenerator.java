package ntr.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.Packet;
import ntr.utils.Config;
import ntr.utils.RandomUtils;
import sun.management.resources.agent;

public class PacketGenerator {
	private Agent _agent;
	public HashMap<Mobile, Double> _modifier = new HashMap<>();
	public ArrayList<Integer> totals = new ArrayList<>();

	public PacketGenerator(Agent agent) {
		_agent = agent;
	}

	/**
	 * Generate a random quantity of packets for every mobile connected to the agent
	 */
	public void tick() {
		ConcurrentHashMap<Mobile, Queue<Packet>> map = _agent.getMap();
		Set<Mobile> keys = map.keySet();
		Iterator<Mobile> it = keys.iterator();
		int totalPacket = 0;

		//System.out.println("tick generator");
		while(it.hasNext()) {
			Mobile mobile = it.next();
			//System.out.println("next mobile");
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
			else offset = RandomUtils.get(Config.MIN_OFFSET, Config.MAX_OFFSET+1);
			nbPackets += offset;

			//System.out.println("nbPacketsMoyen : "+ nbPacketsMoyen + " nbPacket : "+nbPackets+ " offset : "+ offset);
			if(mobile.isBeamforming() && mobile._beamformingAgents.size() > 0 && mobile._beamformingAgents.get(0) == _agent) {
				int count = 0;
				for(int i=0; i != nbPackets; i++) {
					if(mobile._filePacketsBeam.size() < Config.BUFFER_SIZE) {
						Packet p = new Packet(_agent, mobile, _agent.getEnvironement().getCurrentTick());
						count += 1;
						mobile._filePacketsBeam.add(p);
						
						//It's not enough to update the map of one agent, all agents need to be aware of the new beamforming map
						for(int j = 0 ; j < _agent.getEnvironement()._mainAgent.size() ; j++) {
							_agent.getEnvironement()._mainAgent.get(j).getMap().put(mobile, mobile._filePacketsBeam);
						}
						
//						System.out.println("Reference checker:");
//						System.out.println("[" + mobile._filePacketsBeam.size() + "]" +  mobile._filePacketsBeam);
//						for (int j = 0; j < _agent.getEnvironement()._mainAgent.size(); j++) {
//							System.out.println("[" + _agent.getMap().get(mobile).size() + "]" + (mobile._filePacketsBeam.toString().equals(_agent.getMap().get(mobile).toString()) ? "IDEM AS INTERN" : "SOMEHOW DIFFERENT"));
//						}
					}
				}
				
				totalPacket += count;
				//System.out.println("["  + _agent._env._currentTick + "][" + mobile.getTag() + "] Generate Beam Packets :" + count);
				if(Config.COUNT_PACKETS) {
					if(mobile.isBeamforming()) {
						Config.BeamPacketGenerated += count;
					}
				}
			}

			else if(!mobile.isBeamforming()){
				int count = 0;
				for(int i=0; i != nbPackets; i++) {
					if(map.get(mobile).size() < Config.BUFFER_SIZE) {
						Packet p = new Packet(_agent, mobile, _agent.getEnvironement().getCurrentTick());
						count += 1;
						map.get(mobile).add(p);
					}
				}
				//System.out.println("["  + _agent._env._currentTick + "][" + mobile.getTag() + "] Generate Normal Packets " + count);
				totalPacket += count;
				
				if(Config.COUNT_PACKETS) {
					if(!mobile.isBeamforming()) {
						Config.noBeamPacketGenerated += count;
					}
				}
			}
		}
		//System.out.println("generated packet : "+totalPacket);
		totals.add(totalPacket);
	}
}