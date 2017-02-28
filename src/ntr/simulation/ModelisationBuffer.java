package ntr.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.IModel;
import ntr.model.Location;
import ntr.model.Mobile;

public class ModelisationBuffer {

	/**
	 * Fix : source issue d'un Mobile
	 * Fix : Temps
	 * 
	 *	% remplisage buffer 
	 * 		^
	 * 		|
	 * 		|
	 * 		|
	 * 		|
	 * 		|
	 * 		|____________________________\ nombre utilisateur
	 *    		 						 /
	 */
	
	public static long DELAY_BETWEEN_TIME_SLOT = 100;//in MILLISECONDS
	public static long MAX_TIME = 10;//100 delay
	public static long _time = 0;
	
	public static int ENVIRONEMENT_SIZE = 10;
	public static int SIZE = 40;
	
	public static HashMap<IModel, Integer> _diff = new HashMap<>();
	
	public static Environement _env;
	public static Agent _agent; 

	static ScheduledExecutorService executorService;
	
	public static void main(String[] args)
	{
		_env = new Environement(ENVIRONEMENT_SIZE);
		
		_agent = new Agent(new Location(3,1), _env);
		
		_agent.requestConnecte(new Mobile(new Location(0,0), _env));
		
		startSimulation();
	}
	
	public static void startSimulation()
	{
		executorService = Executors.newScheduledThreadPool(5);
		executorService.scheduleAtFixedRate(() -> tick(), 100, DELAY_BETWEEN_TIME_SLOT, TimeUnit.MILLISECONDS);
		
	}
	public static ArrayList<Coordonnee> coord = new ArrayList<>();
	public static int nbUser = 1;
	public static void tick()
	{
		
		try{
		if(++_time >= MAX_TIME)
		{
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println(Graph.displayGraph(coord.toArray(new Coordonnee[0]), "Buffer", "NbUser"));
			executorService.shutdown();
		}
		else
		{
			System.out.println("time : "+ _time);
			for(int i = 0 ; i < 500; i++)
			{
				System.out.println("tick");
				_agent.tick();
			}
			

			System.out.println("build coord");
			for(IModel m : _agent.getMap().keySet())
			{
				coord.add(new Coordonnee(nbUser, _agent.getMap().get(m).size() / 255));
			}

			System.out.println("next");
			_agent.requestConnecte(new Mobile(new Location(0,0), _env));
			nbUser++;
			
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
