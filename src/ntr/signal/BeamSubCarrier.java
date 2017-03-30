package ntr.signal;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class BeamSubCarrier {
	private ConcurrentHashMap<Integer, Double> mknBySubcarriers;
	
	
	public BeamSubCarrier(ConcurrentHashMap<Integer, Double> mknBySubcarriers) {
		this.mknBySubcarriers = mknBySubcarriers;
	}
	
	
	/**
	 * Return true if the mobile linked to this can be managed in the selected subcarrier
	 * @param subcarrier
	 * @return
	 */
	public boolean isBeamFormingable(int subcarrier) {
		return this.mknBySubcarriers.containsKey(subcarrier);
	}
	
	public Double getMkn(int subcarrier) {
		return this.mknBySubcarriers.get(subcarrier);
	}
}
