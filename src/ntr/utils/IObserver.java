package ntr.utils;

/**
 * Interface for IObserver job how want  followe a ISubject
 * @author Kevin
 */
public interface IObserver {
	/**
	 * Method called by ISubject after registration on Subject update state
	 * @param event
	 * @param args
	 */
	public void onEvent(EEvent event, Object[] args);
}
