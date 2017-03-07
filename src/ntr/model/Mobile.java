package ntr.model;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.utils.Config;
import ntr.utils.RandomUtils;

public class Mobile extends Model{
	private char _tag = Config.MOBILE_TAG;
	
	private int networkCondition;
	
	private ConcurrentHashMap<IModel, ArrayList<Double>> mkn_map;
	
	public Mobile(Location loc, Environement env)
	{
		super(loc, env);
		this.networkCondition = 0;
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
	
	public double getSNR(Agent agent, int sub_carrier){
		return mkn_map.get(agent).get(sub_carrier);
	}
	
	/**
	 * Il faut calculer le Mkn pour chaque subcarrier de la timeslot actuel.
	 * @param agent : l'agent pour lequel il faut calculer le Mkn
	 */
	public void computeSNR(Agent agent) {
		int nb_sub_carrier = agent._ofdm._nb_sub_carrier;
		ArrayList<Double> list = new ArrayList<>();
		
		for(int i=0; i<nb_sub_carrier; i++){
			double a = 1 + (3 * agent._diffusPower * RandomUtils.multitrajet() * Math.pow(2,(1/ RandomUtils.setDelta(agent, this))));
			double mkn = Math.log10(a) / Math.log10(2); // log2( 1 + (3*puis*noisekn*(1/dist)²) )
			list.add(mkn);
		}
		
		mkn_map.put(agent, list);
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
