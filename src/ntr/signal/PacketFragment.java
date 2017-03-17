package ntr.signal;

import ntr.model.Model;

public class PacketFragment {
			
	public Packet parent;
	//taille des données envoyées dans ce fragment (= au SNR pour la target sur la subcarrier et le timeslot qu'il faut)
	public int _mkn, _dataSize;
	
	public PacketFragment(Packet parent)
	{
		_mkn = -1;
		_dataSize = -1;
	}
	
	public void setMkn(int mkn) {
		this._mkn = mkn;
	}
	
	public void setDataSize(int dataSize) {
		this._dataSize = dataSize;			
	}
}
