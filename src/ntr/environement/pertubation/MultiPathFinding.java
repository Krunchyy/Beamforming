package ntr.environement.pertubation;

import java.util.List;

import ntr.environement.Environement;
import ntr.model.IModel;
import ntr.model.Mobile;
import ntr.signal.Alteration;
import ntr.utils.RandomUtils;
public class MultiPathFinding extends Alteration{


	@Override
	public void alterate(List<IModel> elements, Environement env) {
		// TODO Auto-generated method stub
		
	}

	public void multipathfadingAlteration (Mobile mobile){
		int random = RandomUtils.get(-50, 51);
		int network = mobile.getNetworkCondition();
		network += random;
		mobile.setNetworkCondition(network);
		
	}

}
