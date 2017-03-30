package ntr.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.Location;
import ntr.model.MaxSNR;
import ntr.model.Mobile;
import ntr.signal.Packet;
import ntr.utils.BuildCSV;
import ntr.utils.Config;

public class BuildCSVPingByCharge {

	public static final boolean SNR = true;
	
	public static Environement _env;
	public static final ArrayList<Mobile> _mobiles = new  ArrayList<>();
	
	public static final int _nbMobile = 5;
	
	public static HashMap<Mobile, Double> _mod = new HashMap<Mobile, Double>();
	
	public static void main(String[] args)
	{
		Config.SIZE = 5;
		Config.MAX_AVERAGE = 2;
		Config.MIN_AVERAGE = 2;
		Config.MAX_OFFSET = 2;
		Config.MIN_OFFSET = -2;
		Config.OFDM_NB_SUB_CARRIER = 100;
		Config.OFDM_NB_TIME_SLOT = 10;
		
		_env = new Environement(Config.ENVIRONEMENT_SIZE);
		new Agent(new Location(3,1), _env);
		
		for(int i = 0 ; i < _nbMobile ; i++)
		{
			Mobile mob = new Mobile(new Location(10,10), _env);
			_mobiles.add(mob);
			_env._mainAgent.get(0).requestConnecte(mob);
			_mod.put(mob, 1.0);
		}
		
		if(SNR)
			_env._mainAgent.get(0).setOrdonnanceur(new MaxSNR(_env._mainAgent.get(0).map ,_env._mainAgent.get(0)._ofdm));

		startSimulation(SNR ? "pingByChargeMaxSNRPacket": "pingByChargeRR");
		
	}
	
	public static final int _nbODFMTrameByRoll = 100;
	/**
	 * Do N roll with 
	 * 1 ,2 ... N mobile,
	 * and logs systems debit
	 */
	public static void startSimulation(String fileName){
		System.out.println("Start Simulation : "+fileName);
		ArrayList<CoordonneeL> result = new ArrayList<CoordonneeL>();
		
		for(double mod = 0; mod < 5.0 ; mod += 0.1)
		{
			System.out.println("["+mod + "/"+ (5.0)+"]");
			long debit = 0;
			long ping = 0;
			_mod = new HashMap<Mobile, Double>();
			for(Mobile m : _mobiles)
			{
				_mod.put(m, mod);
			}
			
			_env._mainAgent.get(0).generator._modifier = _mod;
			for(int nbRoll = 0 ; nbRoll < _nbODFMTrameByRoll ; nbRoll++)
			{
				try
				{
					_env.tick(Config.OFDM_NB_TIME_SLOT);
					for(Packet pack : _env.getEnvBuffer())
					{
						debit++;
						ping += pack.getDateArrivee() - pack.getDateCreation();
					}
					_env.getEnvBuffer().clear();
					_env._mainAgent.get(0).generator.totals.clear();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			if(debit != 0)
			{
				ping /= debit;
			}else{
				ping = 0;
			}
			result.add(new CoordonneeL(debit/100L, ping));
		}
		
		BuildCSV.buildCSV(fileName, result.toArray(new CoordonneeL[0]), new String[]{"Charge", "Ping"});
		
		System.out.println("DONE");
	}
}
