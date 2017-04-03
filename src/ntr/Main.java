package ntr;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.BeamFormingMaxSNR;
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
		Config.OFDM_FOR_ALL = true;
		Config.SIZE = 15;
		Config.MAX_AVERAGE = 6;
		Config.MIN_AVERAGE = 6;
		Config.MAX_OFFSET = 4;
		Config.MIN_OFFSET = 0;
		Config.OFDM_NB_SUB_CARRIER = 50;
		Config.OFDM_NB_TIME_SLOT = 10;
		
		_env = new Environement(Config.ENVIRONEMENT_SIZE);
		Agent a = new Agent(new Location(1, 1), _env);
		Agent z = new Agent(new Location(9, 9), _env);
		z.setTag('Z');
		Mobile b = new Mobile(new Location(2,0), _env);
		b.setTag('B');
		//Mobile c = new Mobile(new Location(9,0), _env);
		//c.setTag('C');
		Mobile d = new Mobile(new Location(5,5), _env);
		d.setTag('D');
		//Mobile mob4 = new Mobile(new Location(4,0), _env);
		//mob4.setTag('E');
		
		a.setOrdonnanceur(new BeamFormingMaxSNR(a.map ,a._ofdm));
		z.setOrdonnanceur(new BeamFormingMaxSNR(z.map ,z._ofdm));
		
		a.requestConnecte(b);
		a.requestConnecte(d);
		
		//z.requestConnecte(c);
		z.requestConnecte(d);
		
		//_env._mainAgent.requestConnecte(mob3);
		//_env._mainAgent.requestConnecte(mob4);

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
