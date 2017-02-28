package ntr.signal;

import java.util.List;
import java.util.stream.Collectors;

import ntr.environement.Environement;
import ntr.model.IModel;
import ntr.model.Mobile;
import ntr.utils.RandomUtils;

public abstract class Alteration {
	
	/**
	 * Compute alteration of a signal to and environement
	 * @param env
	 */
	public abstract void alterate(List<IModel> elements, Environement env);
	
	public static void performAlteration(List<PacketFragment> buff, List<IModel> models) {
		for(int i = 0 ; i < models.size() ; i++) {
			if(models.get(i).getTag() == 'A')
				continue;
			
			Mobile mobile = (Mobile) models.get(i);
			int percentSuccess = models.get(i).getNetworkCondition();
			
			List<PacketFragment> bufferOfCurrentMobile = buff.stream().filter(packet -> packet._target == mobile).collect(Collectors.toList());
		
			int size = bufferOfCurrentMobile.size();
			//size * (percentSuccess/100)
			
			for(int j = 0 ; j < size ; j++) {
				if(RandomUtils.get(0, 100) > percentSuccess) {
					bufferOfCurrentMobile.get(j)._isValid = false;
				}
			}
		}
		/*
		 * For each models according to the networkCondition percent value
		 * set validity bit of networkCondition% packets when receiver equal current model
		 */
	}
}
