package ntr.environement.pertubation;

import java.util.HashMap;
import java.util.List;

import ntr.environement.Environement;
import ntr.model.IModel;
import ntr.signal.Alteration;
import ntr.utils.Config;

public class Shadowing extends Alteration{
	
	private HashMap<IModel, Integer> frequencyStates;
	
	public Shadowing() {
		this.frequencyStates = new HashMap<>();
	}

	@Override
	public void alterate(List<IModel> elements, Environement env) {
		for(IModel model : elements) {
			if(model.getTag() == Config.MOBILE_TAG) {
				
			}
		}
		
	}


}
