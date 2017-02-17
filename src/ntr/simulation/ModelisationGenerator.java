package ntr.simulation;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.IModel;
import ntr.model.Location;
import ntr.model.Mobile;

public class ModelisationGenerator {
	/**
	 *	% remplisage buffer 
	 * 		^
	 * 		|
	 * 		|
	 * 		|
	 * 		|
	 * 		|
	 * 		|____________________________\ tick (temps)
	 *    		 						 /
	 */
	
	public static long DELAY_BETWEEN_TIME_SLOT = 2000;//in MILLISECONDS
	public static long MAX_TIME = 100;//100 delay
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
		_agent.requestConnecte(new Mobile(new Location(0,0), _env));
		_agent.requestConnecte(new Mobile(new Location(0,0), _env));
		_agent.requestConnecte(new Mobile(new Location(0,0), _env));
		startSimulation();
	}
	
	public static void startSimulation()
	{
		executorService = Executors.newScheduledThreadPool(5);
		executorService.scheduleAtFixedRate(() -> tick(), 100, DELAY_BETWEEN_TIME_SLOT, TimeUnit.MILLISECONDS);
		
	}
	public static void tick()
	{
		if(++_time >= MAX_TIME)
		{

			System.out.println("end" );
			executorService.shutdown();
		}
		else
		{
			_agent.tick();
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("============== DISPLAY ============ tick : "+ _time );
			System.out.print(_agent.displayBuffer(SIZE));
		}
	}
}
