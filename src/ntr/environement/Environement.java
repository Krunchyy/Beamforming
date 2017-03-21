package ntr.environement;

import java.util.ArrayList;
import java.util.List;

import ntr.environement.pertubation.MultiPathFading;
import ntr.environement.pertubation.PathLoss;
import ntr.environement.pertubation.Shadowing;
import ntr.model.Agent;
import ntr.model.IModel;
import ntr.model.Location;
import ntr.signal.Alteration;
import ntr.signal.Packet;

public class Environement {
	public boolean _stopAuto = false;
	public long _currentTick = 0;
	public Agent _mainAgent;
	
	public long getCurrentTick()
	{
		return _currentTick;
	}
	
	private Alteration[] _alteration = {
			new PathLoss(),
			new Shadowing(),
			new MultiPathFading(),
	};
	
	public final int _size;
	private final List<IModel> _elements = new ArrayList<>();
	private final List<Packet> _buff = new ArrayList<>();
	
	public Environement(int size)
	{
		_size = size;
	}
	
	/**********************************
	 * MODEL Add / Remove
	 **********************************/
	
	/**
	 * 
	 * @param model
	 */
	public void addModel(IModel model){
		Location loc = model.getLocation();
		if(loc == null)
		{
			//Warn modil no more on environement
			return;
		}
		
		if(loc._x >= _size || loc._y >= _size)
		{
			//WARN bad location
			return;
		}
		if(model instanceof Agent)
		{
			_mainAgent = (Agent) model;
		}
		_elements.add(model);
	}
	
	public void removeModel(IModel model){
		_elements.add(model);
		
		Location loc = model.getLocation();
		if(loc == null)
		{
			//Warn modil no more on environement
			return;
		}
		
		if(loc._x >= _size || loc._y >= _size)
		{
			//WARN bad location
			return;
		}
		
		_elements.remove(model);
	}
	
	public List<IModel> getElements()
	{
		return _elements;
	}
	
	/**********************************
	 * Signal call
	 **********************************/
	
	public void sendPacket(Packet packet)
	{
		_buff.add(packet);
	}
	public List<Packet> getEnvBuffer()
	{
		return _buff;
	}
	
	public void pushPacket()
	{
		for(Alteration alt : _alteration)
		{
			//alterate the signal
			alt.alterate(this._elements, this);
		}
		
		
		for(Packet packet : _buff)
			packet._receiver.receivePacket(packet);
		
		//clear for next wave
		//_buff.clear();
	}
	
	@Deprecated
	public void sendSignal(IModel sender, IModel receiver)
	{
		/*
		for(Alteration alt : _alteration)
		{
			//alterate the signal
			alt.alterate(this._elements, this);
		}
		
		Alteration.performAlteration(_buff, _elements);
		
		receiver.getSignalTo(sender, sender.getSignalInProgress());
		//TODO impl sending delay
		 * */
	}
	
	
	public void tick()
	{
		pushPacket();//do alteration on environement
		
		//System.out.println("buff : "+_mainAgent.map.size());
		//tick all elements
		for(IModel elements : _elements)
		{
			elements.tick();
			//System.out.println("buff elements : "+ elements +" buff "+_mainAgent.map.size());
		}
		_currentTick++;
	}
	
	public void tick(int nbTick)
	{
		for(int i = 0 ; i < nbTick ; i++)
		{
			tick();
		}
	}
	
	public void tick(int nbTick, boolean auto)
	{
		if(!auto || !_stopAuto)
		{
			tick(nbTick);
		}
	}
}
