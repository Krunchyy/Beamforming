package ntr.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ntr.environement.Environement;

public class Mobile extends Model{
	
	public Mobile(Location loc, Environement env)
	{
		super(loc, env);
	}
	
	protected final static synchronized Map<Integer, ?> truc(final int i)
	{
		return new HashMap<Integer, ArrayList<IModel>>();
	}
	
	@Override
	public char getTag() {
		return 'M';
	}
}
