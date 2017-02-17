package ntr.model;

import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.Packet;

public class PacketGenerator {
	private int minMoyen = 1; // quantite moyenne de paquets
	private int maxMoyen = 20;
	private int minDelay = 2; // duree de vie de la moyenne
	private int maxDelay = 5;
	private int minOffset = -2; // offset par rapport a la moyenne
	private int maxOffset = 2;
	private Random random;
	private Agent agent;
	
	public PacketGenerator(Agent newAgent) {
		this.agent = newAgent;
		this.random = new Random();
	}
	
	/**
	 * Genere un nombre aleatoire de paquets pour chaque mobile
	 * connecte a l'agent
	 */
	public void tick() {
		ConcurrentHashMap<IModel, Queue<Packet>> map = agent.getMap();
		int nbMobiles = map.size();
		Set<IModel> keys = map.keySet();
		Object[] tabMobiles = keys.toArray();
		
		
		for(int i=0; i != nbMobiles; i++) {
			Mobile mobile = (Mobile) tabMobiles[i];
			
			// alea du nb de paquets a generer
			int nbPacketsMoyen = mobile.getPacketFlow();
			if(nbPacketsMoyen == -1) {
				int expireDelay = minDelay + random.nextInt(maxDelay - minDelay);
				nbPacketsMoyen = minMoyen + random.nextInt(maxMoyen - minMoyen);
				mobile.setPacketFlow(nbPacketsMoyen, expireDelay);
			}
			int nbPackets = nbPacketsMoyen;
			int offset = random.nextInt(maxOffset - minOffset);
			nbPackets += offset;
			
			// generation des paquets
			for(int j = 0; j != nbPackets; j++) {
				Packet p = new Packet(agent, mobile, "");
				try {
					map.get(mobile).add(p);
				}
				catch(Exception e) {
					
				}
			}	
		}		
	}
}