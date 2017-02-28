package ntr.model;

import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.PacketFragment;
import ntr.utils.RandomUtils;

public class PacketGenerator {
	private int _minMoyen = 1; // quantite moyenne de paquets
	private int _maxMoyen = 20;
	private int _minDelay = 2; // duree de vie de la moyenne
	private int _maxDelay = 5;
	private int _minOffset = -2; // offset par rapport a la moyenne
	private int _maxOffset = 2;
	private Agent _agent;
	
	public PacketGenerator(Agent agent) {
		this._agent = agent;
	}
	
	/**
	 * Genere un nombre aleatoire de paquets pour chaque mobile
	 * connecte a l'agent
	 */
	public void tick() {
		ConcurrentHashMap<IModel, Queue<PacketFragment>> map = _agent.getMap();
		Set<IModel> keys = map.keySet();
		Iterator<IModel> it = keys.iterator();
		
		while(it.hasNext()) {
			Mobile mobile = (Mobile) it.next();
			
			// nombre de paquets a generer
			int nbPacketsMoyen = mobile.getPacketFlow();
			if(nbPacketsMoyen == -1) {
				int expireDelay = _minDelay + RandomUtils.get(_minDelay, _maxDelay);
				nbPacketsMoyen = _minMoyen + RandomUtils.get(_minMoyen, _maxMoyen);
				mobile.setPacketFlow(nbPacketsMoyen, expireDelay);
			}
			int nbPackets = nbPacketsMoyen;
			int offset = RandomUtils.get(_minOffset, _maxOffset);
			nbPackets += offset;
			
			// generation des paquets
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