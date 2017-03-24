package ntr.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import ntr.environement.Environement;
import ntr.signal.BeamSubCarrier;
import ntr.signal.Packet;
import ntr.utils.Config;
import ntr.utils.Distance;
import ntr.utils.RandomUtils;

public class Mobile extends Model {
	private char _tag = Config.MOBILE_TAG;
	private int _networkCondition;
	
	// Beamforming
	public ArrayList<IModel> _beamformingAgents = new ArrayList<>();
	private ArrayList<Integer> _beamformingBestSubCarriers; // Liste des meilleurs subcarriers par timeslot
	private int _nbAgentsConnected;
	private boolean _beamforming;
	public Queue<Packet> _filePacketsBeam = new ArrayBlockingQueue<Packet>(Config.BUFFER_SIZE);

	//agent, timeslot(10) , subcarrier(10)
	private ConcurrentHashMap<IModel, ConcurrentHashMap<Integer, ArrayList<Double>>> _mknMap = new ConcurrentHashMap<IModel, ConcurrentHashMap<Integer, ArrayList<Double>>>();

	public Mobile(Location loc, Environement env) {
		super(loc, env);
		_networkCondition = 0;
		_nbAgentsConnected = 0;
		_beamforming = false;
	}
	
	public boolean isBeamforming() {
		return _beamforming;
	}
	
	public void setBeamforfing(boolean b) {
		_beamforming = b;
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
			System.out.println("getSNR for mobile agent pas dans la map");
			return 0;
		}

		ArrayList<Double> mapTimeslot = mapAgent.get(timeslot % agent._ofdm._nb_time_slot);
		if(mapTimeslot == null) {
			System.out.println("getSNR for mobile timeslot inexistant");
			return 0;
		}
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
				double mkn = 1 + RandomUtils.get(0, 10)/distance; //TODO: am�liorer la valeur g�n�r�e surtout pour les mobiles �loign�es
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

	/**
	 * Add to _beamformingBestSubCarriers the best subcarrier of the agents for every timeslot
	 * @param agent
	 */
	public void computeBeamCarrier(Agent agent) {
		ConcurrentHashMap<Integer, ArrayList<Double>> mapAgent = _mknMap.get(agent);
		if(mapAgent == null) {
			System.out.println("computeBeamCarrier for mobile : agent pas dans la map");
			return;
		}
		ArrayList<Double> mapTimeslot = mapAgent.get(0);
		if(mapTimeslot == null) {
			System.out.println("computeBeamCarrier for mobile : timeslots inexistants");
			return;
		}

		if(_nbAgentsConnected == 0) {
			Set<IModel> keys = _mknMap.keySet();
			Iterator<IModel> it = keys.iterator();
			while (it.hasNext()) {
				_nbAgentsConnected++;
				_beamformingAgents.add(it.next());
				_beamforming = true;
			}
		}
		if(_nbAgentsConnected == 2) {
			_beamformingBestSubCarriers.clear(); // on vide la liste des meilleurs subcarriers
			ConcurrentHashMap<Integer, ArrayList<Double>> map = _mknMap.get(_beamformingAgents.get(0));
			Set<Integer> keys = map.keySet(); // Set des timeslots de l'agent 1
			Iterator<Integer> it1 = keys.iterator();

			ConcurrentHashMap<Integer, ArrayList<Double>> map2 = _mknMap.get(_beamformingAgents.get(1));
			Set<Integer> keys2 = map2.keySet(); // Set des timeslots de l'agent 2
			Iterator<Integer> it2 = keys2.iterator();

			while (it1.hasNext()) {
				ArrayList<Double> list1 = map.get(it1.next()); // liste des subcarriers de l'agent 1
				Iterator<Double> it11 = list1.iterator();

				ArrayList<Double> list2 = map.get(it2.next()); // liste des subcarriers de l'agent 2
				Iterator<Double> it22 = list2.iterator();

				int sub=0;
				Double valA1 = (double) 0;
				Double valA2 = (double) 0;
				for(int i=0; it11.hasNext(); i++) { // d�termination du meilleur subcarrier selon les mkn des deux agents
					Double a1 = it11.next();
					Double a2 = it22.next();
					if(a1 > valA1 && a2 > valA2){
						valA1 = a1;
						valA2 = a2;
						sub=i;
					}
				}
				_beamformingBestSubCarriers.add(sub);
			}
		}
	}

	/**
	 * Give the best subcarrier and the mkn for Beamforming
	 * @param agent
	 * @param timeslot
	 * @return a BeamCarrier containing the best subcarrier and the mkn
	 */
	public BeamSubCarrier getBeamSubCarrier(Agent agent, int timeslot) {
		double val = _mknMap.get(agent).get(timeslot).get(_beamformingBestSubCarriers.get(timeslot));
		return new BeamSubCarrier(_beamformingBestSubCarriers.get(timeslot), val);
	}
}
