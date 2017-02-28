package ntr.signal;

import java.util.concurrent.ScheduledExecutorService;

import ntr.model.Agent;

public class OFDM {
	public final Agent _agent;
	public final int _nb_sub_carrier;
	public final int _nb_time_slot;
	
	public Packet[][] _ofdm;
	public int _currentIndex = 0;
	

	
	public ScheduledExecutorService ofdmScheduledExecutorService;
	public OFDM(int nb_sub_carrier, int nb_time_slot, Agent agent)
	{
		_nb_sub_carrier = nb_sub_carrier;
		_nb_time_slot = nb_time_slot;
		_ofdm = new Packet[nb_time_slot][nb_sub_carrier];
		_agent = agent;
	}
	
	/***********************
	 * THREAD TASK
	 ***********************/
	
	public void tick()
	{
		//Remplie l'ofdm (une ligne ou tout)
		
		//send une colonne de time slot
		Packet[] packets = getNextTimeSlot();
		if(packets == null)
		{
			System.out.println("[ERROR:OFDM] nextTimeSlot invalid : null");
			return;
		}
		if(packets.length < _nb_sub_carrier)
		{
			System.out.println("[ERROR:OFDM] nextTimeSlot invalid : bad length "+ packets.length);
			return;
		}
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
	private Packet[] getNextTimeSlot()
	{
		Packet[] result = _ofdm[_currentIndex];
		System.out.println("[INFO:OFDM] OFDM at index : "+ _currentIndex);
		_ofdm[_currentIndex] = new Packet[_nb_sub_carrier];//vide la ligne
		
		_currentIndex++;
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
		if(timeSlot.length !=  _nb_sub_carrier)
		{
			System.out.println("[ERROR] OFDM try to give bad timeSlot size : "+ timeSlot.length + " expected : "+ _nb_sub_carrier);
			return;
		}
		_ofdm[slot] = timeSlot;
	}
}
