package ntr.model;

import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.PacketFragment;
import ntr.utils.RandomUtils;

public class PacketGenerator {
	private int minMoyen = 1; // quantite moyenne de paquets
	private int maxMoyen = 20;
	private int minDelay = 2; // duree de vie de la moyenne
	private int maxDelay = 5;
	private int minOffset = -2; // offset par rapport a la moyenne
	private int maxOffset = 2;
	private Agent agent;
	
	public PacketGenerator(Agent newAgent) {
		this.agent = newAgent;
	}
	
	/**
	 * Genere un nombre aleatoire de paquets pour chaque mobile
	 * connecte a l'agent
	 */
	public void tick() {
		ConcurrentHashMap<IModel, Queue<PacketFragment>> map = agent.getMap();
		Set<IModel> keys = map.keySet();
		Iterator<IModel> it = keys.iterator();
		
		while(it.hasNext()) {
			Mobile mobile = (Mobile) it.next();
			
			// alea du nombre de paquets a generer
			int nbPacketsMoyen = mobile.getPacketFlow();
			if(nbPacketsMoyen == -1) {
				int expireDelay = minDelay + RandomUtils.get(minDelay, maxDelay);
				nbPacketsMoyen = minMoyen + RandomUtils.get(minMoyen, maxMoyen);
				mobile.setPacketFlow(nbPacketsMoyen, expireDelay);
			}
			int nbPackets = nbPacketsMoyen;
			int offset = RandomUtils.get(minOffset, maxOffset);
			nbPackets += offset;
			
			// generation des paquets
			for(int i=0; i != nbPackets; i++) {
				long date = System.currentTimeMillis();
				PacketFragment p = new PacketFragment(agent, mobile, "", date);
				try {
					map.get(mobile).add(p);
				}
				catch(Exception e) {
					
				}
			}	
		}
	}
}