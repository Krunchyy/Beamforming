package ntr.utils;

/**
 * Interface for ISubject job how want be followed by IObserver
 * @author Kevin
 */
public interface ISubject {
	/**
	 * Method for register an {@code eObserver} to a specify event
	 * @param event for registration
	 * @param eObserver
	 */
	public void registerForEvent(EEvent event, IObserver eObserver);
	/**
	 * Method for unregister an {@code eObserver} to a specify event
	 * @param event for unregistration
	 * @param eObserver
	 */
	public void unregisterForEvent(EEvent event, IObserver eObserver);
	/**
	 * Method for update the current state of Subjet and call method onEvent() on all registered IObserver for this event
	 * @param event
	 * @param eObserver
	 */
	public void onEvent(EEvent event, Object[] args, EventBroadCaster eBroadCaster);
}
