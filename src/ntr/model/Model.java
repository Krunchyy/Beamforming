package ntr.model;

import ntr.environement.Environement;
import ntr.signal.Signal;

public abstract class Model implements IModel{
	public Location _loc;
	public Environement _env;
	private boolean _sendSignal = false;
	
	private Signal _signalInProgress;//sending this -> environment
	private Signal _receivedSignal;//received this <- environment
	
	public Model(Location loc, Environement env)
	{
		_loc = loc;
		_env = env;
		if(_env != null)
		{
			_env.addModel(this);
		}
	}
	
	/****************
	 * Init variable
	 ***************/
	
	@Override
	public void setEnv(Environement env){
		if(_env != null)
		{
			_env.removeModel(this);
		}
		
		else if(env != null)
		{
			env.addModel(this);
		}
	}
	
	
	public char getTag() {
		return ' ';
	}
	

	public Location getLocation() {
		return _loc;
	}

	public Environement getEnvironement() {
		return _env;
	}

	/**************************************
	 * Sending this -> environment Method
	 **************************************/
	
	//sending or not
	public boolean isSendingSignal() {
		return _sendSignal;
	}
	//sending or not
	public void setSendingSignal(boolean sendSignal) {
		_sendSignal = sendSignal;
	}

	
	//signal who will be send to environment
	public Signal getSignalInProgress() {
		return _signalInProgress;
	}

	//signal who will be send to environment
	public void setSignalInProgress(Signal signalInProgress) {
		_signalInProgress = signalInProgress;
	}
	
	
	//validate sending of _signalInProgress
	/**
	 * this method will call the environment
	 * and ask him to alternate or not the signal
	 * and call getSignalTo method from receiver
	 * @param receiver
	 */
	public void sendSignalTo(IModel receiver)
	{
		if(_env == null)
		{
			System.out.println("Try to send a signal out of environement");
			return;
		}
		setSendingSignal(true);
		_env.sendSignal(this, receiver);
	}
	
	/**************************************
	 * Receiving this <- environement Method
	 **************************************/
	
	/**
	 * Called by Environment
	 * @param sender from you get a signal
	 * @param signal the signal altered or not by environment
	 */
	public void getSignalTo(IModel sender, Signal signal)
	{
		_receivedSignal = signal;
	}

	
	public Signal getReceivedSignal() {
		return _receivedSignal;
	}
}
