package ntr.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.Packet;

public abstract class AbstractOrdonnanceur {
	private ConcurrentHashMap<IModel, Queue<Packet>> map;
	
	/**
	 * All ordonnanceurs event Opportunistic one should have access to the map in parameters
	 * @param map
	 */
	public AbstractOrdonnanceur(ConcurrentHashMap<IModel, Queue<Packet>> map) {
		this.map = map;
	}
	
	public ConcurrentHashMap<IModel, Queue<Packet>> getMap() {
		return map;
	}

	/**
	 * tick d'horloge pour faire faire son boulot à l'ordonnanceur
	 */
	public abstract void tick();
	
	/**
	 * 
	 * @return le terminal qui va recevoir ses datas.
	 */
	public abstract IModel choose();
}
