package ntr.simulation;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.Location;
import ntr.model.MaxSNR;
import ntr.model.Mobile;
import ntr.signal.Packet;
import ntr.utils.BuildCSV;
import ntr.utils.Config;

public class BuildCSVDebitByUsers {

	public static final boolean SNR = false;
	
	public static Environement _env;
	public static void main(String[] args)
	{
		Config.SIZE = 5;
		Config.MAX_AVERAGE = 0;
		Config.MIN_AVERAGE = 0;
		Config.MAX_OFFSET = 4;
		Config.MIN_OFFSET = 0;
		Config.OFDM_NB_SUB_CARRIER = 50;
		Config.OFDM_NB_TIME_SLOT = 10;
		
		_env = new Environement(Config.ENVIRONEMENT_SIZE);
		new Agent(new Location(3,1), _env);
		if(SNR)
			_env._mainAgent.get(0).setOrdonnanceur(new MaxSNR(_env._mainAgent.get(0).map ,_env._mainAgent.get(0)._ofdm));

		startSimulation(SNR ? "debitByMobileMaxSNRPacket": "debitByMobileRR");
		
	}
	
	public static final int _maxMobile = 17;
	public static final int _nbODFMTrameByRoll = 100;
	/**
	 * Do N roll with 
	 * 1 ,2 ... N mobile,
	 * and logs systems debit
	 */
	public static void startSimulation(String fileName){
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
					
					for(Packet pack : _env.getEnvBuffer())
					{
						debit += pack.getSize();
					}
					//System.out.println("debit : "+ debit + " nb packet send : "+ _env.getEnvBuffer().size());
					_env.getEnvBuffer().clear();
					_env._mainAgent.get(0).generator.totals.clear();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			debit /= _nbODFMTrameByRoll;
			//System.out.println("debit Moyen : "+ debit);
			result[nbMobiles] = debit;
			
			
			Mobile mob = new Mobile(new Location(10,10), _env);
			_env._mainAgent.get(0).requestConnecte(mob);
		}
		
		BuildCSV.buildCSV(fileName, result, new String[]{"NbMobile", "Debit"});
		
		System.out.println("DONE");
	}
}
