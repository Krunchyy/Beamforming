package ntr.simulation;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.Location;
import ntr.model.MaxSNR;
import ntr.model.Mobile;
import ntr.signal.PacketFragment;
import ntr.utils.BuildCSV;
import ntr.utils.Config;
import ntr.utils.EEvent;
import ntr.utils.RandomUtils;

public class BuildCSVDebitByUsers {
	public static Environement _env;
	
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
		_env._mainAgent.setOrdonnanceur(new MaxSNR(_env._mainAgent.map ,_env._mainAgent._ofdm));

		startSimulation();
		
	}
	
	public static final int _maxMobile = 100;
	public static final int _nbODFMTrameByRoll = 100;
	/**
	 * Do N roll with 
	 * 1 ,2 ... N mobile,
	 * and logs systems debit
	 */
	public static void startSimulation(){
		System.out.println("Start Simulation : BuildCSVDebitByUsers");
		long[] result = new long[_maxMobile];
		for(int nbMobiles = 0; nbMobiles < _maxMobile ; nbMobiles++)
		{
			System.out.println("["+nbMobiles + "/"+ _maxMobile+"]");
			long debit = 0;
			for(int nbRoll = 0 ; nbRoll < _nbODFMTrameByRoll ; nbRoll++)
			{
				try
				{
					_env.tick(Config.OFDM_NB_TIME_SLOT);
					
					for(PacketFragment pack : _env.getEnvBuffer())
					{
						debit += pack._dataSize;
					}
					
					_env.getEnvBuffer().clear();
					_env._mainAgent.generator.totals.clear();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			debit /= _nbODFMTrameByRoll;
			result[nbMobiles] = debit;
			
			
			Mobile mob = new Mobile(new Location(RandomUtils.get(0, 10),RandomUtils.get(0, 10)), _env);
			_env._mainAgent.requestConnecte(mob);
		}
		
		BuildCSV.buildCSV("debitByMobileMaxSNR", result, new String[]{"NbMobile", "Debit"});
		
		System.out.println("DONE");
	}
}