package ntr.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ntr.environement.Environement;
import ntr.signal.PacketFragment;
import ntr.utils.Config;

public class Mobile extends Model{
	private char _tag = Config.MOBILE_TAG;
	
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
	 * Il faut calculer le Mkn pour chaque subcarrier de la timeslot actuel.
	 * @param agent : l'agent pour lequel il faut calculer le Mkn
	 * @param subcarrier to compute mkn
	 * @return
	 */
	public void computeMkn(Agent agent, int subcarrier) {
		/*
		 * dist: Le Mkn prend en compte l'éloignement par rapport à l'antenne,
		 * noise: l'"affaiblissement lié aux multi-trajets
		 * puis: et la puissance (max) de transmission.
		 */
		
		/*
		 * mkn = log2( 1 + (3*puis*noisekn*(1/dist)²) )
		 */
		
		/*
		 * Dans le calcul du mkn, on ne prend pas en compte:
		 * la densité spectrale du bruit, le taux d'erreur par bit.
		 */
	}
}
