package ntr.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.BeamSubCarrier;
import ntr.signal.OFDM;
import ntr.signal.Packet;
import ntr.signal.PacketFragment;

public class BeamFormingMaxSNR extends AbstractOrdonnanceur {

	public BeamFormingMaxSNR(ConcurrentHashMap<Mobile, Queue<Packet>> map, OFDM ofdm) {
		super(map, ofdm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
}
