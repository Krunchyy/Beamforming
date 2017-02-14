package ntr.signal;

import ntr.model.Model;

public class Packet {
	public int _modulation;
	public final Model _sender;
	public final Model _target;
	public final String _data;
	public long _freq;
	public boolean _isValid;
	//TODO data
	
	public Packet(Model sender, Model target, String data)
	{
		_sender = sender;
		_target = target;
		_data = data;
	}
}
