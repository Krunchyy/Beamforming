package ntr.simulation;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.model.BeamFormingMaxSNR;
import ntr.model.Location;
import ntr.model.MaxSNR;
import ntr.model.Mobile;
import ntr.signal.Packet;
import ntr.utils.BuildCSV;
import ntr.utils.Config;

public class BuildCSVPingBeam {

	public static boolean Beam = false;
	public static Environement _env;
	public static Mobile mob;
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
		new Agent(new Location(1,1), _env);	
		if(Beam){
			new Agent(new Location(9,9), _env);
		}
		
		_env._mainAgent.get(0).setOrdonnanceur(new MaxSNR(_env._mainAgent.get(0).map ,_env._mainAgent.get(0)._ofdm));
		
		if(Beam){
			_env._mainAgent.get(1).setOrdonnanceur(new BeamFormingMaxSNR(_env._mainAgent.get(1).map ,_env._mainAgent.get(1)._ofdm));
		}
		
		mob = new Mobile(new Location(5,5), _env);
		_env._mainAgent.get(0).requestConnecte(mob);
		if(Beam)
			_env._mainAgent.get(1).requestConnecte(mob);
		
		startSimulation(Beam ? "pingBeam": "pingNoBeam");
		
	}
	
	public static final int _maxCharge = 20;
	public static final int _nbODFMTrameByRoll = 100;
	/**
	 * Chare 
	 * 0 -> 0-200	packets
	 * 1 -> 50-250	packets
	 * 2 -> 100-300	packets
	 * ..
	 * ..
	 * ..
	 */
	public static void startSimulation(String fileName){
		System.out.println("Start Simulation : "+fileName);
		long[] result = new long[_maxCharge];
		long[] bufferr = new long[_maxCharge];
		for(int charge = 0; charge < _maxCharge ; charge++)
		{
			System.out.println("["+charge + "/"+ _maxCharge+"]");
			
			long debit = 0;
			int ping = 0;
			int buffer = 0;
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
			buffer = _env._mainAgent.get(0).getMap().get(mob).size();
			_env._mainAgent.get(0).generator.totals.clear();
			System.out.println("Ping : "+ ping + " debit : "+ debit);
			
			result[charge] = ping;
			bufferr[charge] = buffer;
			Config.MAX_AVERAGE += 1;
			Config.MIN_AVERAGE += 1;
			

			//_env._mainAgent.get(1).requestConnecte(mob);
			
		}
		
		BuildCSV.buildCSV(fileName, result, new String[]{"Charge", "Ping"});
		BuildCSV.buildCSV(fileName+"Buffer", bufferr, new String[]{"Charge", "Buffer"});
		
		System.out.println("DONE");
	}
}
