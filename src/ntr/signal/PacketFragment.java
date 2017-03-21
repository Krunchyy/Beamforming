package ntr.signal;

import ntr.utils.Config;

public class PacketFragment {
			
	public Packet parent;
	//taille des données envoyées dans ce fragment (= au SNR pour la target sur la subcarrier et le timeslot qu'il faut)
	public int _mkn, _dataSize;
	
	public PacketFragment(Packet parent)
	{
		this.parent = parent;
		_mkn = -1;
		_dataSize = -1;
	}
	
	public void setMkn(int mkn) {
		this._mkn = mkn;
	}
	
	public void addData() {
		
		this._dataSize = Math.min(this._mkn, this.parent.getRestToSend());
		
		if(this._dataSize == 0)
			System.err.println("[WARNING] add empty data to packet fragment:" + this + " with parent: " + this.parent);
	}
	
	
	public boolean sended(long currentTime){
		
		parent.setSizeSend(parent.getSizeSend()+ (_dataSize == -1 ? _mkn : _dataSize));
		if(Config.OFDM_DEBUG)
			System.out.println("p : "+ parent + " pf:"+ this + " "+parent.getSizeSend() + " / "+ parent.getSize()+ " Fragment Size "+ (_dataSize == -1 ? _mkn : _dataSize));
		if(parent.getSizeSend() >= parent.getSize())
		{
			parent.setDateArrivee(currentTime);
			if(Config.OFDM_DEBUG)
				System.out.println("end");
			return true;
		}
		if(Config.OFDM_DEBUG)
			System.out.println("down");
		return false;
	}
}
