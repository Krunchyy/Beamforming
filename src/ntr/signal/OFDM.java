package ntr.signal;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ntr.model.Agent;

public class OFDM {
	public final Agent _agent;
	public final int _nb_sub_carrier;
	public final int _nb_time_slot;
	
	public Packet[][] _ofdm;
	public int _currentIndex = 0;
	
	public long delayBetweenTimeSlot = 5;//in MILLISECONDS
	
	public ScheduledExecutorService ofdmScheduledExecutorService;
	public OFDM(int nb_sub_carrier, int nb_time_slot, Agent agent)
	{
		_nb_sub_carrier = nb_sub_carrier;
		_nb_time_slot = nb_time_slot;
		_ofdm = new Packet[nb_time_slot][nb_sub_carrier];
		_agent = agent;
	}
	
	/***********************
	 * THREAD SETTING
	 ***********************/
	
	public void startOFDM()
	{
		ScheduledExecutorService ofdmScheduledExecutorService = Executors.newScheduledThreadPool(5);
		ofdmScheduledExecutorService.schedule(() -> tick(),delayBetweenTimeSlot,TimeUnit.MILLISECONDS);
	}
	
	public void stopOFDM()
	{
		//termine les taches actuellement executer mais pas les autres
		ofdmScheduledExecutorService.shutdown();
	}
	
	/***********************
	 * THREAD TASK
	 ***********************/
	
	public void tick()
	{
		//Remplie l'ofdm (une ligne ou tout)
		
		//send une colonne de time slot
		Packet[] packets = getNextTimeSlot();
		for(int subCar = 0 ; subCar < _nb_sub_carrier ; subCar++)
		{
			_agent.sendPacket(packets[subCar], subCar);
		}
	}
	
	
	
	/***********************
	 * GETTER / SETTER
	 ***********************/
	
	
	
	
	public Packet[][] getTimeSlots()
	{
		return _ofdm;
	}
	
	/**
	 * Called for send Packets
	 * @param slot
	 * @return
	 */
	public Packet[] getTimeSlot(int slot)
	{
		if(slot < 0 || slot >= _nb_time_slot)
		{
			System.out.println("error on ofdm");
			return null;
		}
		return _ofdm[slot];
	}
	
	/**
	 * Called for send Packets
	 * @param slot
	 * @return
	 */
	public Packet[] getNextTimeSlot()
	{
		Packet[] result = _ofdm[_currentIndex++];
		_currentIndex = _currentIndex%_nb_time_slot;
		return result;
	}
	
	public void setTimeSlots(Packet[][] ofdm)
	{
		_ofdm = ofdm;
	}
	
	public void setTimeSlot(int slot, Packet[] timeSlot)
	{
		if(slot < 0 || slot >= _nb_time_slot)
		{
			System.out.println("error on ofdm");
			return;
		}
		_ofdm[slot] = timeSlot;
	}
}
