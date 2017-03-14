package ntr.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.OFDM;
import ntr.signal.PacketFragment;

public class ModulationCalculator {
	
	private OFDM _ofdm;
	private ConcurrentHashMap<IModel, Queue<PacketFragment>> _map;
	
	
	public ModulationCalculator(ConcurrentHashMap<IModel, Queue<PacketFragment>> map, OFDM ofdm){
		this._ofdm = ofdm;
		this._map  = map;
	}
	
	public void tick(){
	}


}
