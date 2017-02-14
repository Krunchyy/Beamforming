package ntr.model;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.signal.OFDM;
import ntr.signal.Packet;
import ntr.signal.Signal;

public class Agent extends Model{
	
	private AbstractOrdonnanceur ordonnanceur;
	private final ConcurrentHashMap<IModel, Queue<Packet>> map;
	private final PacketGenerator generator;
	private final OFDM _ofdm;
	
	public Agent(Location loc, Environement env)
	{
		super(loc, env);
		map = new ConcurrentHashMap<>();
		
		generator = new PacketGenerator(this);
		//TODO: utilisation de la Queue (utiliser la méthode add -> lève une exception quand la Queue est pleine)
		_ofdm = new OFDM(10,10, this);
		
		//demmard l'ofdm
		//_ofdm.startOFDM();
		
		
	}
	
	
	@Override
	public void tick()
	{
		generator.tick();
		ordonnanceur.tick();
		_ofdm.tick();
	}
	
	@Override
	public char getTag() {
		return 'A';
	}
	
	public void requestConnecte(IModel model)
	{
		map.put(model, new ArrayBlockingQueue<>(QUEUE_SIZE));
	}
	
	public void deconnecteConnecte(IModel model)
	{
		map.put(model, new ArrayBlockingQueue<>(QUEUE_SIZE));
	}
	//called by OFDM schedule who call tick() method
	public void sendPacket(Packet paket, int sub_carrier_id)
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
	public Signal buildSignal(Packet paket, int sub_carrier_id)
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
	
	
	public char[] getModulation(Packet paket, long frenqUsed)
	{
		//paket._target
		//paket._data
		//frenqUsed
		return new char[]{'a', 'b'};//TODO
	}
	
	public int[] getDataFromModulation(Packet paket, char[] symbol)
	{
		//paket._data
		//symbol
		return new int[]{0,0,1};//TODO
	}
}
