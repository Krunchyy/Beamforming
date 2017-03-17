package ntr.signal;

import java.util.ArrayList;
import java.util.List;

import ntr.model.Agent;
import ntr.model.Mobile;
import ntr.utils.Config;

public class Packet {
	public Mobile _receiver;
	public Agent _sender;
	
	private long dateCreation, dateArrivee;
	private int sizeSend, size;
	private List<PacketFragment> fragments;
	
	public Packet(Agent sender, Mobile receiver, long dateCreation) {
		this._receiver = receiver;
		this._sender = sender;
		this.dateCreation = dateCreation;
		this.size = Config.SIZE;
		this.sizeSend = 0;
		this.fragments = new ArrayList<>();
	}
	
	public void addFragment(PacketFragment fragment) {
		if (this.sizeSend >= this.size)
			return;
		
		this.fragments.add(fragment);
	}
	
	public int getSize() {
		return this.size;
	}
	
	public long getDateCreation() {
		return dateCreation;
	}

	public long getDateArrivee() {
		return dateArrivee;
	}

	public void setDateArrivee(long dateArrivee) {
		this.dateArrivee = dateArrivee;
	}

	public int getSizeSend() {
		return sizeSend;
	}

	public void setSizeSend(int sizeSend) {
		this.sizeSend = sizeSend;
	}
}
