package ntr.model;

import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.Packet;

public class PacketGenerator {	
	private int min, max; // quantite min/max de paquets a generer
	private Agent agent;
	
	public PacketGenerator(Agent newAgent) {
		this.min = 1;
		this.max = 20;
		this.agent = newAgent;				
	}
	
	/**
	 * Designe un nombre aleatoire de mobiles rataches a l'agent et leur
	 * attribue une quantite aleatoire de paquets
	 */
	public void tick() {
		ConcurrentHashMap<IModel, Queue<Packet>> map = agent.getMap();
		int nbMobiles = map.size();
		
		for(int i=0; i != nbMobiles; i++) {
			// alea du mobile cible
			Random r = new Random();
			int numMobile = 0 + r.nextInt(nbMobiles - 0);
			
			Set<IModel> keys = map.keySet();
			Object[] t = keys.toArray();
			Mobile mobile = (Mobile) t[numMobile];
			
			// alea du nb de paquets a generer
			int nbPackets = 0;
			r = new Random();
			nbPackets = min + r.nextInt(max - min);
			
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