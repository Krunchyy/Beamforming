package ntr.model;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.utils.Config;
import ntr.utils.RandomUtils;

public class Mobile extends Model{
	private char _tag = Config.MOBILE_TAG;
	
	private int networkCondition;
	
	private ConcurrentHashMap<IModel, ConcurrentHashMap<Integer, ArrayList<Double>>> _mknMap;
		
	private int currentTimeSlot;
	
	public Mobile(Location loc, Environement env)
	{
		super(loc, env);
		this.networkCondition = 0;
		this.currentTimeSlot = 0;
	}	
	
	@Override
	public char getTag() {
		return _tag;
	}
	
	
	public void setTag(char tag) {
		_tag = tag;
	}
	
	
	@Override
	public boolean isMobile()
	{
		return true;
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
	
	/**
	 * Return the sub_carrier of a timeslot of the mobile connected to an agent
	 * @param agent
	 * @param sub_carrier
	 * @param timeslot between 0 and 9
	 * @return
	 */
	public double getSNR(Agent agent, int sub_carrier, int timeslot) {
		return _mknMap.get(agent).get(timeslot%agent._ofdm._nb_time_slot).get(sub_carrier);
	}
	
	/**
	 * Compute mkn pour every subcarrier of the current timeslot
	 * @param agent
	 */
	public void computeSNR(Agent agent) {
		currentTimeSlot = currentTimeSlot%agent._ofdm._nb_time_slot; // currentTimeSlot between 0 and nb_sub_carrier excluded
		int nb_sub_carrier = agent._ofdm._nb_sub_carrier;
		ArrayList<Double> list = new ArrayList<>();
		
		for(int i=0; i<nb_sub_carrier; i++){
			double a = 1 + (3 * agent._diffusPower * RandomUtils.multitrajet() * Math.pow(2,(1/ RandomUtils.setDelta(agent, this))));
			double mkn = Math.log10(a) / Math.log10(2); // log2( a )
			list.add(mkn);
		}
		_mknMap.get(agent).put(currentTimeSlot, list);

		currentTimeSlot++;
		/*
		 * dist: Le Mkn prend en compte l'éloignement par rapport à l'antenne,
		 * noise: l'affaiblissement lie aux multi-trajets
		 * puis: et la puissance (max) de transmission.
		 *
		 * mkn = log2( 1 + (3*puis*noisekn*(1/dist)²) )
		 *
		 * Dans le calcul du mkn, on ne prend pas en compte:
		 * la densité spectrale du bruit, le taux d'erreur par bit.
		 */
	}
}
