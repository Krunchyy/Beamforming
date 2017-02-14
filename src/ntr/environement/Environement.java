package ntr.environement;

import java.util.ArrayList;
import java.util.List;

import ntr.environement.pertubation.MultiPathFinding;
import ntr.environement.pertubation.PathLost;
import ntr.environement.pertubation.Shadowing;
import ntr.model.IModel;
import ntr.model.Location;
import ntr.signal.Alteration;
import ntr.signal.Packet;

public class Environement {
	
	private Alteration[] _alteration = {
			new MultiPathFinding(),
			new PathLost(),
			new Shadowing()
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
	
	public void pushPacket()
	{
		for(Alteration alt : _alteration)
		{
			//alterate the signal
			alt.alterate(this);
		}
		for(Packet packet : _buff)
			packet._target.receivePacket(packet);
		
		//clear for next wave
		_buff.clear();
	}
	
	@Deprecated
	public void sendSignal(IModel sender, IModel receiver)
	{
		for(Alteration alt : _alteration)
		{
			//alterate the signal
			alt.alterate(sender, receiver, this);
		}
	
		receiver.getSignalTo(sender, sender.getSignalInProgress());
		//TODO impl sending delay
	}
	
	
	public void tick()
	{
		pushPacket();//do alteration on environement
		
		//tick all elements
		for(IModel elements : _elements)
		{
			elements.tick();
		}
	}
}
