package ntr.signal;

import ntr.environement.Environement;
import ntr.model.IModel;

public interface Alteration {
	
	/**
	 * Compute alteration of a signal to and environement
	 * @param frequency
	 * @param nbModulation
	 * @param data
	 * @param env
	 * @return signal data altered by this alteration
	 */
	public int[] alterate(IModel sender, Environement env);
}
