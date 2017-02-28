package ntr.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ntr.environement.Environement;
import ntr.utils.Config;

public class Mobile extends Model{
	private int networkCondition;
	
	public Mobile(Location loc, Environement env)
	{
		super(loc, env);
		this.networkCondition = 0;
	}
	
	protected final static synchronized Map<Integer, ?> truc(final int i)//TODO: C'est quoi çà ?
	{
		return new HashMap<Integer, ArrayList<IModel>>();
	}
	
	@Override
	public char getTag() {
		return Config.MOBILE_TAG;
	}

	@Override
	public void setNetworkCondition(int value) {
		if(value > 100) value = 100;
		if(value < 100) value = 0;
		this.networkCondition = value;
	}

	@Override
	public int getNetworkCondition() {
		return this.networkCondition;
	}
	
	public int _packetFlow = -1;
	public int _packetFlowDelay = 0;
	
	public void setPacketFlow(int packetCount, int expireDelay)
	{
		_packetFlow = packetCount;
		_packetFlowDelay = expireDelay;
	}
	
	public int getPacketFlow()
	{
		if(_packetFlowDelay-- <= 0)
		{
			return -1;
		}
		return _packetFlow;
	}
}
