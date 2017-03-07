package ntr.simulation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.Location;
import ntr.model.Mobile;
import ntr.signal.OFDM;
import ntr.signal.PacketFragment;
import ntr.utils.Config;

public class ModelisationOFDM {

	
	public static long DELAY_BETWEEN_TIME_SLOT = 1000;//in MILLISECONDS
	public static long MAX_TIME = 1000;//100 delay
	public static long _time = 0;
	
	public static int ENVIRONEMENT_SIZE = 10;
	public static int SIZE = 40;
	
	
	public static Environement _env;
	public static Agent _agent; 

	static ScheduledExecutorService executorService;
	
	public static void main(String[] args)
	{
		_env = new Environement(ENVIRONEMENT_SIZE);
		
		_agent = new Agent(new Location(3,1), _env);
		Config.MAX_AVERAGE = 2;
		Config.MIN_AVERAGE = 1;
		Config.MAX_OFFSET = 5;
		Config.MIN_OFFSET = -1;
		Config.OFDM_NB_SUB_CARRIER = 20;
		Config.OFDM_NB_TIME_SLOT = 20;
		Mobile mob1 = new Mobile(new Location(0,0), _env);
		mob1.setTag('1');
		Mobile mob2 = new Mobile(new Location(2,0), _env);
		mob2.setTag('2');
		Mobile mob3 = new Mobile(new Location(3,0), _env);
		mob3.setTag('3');
		Mobile mob4 = new Mobile(new Location(4,0), _env);
		mob4.setTag('4');
		_agent.requestConnecte(mob1);
		_agent.requestConnecte(mob2);
		_agent.requestConnecte(mob3);
		_agent.requestConnecte(mob4);
		
		startSimulation();
	}
	
	public static void startSimulation()
	{
		executorService = Executors.newScheduledThreadPool(5);
		executorService.scheduleAtFixedRate(() -> tick(), 100, DELAY_BETWEEN_TIME_SLOT, TimeUnit.MILLISECONDS);
		
	}
	
	public static void tick()
	{
		
		try{
		if(++_time >= MAX_TIME)
		{
			executorService.shutdown();
		}
		else
		{
			for(int i = 0 ; i < _agent._ofdm._nb_time_slot; i++)
			{
				_agent.tick();
			}
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("============== DISPLAY ============");
			System.out.print(_agent.displayBuffer(SIZE));
			displayOFDM(_agent._ofdm, _time);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	public static void displayOFDM(OFDM ofdm, long time)
	{
		String display = "Tick : "+ (_time*_agent._ofdm._nb_time_slot)+ "\n -";
		for(int x = 0 ; x < ofdm._nb_time_slot ; x++)
		{
			display += "--";
		}
		display += "\n";
		
		for(int x = 0  ; x < ofdm._nb_time_slot ; x++)
		{
			display += " |";
			for(int y = 0 ; y < ofdm._nb_sub_carrier ; y++)
			{
				PacketFragment packetF= ofdm._ofdm[y][x];
				display += (packetF == null ? " " : packetF._target.getTag())+"|";
			}
			display += "\n";
		}
		
		display += " -";
		for(int x = 0 ; x < ofdm._nb_time_slot ; x++)
		{
			display += "--";
		}
		
		display += "\n";
		System.out.println(display);
	}
}
