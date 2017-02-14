package ntr.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.Paquet;

public abstract class AbstractOrdonnanceur {
	private ConcurrentHashMap<IModel, Queue<Paquet>> map;
	
	/**
	 * All ordonnanceurs event Opportunistic one should have access to the map in parameters
	 * @param map
	 */
	public AbstractOrdonnanceur(ConcurrentHashMap<IModel, Queue<Paquet>> map) {
		this.map = map;
	}
	
	public ConcurrentHashMap<IModel, Queue<Paquet>> getMap() {
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
