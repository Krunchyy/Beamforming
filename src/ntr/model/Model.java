package ntr.model;

import ntr.environement.Environement;

public abstract class Model implements IModel{
	public Location _loc;
	public Environement _env;
	
	public Model(Location loc, Environement env)
	{
		_loc = loc;
		_env = env;
		if(_env != null)
		{
			_env.addModel(this);
		}
	}
	
	@Override
	public void setEnv(Environement env){
		if(_env != null)
		{
			_env.removeModel(this);
		}
		
		else if(env != null)
		{
			env.addModel(this);
		}
	}
	
	
	public char getTag() {
		return ' ';
	}
	

	public Location getLocation() {
		return _loc;
	}

	public Environement getEnvironement() {
		return _env;
	}
}
