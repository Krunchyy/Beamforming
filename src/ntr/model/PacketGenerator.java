package ntr.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.signal.Packet;

public class PacketGenerator {
	
	public PacketGenerator(Agent agent)
	{
		
	}
	
	public void tick()
	{
		
	}
	// add/remove add tail/remove first
	public PacketGenerator(int min, int max, Agent agent, Environement env){
		// recuperation de la map de l agent
		ConcurrentHashMap<IModel, Queue<Packet>> map = agent.getMap();
		// recuperation des mobiles de l environnement
		List<IModel> elts = new ArrayList<>();
		for(IModel model :env.getElements())
		{
			if(model instanceof Mobile)
				elts.add(model);
		}
		
		// alea du nb de paquets a generer
		int nbPackets = 0;
		Random r = new Random();
		nbPackets = min + r.nextInt(max - min);
		
		// generation des paquets
		for(int i = 0; i != nbPackets; i++){
			// alea du mobile cible
			r = new Random();
			int numMobile = 0 + r.nextInt(map.size() - 0);
			
			Set<IModel> keys = map.keySet();
			Object[] t = keys.toArray();
			
			Mobile m = (Mobile) t[numMobile];
			Packet p = new Packet(agent, a, "");
			map.get(m).add(p);
		}
	}
	
	//Agent.map (client)
	public static void main(String[] args) {
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