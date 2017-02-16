package ntr.model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
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
	 * Genere un nombre aleatoire de paquets sur une quantite aleatoire
	 * de mobiles
	 */
	public void tick() {
		ConcurrentHashMap<IModel, Queue<Packet>> map = agent.getMap();
		
		// alea du nb de paquets a generer
		int nbPackets = 0;
		Random r = new Random();
		nbPackets = min + r.nextInt(max - min);
		
		// generation des paquets
		for(int i = 0; i != nbPackets; i++) {
			// alea du mobile cible
			r = new Random();
			int numMobile = 0 + r.nextInt(map.size() - 0);
			
			Set<IModel> keys = map.keySet();
			Object[] t = keys.toArray();
			
			Mobile mobile = (Mobile) t[numMobile];
			Packet p = new Packet(agent, mobile, "");
			map.get(mobile).add(p);
		}
	}
	
	public static void main(String[] args) { //tests
		Queue<Packet> file = new LinkedList<Packet>();
		Location loc = new Location(5, 5);
		Environement env = new Environement(20);
		Mobile m1 = new Mobile(loc, env);
		Mobile m2 = new Mobile(loc, env);
		Agent agent = new Agent(loc, env);
		
		/*packetGen(1, 10, agent, file);
		int i=0;
		while(! file.isEmpty()){
			System.out.println(file.remove());
			i++;
		}
		System.out.println("nbPackets = "+i);*/
	}
}