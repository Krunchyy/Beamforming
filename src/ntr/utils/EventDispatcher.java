package ntr.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @author Roche Kevin
 */
public class EventDispatcher implements ISubject{
	
	//Must be Private (unlock for JUnit test)
	public final ConcurrentMap<EEvent,CopyOnWriteArraySet<IObserver>> _registered = new ConcurrentHashMap<EEvent, CopyOnWriteArraySet<IObserver>>();
	

	public EventDispatcher(){
		//initialise la list des inscrits
		for(EEvent e : EEvent.values())
		{
			_registered.put(e, new CopyOnWriteArraySet<>());
		}
	}
	
	
	@Override
	public void registerForEvent(EEvent event, IObserver eObserver)
	{
		if(event == null){return;}
		
		CopyOnWriteArraySet<IObserver> registeredForEvent = _registered.get(event);
		registeredForEvent.add(eObserver);
	}
	
	@Override
	public void unregisterForEvent(EEvent event, IObserver eObserver)
	{
		if(event == null){return;}
		
		CopyOnWriteArraySet<IObserver> registeredForEvent = _registered.get(event);
		registeredForEvent.remove(eObserver);
	}
	
	@Override
	public void onEvent(EEvent event, Object[] args, EventBroadCaster eBroadCaster)
	{
	
		if(event == null){return;}
		//System.out.println("[DEBUG] onEvent : "+ event.name() );
		CopyOnWriteArraySet<IObserver> registeredForEvent = _registered.get(event);
		for(IObserver eventObserver : registeredForEvent)
		{
			if(eventObserver == null)
			{
				continue;
			}
			eventObserver.onEvent(event, args);
		}
	}
	
}
