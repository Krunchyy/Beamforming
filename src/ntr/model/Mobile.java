package ntr.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.signal.BeamMkn;
import ntr.signal.BeamSubCarriers;
import ntr.signal.Packet;
import ntr.utils.Config;
import ntr.utils.Distance;
import ntr.utils.RandomUtils;

public class Mobile extends Model {
	private char _tag = Config.MOBILE_TAG;
	private int _networkCondition;

	// Beamforming
	public ArrayList<IModel> _beamformingAgents = new ArrayList<>();
	private ArrayList<Integer> _beamformingBestSubCarriers = new ArrayList<>(); // Liste des meilleurs subcarriers par timeslot
	private int _nbAgentsConnected;
	private boolean _beamforming;
	
	
	
	/**
	 * WARNING !!! Important the HashMap key is a Long object 
	 * WARNING !!! when you want to access make sure to use map.get() 
	 * WARNING !!! with a Long Instance, not a long primitive or int
	 */
	private HashMap<Long, HashMap<Integer, Integer>> bestSubCarrierOnEachTimeslot;
	private BeamSubCarriersCalculator calculator;
	
	public Queue<Packet> _filePacketsBeam = new ArrayBlockingQueue<Packet>(Config.BUFFER_SIZE);

	private ConcurrentHashMap<Integer, ArrayList<BeamMkn>> _mapBeam = new ConcurrentHashMap<>();
	private ConcurrentHashMap<IModel, ConcurrentHashMap<Integer, BeamSubCarriers>> _mapBeamAgents = new ConcurrentHashMap<IModel, ConcurrentHashMap<Integer, BeamSubCarriers>>();

	//agent, timeslot(10) , subcarrier(10)
	private ConcurrentHashMap<IModel, ConcurrentHashMap<Integer, ArrayList<Double>>> _mknMap = new ConcurrentHashMap<IModel, ConcurrentHashMap<Integer, ArrayList<Double>>>();

	public Mobile(Location loc, Environement env) {
		super(loc, env);
		_networkCondition = 0;
		_nbAgentsConnected = 0;
		_beamforming = false;
		bestSubCarrierOnEachTimeslot = new HashMap<>();
		this.calculator = new BeamSubCarriersCalculator(env._mainAgent, this);
	}

	public synchronized boolean isBeamforming() {
		return _beamforming;
	}

	@Override
	public char getTag() {
		return _tag;
	}

	public void setTag(char tag) {
		_tag = tag;
	}


	@Override
	public boolean isMobile() {
		return true;
	}

	@Override
	public void setNetworkCondition(int value) {
		if(value > 100) value = 100;
		if(value < 100) value = 0;
		this._networkCondition = value;
	}

	@Override
	public int getNetworkCondition() {
		return this._networkCondition;
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
	 * Get the sub_carrier of a timeslot of the mobile connected to an agent
	 * @param agent
	 * @param sub_carrier
	 * @param timeslot
	 * @return the sub_carrier of a timeslot of the mobile connected to an agent
	 */
	public double getSNR(Agent agent, int sub_carrier, int timeslot) {
		if(agent == null) {
			System.out.println("getSNR for mobile agent == null");
			return 0;
		}

		ConcurrentHashMap<Integer, ArrayList<Double>> mapAgent = _mknMap.get(agent);
		if(mapAgent == null) {
			System.out.println("Requested Agent:" + agent.getTag() + ", subcarriers: "+  sub_carrier + ", timeslot: " + timeslot);
			System.out.println("getSNR for mobile agent pas dans la map");
			return 0;
		}

		ArrayList<Double> mapTimeslot = mapAgent.get(timeslot % agent._ofdm._nb_time_slot);
		if(mapTimeslot == null) {
			System.out.println("getSNR for mobile timeslot inexistant");
			return 0;
		}

		if(Config.DEBUG_FIX_MKN)
			return Config.DEBUG_FIX_MKN_VALUE;
		return mapTimeslot.get(sub_carrier);
	}

	/**
	 * Compute mkn for every subcarrier of every timeslots
	 * @param agent
	 */
	public void computeAllSNR(Agent agent) {
		int nb_sub_carrier = agent._ofdm._nb_sub_carrier;
		double distance = Distance.setDelta(agent, this);
		for(int i=0; i<agent._ofdm._nb_time_slot; i++) { // generate the timeslots
			ArrayList<Double> list = new ArrayList<>();

			for(int j=0; j<nb_sub_carrier; j++){ // generate mkn for every subcarrier
				double mkn = 2 + RandomUtils.get(1, 10)/distance; //TODO: am�liorer la valeur g�n�r�e surtout pour les mobiles �loign�es
				list.add(mkn);
			}

			if(_mknMap.get(agent) == null) {
				ConcurrentHashMap<Integer, ArrayList<Double>> value = new ConcurrentHashMap<Integer, ArrayList<Double>>();
				_mknMap.put(agent, value);
			}
			_mknMap.get(agent).put(i, list);
		}/*
		System.out.println("-----------");
		System.out.println("moyenne du SNR de "+this+"est de : "+averageSNR(agent));
		System.out.println("distance de "+this+" :"+distance);*/
	}

	/**
	 * Give the average value of all the mkn in the map (10 timeslots)
	 * @param agent
	 * @return the average value of mkn
	 */
	public double averageSNR(Agent agent) {
		double res = 0;
		int cmpt = 0;

		ConcurrentHashMap<Integer, ArrayList<Double>> map = _mknMap.get(agent);
		Set<Integer> keys = map.keySet();
		Iterator<Integer> it = keys.iterator();
		while (it.hasNext()) {
			int v = it.next();
			ArrayList<Double> list = map.get(v);
			Iterator<Double> it2 = list.iterator();
			while(it2.hasNext()) {
				res += it2.next();
				cmpt++;
			}
		}
		return res/cmpt;
	}

	public void computeBeamSubCarriers() {
		int connectedAgents = 0;
		for(int i = 0 ; i < this._env._mainAgent.size() ; i++) {
			if(this._env._mainAgent.get(i).map.containsKey(this)) {
				connectedAgents++;
				this._beamformingAgents.add(this._env._mainAgent.get(i));
			}
		}
		if(connectedAgents >= 2) {
			this._beamforming = true;
			
			this.bestSubCarrierOnEachTimeslot.clear();
			int allowedSubCarriers = Config.OFDM_NB_SUB_CARRIER / (this._env.getElements().size() - this._env._mainAgent.size());
			//System.err.println("We allowed " + allowedSubCarriers + " subcarriers");
			try {
				this.bestSubCarrierOnEachTimeslot = this.calculator.getBeamSubCarrierByTimeslot(allowedSubCarriers);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				System.exit(0);
			}
		}
		else {
			this._beamforming = false;
			this._beamformingAgents.clear();
			this.bestSubCarrierOnEachTimeslot.clear();
		}
	}
	
	/**
	 * Give the best subcarrier and the mkn for Beamforming
	 * @param agent
	 * @param timeslot
	 * @return a BeamCarrier containing the best subcarrier and the mkn
	 */
	public BeamSubCarriers getBeamSubCarrier(Agent agent, int timeslot) {
		return new BeamSubCarriers(this, agent, timeslot, this.bestSubCarrierOnEachTimeslot.get(new Long(timeslot)));
	}
}
