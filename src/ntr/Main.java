package ntr;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.Location;
import ntr.model.MaxSNR;
import ntr.model.Mobile;
import ntr.ui.JSwingGui;
import ntr.ui.interf.IGui;
import ntr.utils.Config;
import ntr.utils.EEvent;
import ntr.utils.EventDispatcher;

public class Main {
	public static IGui _gui;
	public static Environement _env;
	public static EventDispatcher _dispatcher;
	
	public static void main(String[] args)
	{
		Config.MAX_AVERAGE = 4;
		Config.MIN_AVERAGE = 1;
		Config.MAX_OFFSET = 1;
		Config.MIN_OFFSET = -1;
		Config.OFDM_NB_SUB_CARRIER = 10;
		Config.OFDM_NB_TIME_SLOT = 10;
		
		_env = new Environement(Config.ENVIRONEMENT_SIZE);
		new Agent(new Location(3,1), _env);
		Mobile mob1 = new Mobile(new Location(0,0), _env);
		mob1.setTag('B');
		Mobile mob2 = new Mobile(new Location(2,0), _env);
		mob2.setTag('C');
		Mobile mob3 = new Mobile(new Location(3,0), _env);
		mob3.setTag('D');
		Mobile mob4 = new Mobile(new Location(4,0), _env);
		mob4.setTag('E');
		
		_env._mainAgent.setOrdonnanceur(new MaxSNR(_env._mainAgent.map ,_env._mainAgent._ofdm));
		_env._mainAgent.requestConnecte(mob1);
		_env._mainAgent.requestConnecte(mob2);
		_env._mainAgent.requestConnecte(mob3);
		_env._mainAgent.requestConnecte(mob4);

		_dispatcher = new EventDispatcher();
		
		_gui = new JSwingGui(_env, _dispatcher);
		_gui.Start();
		startSimulation();
	}
	
	public static long DELAY_BETWEEN_TIME_SLOT = 1000;//in MILLISECONDS
	
	public static ScheduledExecutorService executorService;
	
	public static void startSimulation()
	{
		executorService = Executors.newScheduledThreadPool(5);
		executorService.scheduleAtFixedRate(() -> tick(), 100, DELAY_BETWEEN_TIME_SLOT, TimeUnit.MILLISECONDS);
	}
	
	public static void tick()
	{
		try
		{
			_env.tick(Config.OFDM_NB_TIME_SLOT, true);
			if(!_env._stopAuto)
			_dispatcher.onEvent(EEvent.GUI_UPDATE, new Object[]{_env.getCurrentTick()}, null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
