package ntr.model;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.signal.OFDM;
import ntr.signal.Packet;
import ntr.signal.PacketFragment;
import ntr.signal.Signal;
import ntr.utils.Config;
import ntr.utils.Distance;

public class Agent extends Model{
	
	public ModulationCalculator moduleur;

	private char _tag = Config.AGENT_TAG;
	
	public AbstractOrdonnanceur _ordonnanceur;
	public final ConcurrentHashMap<IModel, Queue<Packet>> map;
	public final PacketGenerator generator;
	public final OFDM _ofdm;
	public final int _diffusPower = 10;
	public final FlowRateCalculator _frc;
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
		_frc = new FlowRateCalculator(this);
		moduleur = new ModulationCalculator(_ofdm);
	}
	
	
	public int nextSchedul = 0;
	@Override
	public void tick()
	{

		//System.out.println("tick buff elements : "+ this +" buff "+map.size());
		generator.tick();

		//System.out.println("generator buff elements : "+ this +" buff "+map.size());
		if(nextSchedul <= 0)
		{
			// appel� dans l'environnement
			//_frc.tick();

			//System.out.println("frc buff elements : "+ this +" buff "+map.size());
			_ordonnanceur.tick();

			//System.out.println("ordo buff elements : "+ this +" buff "+map.size());
			//moduleur.tick();
			nextSchedul = _ofdm._nb_time_slot;
		}
		nextSchedul--;
		_ofdm.tick();

		//System.out.println("ofdm buff elements : "+ this +" buff "+map.size());

	}
	
	@Override
	public void tickFRC()
	{		
		if(nextSchedul <= 0)
		{
		_frc.tick();
		}
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
		map.put(model, new ArrayBlockingQueue<Packet>(Config.BUFFER_SIZE));
	}
	
	public void deconnecteConnecte(IModel model)
	{
		map.put(model, new ArrayBlockingQueue<Packet>(Config.BUFFER_SIZE));
	}
	//called by OFDM schedule who call tick() method
	public void sendPacket(Packet paket)
	{
		if(paket == null)
				return;
		if(paket._isSended)
			return;
		paket._isSended = true;
		//Signal signal = buildSignal(paket, sub_carrier_id);
		
		//prepare le Model a envoyer un message
		//setSignalInProgress(signal);
		//System.out.println("vidage packet de : "+ paket._receiver.getTag()+ " nom : "+ paket);
		map.get(paket._receiver).remove(paket);
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
	
	public ConcurrentHashMap<IModel, Queue<Packet>> getMap() {
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

			double pourcentageUtilisation = (getMap().get(model).size() /  (double) Config.BUFFER_SIZE );
			int bufferDisplaySize = (int)(pourcentageUtilisation*size);
			result += "Name : "+ model.getTag() +" Size : "+ getMap().get(model).size() + " rang : "+ Distance.setDelta(this,  model)+"\n";
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
