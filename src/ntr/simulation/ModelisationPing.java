package ntr.simulation;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.Location;
import ntr.model.Mobile;
import ntr.signal.PacketFragment;
import ntr.utils.Config;

public class ModelisationPing {

	
	public static long DELAY_BETWEEN_TIME_SLOT = 1000;//in MILLISECONDS
	public static long MAX_TIME = 50;//100 delay
	public static long _time = 0;
	
	public static int ENVIRONEMENT_SIZE = 10;
	public static int SIZE = 40;
	
	
	public static Environement _env;
	public static Agent _agent; 

	static ScheduledExecutorService executorService;
	
	public static void main(String[] args)
	{
		Config.MAX_AVERAGE = 17;
		Config.MIN_AVERAGE = 1;
		Config.MAX_OFFSET = 1;
		Config.MIN_OFFSET = -1;
		Config.OFDM_NB_SUB_CARRIER = 40;
		Config.OFDM_NB_TIME_SLOT = 40;
		
		_env = new Environement(ENVIRONEMENT_SIZE);
		_agent = new Agent(new Location(3,1), _env);
		Mobile mob1 = new Mobile(new Location(0,0), _env);
		mob1.setTag('1');
		//Mobile mob2 = new Mobile(new Location(2,0), _env);
		//mob2.setTag('2');
		//Mobile mob3 = new Mobile(new Location(3,0), _env);
		//mob3.setTag('3');
		//Mobile mob4 = new Mobile(new Location(4,0), _env);
		//mob4.setTag('4');
		_agent.requestConnecte(mob1);
		//_agent.requestConnecte(mob2);
		//_agent.requestConnecte(mob3);
		//_agent.requestConnecte(mob4);
		
		startSimulation();
	}


	public static void startSimulation()
	{
		executorService = Executors.newScheduledThreadPool(5);
		executorService.scheduleAtFixedRate(() -> tick(), 100, DELAY_BETWEEN_TIME_SLOT, TimeUnit.MILLISECONDS);

	}
	
	public static void tick()
	{		
		ArrayList<Coordonnee> coord = new ArrayList<>();
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println("============== DISPLAY ============");
	for(int i = 0 ; i < MAX_TIME; i++)
	{
		
		for(int tick = 0 ; tick < _agent._ofdm._nb_time_slot; tick++)
		{
			_env.tick();
		}
		long average = 0;
		for(PacketFragment packet : _env.getEnvBuffer())
		{
			average += packet._dateExpedition - packet._dateCreation;
		}
		
		
		double averageR =  average / (double) _env.getEnvBuffer().size();
		coord.add(new Coordonnee(((int) (_env.getCurrentTick() / _agent._ofdm._nb_time_slot)), (int) averageR));
		_env.getEnvBuffer().clear();
	}
	_env._currentTick = 0;
		System.out.println(Graph.displayGraph(coord.toArray(new Coordonnee[0]), "Tick", "Ping"));
	}
}
