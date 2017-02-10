package ntr.model;

import ntr.environement.Environement;

public interface IModel {
	public char getTag();
	public Location getLocation();
	public Environement getEnvironement();
	public void setEnv(Environement env);
}
