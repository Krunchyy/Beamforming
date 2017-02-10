package ntr.environement;

import java.util.ArrayList;
import java.util.List;

import ntr.environement.pertubation.MultiPathFinding;
import ntr.environement.pertubation.PathLost;
import ntr.environement.pertubation.Shadowing;
import ntr.model.IModel;
import ntr.model.Location;
import ntr.signal.Alteration;

public class Environement {
	
	private Alteration[] _alteration = {
			new MultiPathFinding(),
			new PathLost(),
			new Shadowing()
	};
	
	public final int _size;
	private final List<IModel> _elements = new ArrayList<>();

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
	
	public void sendSignal(IModel sender, IModel receiver)
	{
		//TODO impl sending delay
		
		for(Alteration alt : _alteration)
		{
			//alterate the signal
			alt.alterate(sender, this);
		}
	
		receiver.getSignalTo(sender, sender.getSignalInProgress());
		//TODO impl sending delay
	}
}
