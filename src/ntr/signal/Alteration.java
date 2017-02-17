package ntr.signal;

import java.util.List;

import ntr.environement.Environement;
import ntr.model.IModel;

public abstract class Alteration {
	
	/**
	 * Compute alteration of a signal to and environement
	 * @param env
	 */
	public abstract void alterate(List<IModel> elements, Environement env);
	
	public static void performAlteration(List<Packet> buff, List<IModel> models) {
		/*
		 * For each models according to the networkCondition percent value
		 * set validity bit of networkCondition% packets when receiver equal current model
		 */
	}
}
