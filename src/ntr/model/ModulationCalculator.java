package ntr.model;

import ntr.signal.OFDM;
import ntr.signal.PacketFragment;

public class ModulationCalculator {
	
	private PacketFragment[][] ofdm;
	private int nb_time_slot;
	private int nb_sub_carrier;
	
	
	public ModulationCalculator(OFDM ofdm){
		this.ofdm = ofdm._ofdm;
		nb_time_slot = ofdm._nb_time_slot;
		nb_sub_carrier = ofdm._nb_sub_carrier;
	}
	
	public void tick(){
		if (ofdm != null)
			
		for(int x = 0; x < nb_time_slot; x++ ){
			for(int  y = 0; y < nb_sub_carrier; y++){
				if(ofdm[x][y] != null){
					// TODO : change this value with a real value
					ofdm[x][y]._mkn = ntr.utils.RandomUtils.get(0, 10);
				}
			}
		}

	}


}
