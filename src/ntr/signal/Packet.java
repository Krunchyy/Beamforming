package ntr.signal;

import java.util.ArrayList;
import java.util.List;

import ntr.utils.Config;

public class Packet {
	private int dateCreation, dateArrivee;
	private int sizeSend, size;
	private List<PacketFragment> fragments;
	
	public Packet(int dateCreation) {
		this.dateCreation = dateCreation;
		this.size = Config.SIZE;
		this.sizeSend = 0;
		this.fragments = new ArrayList<>();
	}
	
	public void addFragment(PacketFragment fragment) {
		fragment.
		this.fragments.add(fragment);
	}
	
}
