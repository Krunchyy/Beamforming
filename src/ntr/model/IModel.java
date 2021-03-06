package ntr.model;

import ntr.environement.Environement;
import ntr.signal.Signal;

public interface IModel {
	public char getTag();
	public Location getLocation();
	public Environement getEnvironement();
	public void setEnv(Environement env);
	
	public void getSignalTo(IModel sender, Signal signal);
	public Signal getReceivedSignal();
	
	public boolean isSendingSignal();
	public void setSendingSignal(boolean sendSignal);

	public Signal getSignalInProgress();
	public void setSignalInProgress(Signal signalInProgress);
	
	public void sendSignalTo(IModel receiver);
	public void tick();
	public void tickFRC();
	
	/**
	 * 
	 * @param value always between 0 and 100
	 */
	public void setNetworkCondition(int value);
	
	public int getNetworkCondition();
	
	public boolean isMobile();
}
