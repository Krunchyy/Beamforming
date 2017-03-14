package ntr.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.OFDM;
import ntr.signal.PacketFragment;

public class ModulationCalculator {
	
	private PacketFragment[][] ofdm;
	
	
	public ModulationCalculator(OFDM ofdm){
		this.ofdm = ofdm._ofdm;
	}
	
	public void tick(){
		for(int x = 0; x < 10; x++ ){
			for(int  y = 0; y < 10; y++){
				ofdm[x][y]._modulation = ntr.utils.RandomUtils.get(0, 10);
			}
		}
	}


}
