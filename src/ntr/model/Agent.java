package ntr.model;

import ntr.environement.Environement;

public class Agent extends Model{
	
	public Agent(Location loc, Environement env)
	{
		super(loc, env);
	}
	
	@Override
	public char getTag() {
		return 'A';
	}

}
