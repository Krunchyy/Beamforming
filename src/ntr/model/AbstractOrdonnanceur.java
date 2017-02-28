package ntr.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.OFDM;
import ntr.signal.PacketFragment;

public abstract class AbstractOrdonnanceur {
	private OFDM ofdm;
	
	private ConcurrentHashMap<IModel, Queue<PacketFragment>> map;
	
	/**
	 * All ordonnanceurs event Opportunistic one should have access to the map in parameters
	 * @param map
	 * @param ofdm 
	 */
	public AbstractOrdonnanceur(ConcurrentHashMap<IModel, Queue<PacketFragment>> map, OFDM ofdm) {
		this.map = map;
		this.ofdm = ofdm;
	}
	
	public ConcurrentHashMap<IModel, Queue<PacketFragment>> getMap() {
		return map;
	}

	/**
	 * tick d'horloge pour faire faire son boulot à l'ordonnanceur
	 */
	public abstract void tick();
	
	
	public OFDM getOfdm() {
		return this.ofdm;
	}
}
