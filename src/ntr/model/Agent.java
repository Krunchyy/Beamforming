package ntr.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.signal.Paquet;

public class Agent extends Model{
	
	private IOrdonnanceur ordonnanceur;
	private ConcurrentHashMap<IModel, Queue<Paquet>> map;
	
	public Agent(Location loc, Environement env)
	{
		super(loc, env);
	}
	
	@Override
	public char getTag() {
		return 'A';
	}

}
