package ntr.signal;

import ntr.model.Model;

public class PacketFragment {
			
	public int _modulation;
	public final Model _sender;
	public final Model _target;
	public final String _data;
	public long _freq;
	public boolean _isValid;
	public long _dateCreation;
	public long _dateExpedition;
	//taille des données envoyées dans ce fragment (= au SNR pour la target sur la subcarrier et le timeslot qu'il faut)
	public int _dataAvailableSize;
	//TODO data
	public int modulation;
	
	public PacketFragment(Model sender, Model target, String data, long date)
	{
		_sender = sender;
		_target = target;
		_data = data;
		_dateCreation = date;
		_dataAvailableSize = -1;
	}
	
	public void setDataAvailableSize(int size) {
		this._dataAvailableSize = size;
	}
}
