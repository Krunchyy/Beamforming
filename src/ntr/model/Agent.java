package ntr.model;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.signal.OFDM;
import ntr.signal.PacketFragment;
import ntr.signal.Signal;
import ntr.utils.Config;

public class Agent extends Model{
	private char _tag = Config.AGENT_TAG;
	
	private AbstractOrdonnanceur _ordonnanceur;
	public final ConcurrentHashMap<IModel, Queue<PacketFragment>> map;
	public final PacketGenerator generator;
	public final OFDM _ofdm;
	public final int _diffusPower = 10;
	public Agent(Location loc, Environement env)
	{
		super(loc, env);
		map = new ConcurrentHashMap<>();
		
		generator = new PacketGenerator(this);
		//TODO: utilisation de la Queue (utiliser la méthode add -> lève une exception quand la Queue est pleine)
		_ofdm = new OFDM(Config.OFDM_NB_SUB_CARRIER,Config.OFDM_NB_TIME_SLOT, this);
		
		//demmard l'ofdm
		//_ofdm.startOFDM();
		_ordonnanceur = new RoundRobin(map , _ofdm);
	}
	
	
	public int nextSchedul = 0;
	@Override
	public void tick()
	{
		generator.tick();
		if(nextSchedul <= 0)
		{
			_ordonnanceur.tick();
			nextSchedul = _ofdm._nb_time_slot;
		}
		nextSchedul--;
		_ofdm.tick();
	}
	
	@Override
	public char getTag() {
		return _tag;
	}
	
	public void setOrdonnanceur(AbstractOrdonnanceur ordonnanceur){
		_ordonnanceur = ordonnanceur;
	}
	
	public void setTag(char tag) {
		_tag = tag;
	}
	
	
	public void requestConnecte(IModel model)
	{
		map.put(model, new ArrayBlockingQueue<PacketFragment>(Config.BUFFER_SIZE));
	}
	
	public void deconnecteConnecte(IModel model)
	{
		map.put(model, new ArrayBlockingQueue<PacketFragment>(Config.BUFFER_SIZE));
	}
	//called by OFDM schedule who call tick() method
	public void sendPacket(PacketFragment paket, int sub_carrier_id)
	{
		if(paket == null)
				return;
		//Signal signal = buildSignal(paket, sub_carrier_id);
		
		//prepare le Model a envoyer un message
		//setSignalInProgress(signal);
		super.sendPacket(paket);//envoi le message (call environement)
	}
	
	/**************************************
	 * Build Signal Method
	 *************************************/
	
	/**
	 * @param paket
	 * @param sub_carrier_id
	 * @return
	 */
	public Signal buildSignal(PacketFragment paket, int sub_carrier_id)
	{
		//creation du signal
		long frenqUsed = getFrenqByCarrierId(sub_carrier_id);
		char[] symbol = getModulation(paket, frenqUsed);
		int[] data = getDataFromModulation(paket, symbol);
		return new Signal(frenqUsed, symbol, data);
	}
	
	/**
	 * 
	 * @param sub_carrier_id
	 * @return
	 */
	public long getFrenqByCarrierId(int sub_carrier_id)
	{
		return 10L;//TODO
	}
	
	
	public char[] getModulation(PacketFragment paket, long frenqUsed)
	{
		//paket._target
		//paket._data
		//frenqUsed
		return new char[]{'a', 'b'};//TODO
	}
	
	public int[] getDataFromModulation(PacketFragment paket, char[] symbol)
	{
		//paket._data
		//symbol
		return new int[]{0,0,1};//TODO
	}
	
	public ConcurrentHashMap<IModel, Queue<PacketFragment>> getMap() {
		return this.map;
	}
	
	public PacketGenerator getPacketGen() {
		return this.generator;
	}
	
	/********************
	 * DISPLAY
	 ****************/

	public HashMap<IModel, Integer> _diff = new HashMap<>();
	public String displayBuffer(int size)
	{
		String result = "";
		for(IModel model : getMap().keySet())
		{
			int oldQueueSize = 0;
			
			if(_diff.containsKey(model))
			{
				oldQueueSize = _diff.get(model);
			}
			double pourcentageUtilisation = (getMap().get(model).size() /  (double) Config.BUFFER_SIZE );
			int bufferDisplaySize = (int)(pourcentageUtilisation*size);
			result += "Name : "+ model.getTag() +" Size : "+ getMap().get(model).size() + "\n";
			result +="[" + getNbChar(bufferDisplaySize, '|') + getNbChar(size-bufferDisplaySize, ' ' ) + "]\n";
			_diff.remove(model);
			_diff.put(model, getMap().get(model).size());
		}
		return result;
	}
	public static String getNbChar(int nb, char c)
	{
		String result = "";
		for(int i = nb ; i > 0; i--)
		{
			result += c;
		}
		
		return result;
	}


	@Override
	public void setNetworkCondition(int value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getNetworkCondition() {
		// TODO Auto-generated method stub
		return 0;
	}
}
