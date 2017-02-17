package ntr.simulation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.Location;
import ntr.model.Mobile;

/**
 * One Agent,
 * One Mobile,
 * 
 * pseudo random download to mobile
 * 
 * Static Modulation
 * 
 * PathLost,
 * Shadowing,
 * MultiPathFinding
 * 
 * @author Roche Kevin
 */
public class Simulation1 {
	public static int ENVIRONEMENT_SIZE = 10;
	public static int NB_AGENT = 1;
	public static int NB_MOBILE = 1;
	
	public static long DELAY_BETWEEN_TIME_SLOT = 2000;//in MILLISECONDS
	public static long MAX_TIME = 100;//100 delay
	
	public static Environement _env;
	public static Mobile _mobile; 
	public static Agent _agent; 
	public static long _time = 0;
	
	static ScheduledExecutorService executorService;
	
	public static void main(String[] args)
	{
		_env = new Environement(ENVIRONEMENT_SIZE);
		
		_mobile = new Mobile(new Location(0,0), _env);//auto env reg
		_agent = new Agent(new Location(3,1), _env);
		
		_agent.requestConnecte(_mobile);
		
		startSimulation();
	}
	
	public static void startSimulation()
	{
		executorService = Executors.newScheduledThreadPool(5);
		executorService.scheduleAtFixedRate(() -> tick(),100, DELAY_BETWEEN_TIME_SLOT,TimeUnit.MILLISECONDS);
		
	}
	
	public static void tick(){
		System.out.println("--> tick");
		try{
			if(++_time >= MAX_TIME)
			{
				System.out.println("--> shutdown");
				executorService.shutdown();
			}
			else
			{
				_env.tick();
			}
		}
		catch(Exception e)
		{
				e.printStackTrace();
		}
		System.out.println("<-- tick");
	}
}
