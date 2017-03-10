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
import ntr.utils.Config;

public class ModelisationGraph {

	public static long DELAY_BETWEEN_TIME_SLOT = 20;//in MILLISECONDS
	public static long MAX_TIME = 70;//100 delay
	public static long _time = 0;
	
	public static int ENVIRONEMENT_SIZE = 10;
	public static int SIZE = 40;
	
	public static HashMap<IModel, Integer> _diff = new HashMap<>();
	
	public static Environement _env;
	public static Agent _agent; 
	public static Mobile _mob;

	static ScheduledExecutorService executorService;
	
	public static void main(String[] args)
	{
		Config.MAX_AVERAGE = 10;
		Config.MIN_AVERAGE = 1;
		Config.MAX_OFFSET = 1;
		Config.MIN_OFFSET = -1;
		Config.OFDM_NB_SUB_CARRIER = 40;
		Config.OFDM_NB_TIME_SLOT = 40;
		_env = new Environement(ENVIRONEMENT_SIZE);
		_mob = new Mobile(new Location(0,0), _env);
		_agent = new Agent(new Location(3,1), _env);
		_agent.requestConnecte(_mob);
		
		startSimulation();
	}
	
	public static void startSimulation()
	{
		executorService = Executors.newScheduledThreadPool(5);
		executorService.scheduleAtFixedRate(() -> tick(), 100, DELAY_BETWEEN_TIME_SLOT, TimeUnit.MILLISECONDS);
		
	}
	public static ArrayList<Coordonnee> coord = new ArrayList<>();
	public static void tick()
	{
		try{
		if(++_time >= MAX_TIME)
		{
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println(Graph.displayGraph(coord.toArray(new Coordonnee[0]), "Tick", "Qte"));
			//System.out.println("end" );
			_time = 0;coord.clear();
			//executorService.shutdown();
		}
		else
		{
			_agent.getPacketGen().tick();
			coord.add(new Coordonnee((int)_time, _agent.getMap().get(_mob).size()));
			_agent.getMap().get(_mob).clear();
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}