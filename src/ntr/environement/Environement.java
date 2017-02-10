package ntr.environement;

import java.util.ArrayList;
import java.util.List;

import ntr.model.IModel;
import ntr.model.Location;

public class Environement {
	public final int _size;
	private final List<IModel> _elements = new ArrayList<>();
	
	public Environement(int size)
	{
		_size = size;
	}
	
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
}
