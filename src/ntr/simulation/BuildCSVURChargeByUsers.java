package ntr.simulation;

import java.util.ArrayList;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.Location;
import ntr.model.MaxSNR;
import ntr.model.Mobile;
import ntr.utils.BuildCSV;
import ntr.utils.Config;

public class BuildCSVURChargeByUsers {

	public static final boolean SNR = true;
	
	public static Environement _env;
	public static void main(String[] args)
	{
		Config.SIZE = 5;
		Config.MAX_AVERAGE = 2;
		Config.MIN_AVERAGE = 2;
		Config.MAX_OFFSET = 0;
		Config.MIN_OFFSET = 0;
		Config.OFDM_NB_SUB_CARRIER = 50;
		Config.OFDM_NB_TIME_SLOT = 10;
		
		_env = new Environement(Config.ENVIRONEMENT_SIZE);
		new Agent(new Location(3,1), _env);
		if(SNR)
			_env._mainAgent.get(0).setOrdonnanceur(new MaxSNR(_env._mainAgent.get(0).map ,_env._mainAgent.get(0)._ofdm));

		startSimulation(SNR ? "urChargeByMobileMaxSNRPacket": "urChargeByMobileRR");
		
	}
	
	public static final int _maxMobile = 17;
	public static final int _nbODFMTrameByRoll = 100;
	/**
	 * Do N roll with 
	 * 1 ,2 ... N mobile,
	 * and logs systems debit
	 */
	public static void startSimulation(String fileName){
		System.out.println("Start Simulation : "+fileName);
		ArrayList<CoordonneeL> _result = new ArrayList<>();
		
		for(int nbMobiles = 0; nbMobiles < _maxMobile ; nbMobiles++)
		{
			System.out.println("["+nbMobiles + "/"+ _maxMobile+"]");
			long ur = 0;
			int nbURUsed = 0;
			for(int nbRoll = 0 ; nbRoll < _nbODFMTrameByRoll ; nbRoll++)
			{
				try
				{
					for(int i = 0 ; i < Config.OFDM_NB_TIME_SLOT ; i++)
					{
						int nbUR = 0;
						_env.tick(Config.OFDM_NB_TIME_SLOT);
						for(int x = 0 ; x < Config.OFDM_NB_TIME_SLOT ; x++)
						{
							for(int y = 0 ; y < Config.OFDM_NB_SUB_CARRIER; y++)
							{
								if(_env._mainAgent.get(0)._ofdm._ofdm[x][y] != null)
								{
									if(_env._mainAgent.get(0)._ofdm._ofdm[x][y]._dataSize == 0)
									{
										System.out.println("yolo");
									}
									nbURUsed++;
									nbUR += _env._mainAgent.get(0)._ofdm._ofdm[x][y]._dataSize;
								}
							}
						}
						ur += nbUR;//add 1 frame
						//System.out.println("UR : "+ ur);
					}
					_env.getEnvBuffer().clear();
					_env._mainAgent.get(0).generator.totals.clear();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			nbURUsed /= _nbODFMTrameByRoll;
			
			ur /= _nbODFMTrameByRoll;
			
			double result2 = ((double)ur)/ (nbURUsed == 0 ? 1 : nbURUsed);
			System.out.println("UR : "+ ur + " NbUr : " + nbURUsed + " expected "+ result2);
			//result[nbMobiles] = result2;
			_result.add(new CoordonneeL(((long)nbMobiles), result2));
			
			Mobile mob = new Mobile(new Location(10,10), _env);
			_env._mainAgent.get(0).requestConnecte(mob);
		}
		
		BuildCSV.buildCSV(fileName, _result.toArray(new CoordonneeL[0]), new String[]{"NbMobile", "UR"});
		
		System.out.println("DONE");
	}
}
