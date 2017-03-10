package ntr.model;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.utils.Config;
import ntr.utils.RandomUtils;

public class Mobile extends Model{
	private char _tag = Config.MOBILE_TAG;
	
	private int networkCondition;
	
	//agent, timeslot(10) , subcarrier(10)
	private ConcurrentHashMap<IModel, ConcurrentHashMap<Integer, ArrayList<Double>>> _mknMap = new ConcurrentHashMap<IModel, ConcurrentHashMap<Integer, ArrayList<Double>>>();;
		
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
		if(agent == null)
		{
			System.out.println("getSNR for mobile agent == null");
			return 0;
		}
		ConcurrentHashMap<Integer, ArrayList<Double>> mkn = _mknMap.get(agent);
		if(mkn == null)
		{
			mkn = new ConcurrentHashMap<Integer, ArrayList<Double>>();
			_mknMap.put(agent, mkn);
		}
		ArrayList<Double> mkna = mkn.get(timeslot%agent._ofdm._nb_time_slot);
		if(mkna == null)
		{
			System.out.println("mkna null");
			return 0;
		}
		return mkna.get(sub_carrier);
	}
	
	/**
	 * Compute mkn pour every subcarrier of the current timeslot
	 * @param agent
	 */
	public void computeSNR(Agent agent) {
		
		//getMkn rand.get(0, 10/d);
		
		currentTimeSlot = currentTimeSlot%agent._ofdm._nb_time_slot; // currentTimeSlot between 0 and nb_sub_carrier excluded
		int nb_sub_carrier = agent._ofdm._nb_sub_carrier;
		ArrayList<Double> list = new ArrayList<>();
		
		for(int i=0; i<nb_sub_carrier; i++){
			double multi = RandomUtils.multitrajet();
			double a = 1 + (3 * agent._diffusPower * multi * Math.pow(2,(1/ RandomUtils.setDelta(agent, this))));
			double mkn = Math.log10(a) / Math.log10(2); // log2( a )
			list.add(mkn);
		}
		
		if(_mknMap.get(agent) == null) {
			ConcurrentHashMap<Integer, ArrayList<Double>> value = new ConcurrentHashMap<Integer, ArrayList<Double>>();
			_mknMap.put(agent, value);
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
