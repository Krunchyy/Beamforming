package ntr.environement.pertubation;

import java.util.List;

import ntr.environement.Environement;
import ntr.model.IModel;
import ntr.signal.Alteration;
import ntr.utils.Config;
import ntr.utils.RandomUtils;

public class Shadowing extends Alteration{
	
	private HashMap<IModel, Integer> frequencyStates;
	
	public Shadowing() {
		this.frequencyStates = new HashMap<>();
	}

	@Override
	public void alterate(List<IModel> elements, Environement env) {
		for(IModel model : elements) {
			if(model.getTag() == Config.MOBILE_TAG) {
				if(!this.frequencyStates.containsKey(model))
					this.updateNetworkCondition(model);
				else if(this.frequencyStates.get(model) > Config.SHADOWING_UPATE_FREQUENCY) {
					this.updateNetworkCondition(model);
				}
				else
					this.frequencyStates.put(model, this.frequencyStates.get(model) + 1);
			}
		}
		
	}
	
	/**
	 * This minimalistic version of shadowing update actual network condition of IModel according to Config.SHADOWING_MAXIMUM_ALTERATION
	 * @param model the model network conditions to update
	 */
	private void updateNetworkCondition(IModel model) {
		
		//Warning for now if Pathloss update network condition at every pick, it may be the wrong way to do.
		int current = model.getNetworkCondition();
		int shadowing = RandomUtils.get(- Config.SHADOWING_MAXIMUM_ALTERATION, Config.SHADOWING_MAXIMUM_ALTERATION + 1);
		//System.err.println("Shadowing compute: " + shadowing);
		model.setNetworkCondition(current + shadowing);
		this.frequencyStates.put(model, 1);
	}


}
