package ntr.ui;

import ntr.environement.Environement;
import ntr.ui.interf.IGui;
import ntr.ui.jswing.Window;
import ntr.utils.EEvent;
import ntr.utils.ISubject;

/**
*@DesignPattern Observer : Observer
 * @author Roche Kevin
 */
public class JSwingGui implements IGui{
	private static Window frame;
	
	private final Environement _env;
	private final  ISubject _dispatcher;
	
	// IGui
	public JSwingGui(Environement env, ISubject dispatcher){_env = env;_dispatcher = dispatcher;}
	@Override
	public void Start() {
		if(frame == null)
			frame = new Window(this);
		
		_dispatcher.registerForEvent(EEvent.GUI_UPDATE, this);
	}
	//~ IGui
	
	// EventObserver
	@Override
	public void onEvent(EEvent event, Object[] args) {
		if(frame == null)
			return;
		
		switch(event)
		{
			case GUI_UPDATE : frame.updateRender();
		default:
			break;
		}
	}
	//~ EventObserver
	public Environement getEnvironement(){
		return _env;
	}
	
	public ISubject getDispatcher(){
		return _dispatcher;
	}
}
