package ntr.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.signal.Packet;


public class RoundRobin extends AbstractOrdonnanceur {

	public RoundRobin(ConcurrentHashMap<IModel, Queue<Packet>> map) {
		super(map);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	@Override
	public IModel choose() {
		// TODO Auto-generated method stub
		return null;
	}

}
