package ntr.signal;

import java.util.concurrent.ScheduledExecutorService;

import ntr.model.Agent;
import ntr.utils.Config;

public class OFDM {
	public final Agent _agent;
	public final int _nb_sub_carrier;
	public final int _nb_time_slot;
	
	public PacketFragment[][] _ofdm;
	public int _currentIndex = 0;
	

	
	public ScheduledExecutorService ofdmScheduledExecutorService;
	public OFDM(int nb_sub_carrier, int nb_time_slot, Agent agent)
	{
		_nb_sub_carrier = nb_sub_carrier;
		_nb_time_slot = nb_time_slot;
		_ofdm = new PacketFragment[nb_time_slot][nb_sub_carrier];
		_agent = agent;
	}
	
	/***********************
	 * THREAD TASK
	 ***********************/
	
	public void tick()
	{
		//Remplie l'ofdm (une ligne ou tout)
		
		//send une colonne de time slot
		PacketFragment[] packetsFrag = getNextTimeSlot();
		if(packetsFrag == null)
		{
			System.err.println("[ERROR:OFDM] nextTimeSlot invalid : null");
			return;
		}
		if(packetsFrag.length < _nb_sub_carrier)
		{
			System.err.println("[ERROR:OFDM] nextTimeSlot invalid : bad length "+ packetsFrag.length);
			return;
		}
		for(int subCar = 0 ; subCar < _nb_sub_carrier ; subCar++)
		{
			if(Config.OFDM_DEBUG)
				System.out.println("ofd");
			if(packetsFrag[subCar] != null && packetsFrag[subCar].sended(_agent.getEnvironement().getCurrentTick()))
			{
				if(Config.OFDM_DEBUG)
					System.out.println("send");
				_agent.sendPacket(packetsFrag[subCar].parent);
			}
		}
	}
	
	
	
	/***********************
	 * GETTER / SETTER
	 ***********************/
	
	
	
	
	public PacketFragment[][] getTimeSlots()
	{
		return _ofdm;
	}
	
	/**
	 * Called for send Packets
	 * @param slot
	 * @return
	 */
	public PacketFragment[] getTimeSlot(int slot)
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
	private PacketFragment[] getNextTimeSlot()
	{
		PacketFragment[] result = _ofdm[_currentIndex];
		
		if(Config.OFDM_DEBUG)System.out.println("[INFO:OFDM] OFDM at index : "+ _currentIndex);
		
		//_ofdm[_currentIndex] = new PacketFragment[_nb_sub_carrier];//vide la ligne
		
		_currentIndex++;
		_currentIndex = _currentIndex%_nb_time_slot;
		return result;
	}
	
	
	public void setTimeSlots(PacketFragment[][] ofdm)
	{
		_ofdm = ofdm;
	}
	
	public void setTimeSlot(int slot, PacketFragment[] timeSlot)
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
