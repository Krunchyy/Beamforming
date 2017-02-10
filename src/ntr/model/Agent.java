package ntr.model;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.signal.Paquet;

public class Agent extends Model{
	
	private IOrdonnanceur ordonnanceur;
	private ConcurrentHashMap<IModel, Queue<Paquet>> map;
	
	public Agent(Location loc, Environement env)
	{
		super(loc, env);
		Queue<Paquet> q = new ArrayBlockingQueue<>(255);
		//TODO: utilisation de la Queue (utiliser la méthode add -> lève une exception quand la Queue est pleine)
	}
	
	@Override
	public char getTag() {
		return 'A';
	}

}
