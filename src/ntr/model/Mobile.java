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
	private HashMap<Long, HashMap<Integer, Double>> bestSubCarrierOnEachTimeslot;
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

	/**
	 * Add to _beamformingBestSubCarriers the best subcarrier of the agents for every timeslot
	 * @param agent
	 */
//	public void computeBeamCarrier(Agent agent) {
//		ConcurrentHashMap<Integer, ArrayList<Double>> mapAgent = _mknMap.get(agent);
//		if(mapAgent == null) {
//			System.out.println("computeBeamCarrier for mobile : agent pas dans la map");
//			return;
//		}
//		ArrayList<Double> mapTimeslot = mapAgent.get(0);
//		if(mapTimeslot == null) {
//			System.out.println("computeBeamCarrier for mobile : timeslots inexistants");
//			return;
//		}
//
//		if(Config.OFDM_DEBUG)
//			System.out.println(this+" agents connectés : "+_nbAgentsConnected);
//
//		if(_nbAgentsConnected == 0) {
//			Set<IModel> keys = _mknMap.keySet();
//			Iterator<IModel> it = keys.iterator();
//			while (it.hasNext()) {
//				_nbAgentsConnected++;
//				_beamformingAgents.add(it.next());
//			}
//		}
//		else if(_nbAgentsConnected == 1) {
//			_beamformingAgents.clear();
//			_nbAgentsConnected = 0;
//			Set<IModel> keys = _mknMap.keySet();
//			Iterator<IModel> it = keys.iterator();
//			while (it.hasNext()) {
//				_nbAgentsConnected++;
//				_beamformingAgents.add(it.next());
//			}
//		}
//		else if(_nbAgentsConnected == 2) {
//			_beamforming = true;
//			_beamformingBestSubCarriers.clear(); // on vide la liste des meilleurs subcarriers
//			ConcurrentHashMap<Integer, ArrayList<Double>> map = _mknMap.get(_beamformingAgents.get(0));
//			Set<Integer> keys = map.keySet(); // Set des timeslots de l'agent 1
//			Iterator<Integer> it1 = keys.iterator();
//
//			ConcurrentHashMap<Integer, ArrayList<Double>> map2 = _mknMap.get(_beamformingAgents.get(1));
//			Set<Integer> keys2 = map2.keySet(); // Set des timeslots de l'agent 2
//			Iterator<Integer> it2 = keys2.iterator();
//			try {
//				BeamSubCarriersCalculator.printComputation(agent.getEnvironement()._mainAgent, this);
//			} catch (Exception e) {
//				System.err.println(e.getMessage());
//				e.printStackTrace();
//				System.exit(0);
//			}
//			while (it1.hasNext()) {
//				ArrayList<Double> list1 = map.get(it1.next()); // liste des subcarriers de l'agent 1
//				Iterator<Double> it11 = list1.iterator();
//
//				ArrayList<Double> list2 = map.get(it2.next()); // liste des subcarriers de l'agent 2
//				Iterator<Double> it22 = list2.iterator();
//
//				int sub=0;
//				Double valA1 = (double) 0;
//				Double valA2 = (double) 0;
//				for(int i=0; it11.hasNext(); i++) { // détermination du meilleur subcarrier selon les mkn des deux agents
//					Double a1 = it11.next();
//					Double a2 = it22.next();
//					if((a1+a2) > (valA1+valA2)){
//						valA1 = a1;
//						valA2 = a2;
//						sub=i;
//					}
//				}
//				_beamformingBestSubCarriers.add(sub);
//			}
//		}
//	}

	/*
	 * TODO: faire une liste ordonn�e de X valeurs de mkn par ordre croissant des valeurs cumul�es agent1+2
	 * remplir cette liste avec les X premi�res valeurs g�n�r�es puis remplacer ces valeurs par les suivantes si elles sont plus �lev�es
	 */
//	public void computeAllBeamCarrier(Agent agent) {
//		ConcurrentHashMap<Integer, ArrayList<Double>> mapAgent = _mknMap.get(agent);
//		if(mapAgent == null) {
//			System.out.println("computeBeamCarrier for mobile : agent pas dans la map");
//			return;
//		}
//		ArrayList<Double> mapTimeslot = mapAgent.get(0);
//		if(mapTimeslot == null) {
//			System.out.println("computeBeamCarrier for mobile : timeslots inexistants");
//			return;
//		}
//
//		System.out.println(this+" agents connectés : "+_nbAgentsConnected);
//
//		if(_nbAgentsConnected == 0) {
//			_beamformingAgents.clear();
//			Set<IModel> keys = _mknMap.keySet();
//			Iterator<IModel> it = keys.iterator();
//			while (it.hasNext()) {
//				_nbAgentsConnected++;
//				_beamformingAgents.add(it.next());
//			}
//		}
//		else if(_nbAgentsConnected == 1) {
//			_beamformingAgents.clear();
//			_nbAgentsConnected = 0;
//			Set<IModel> keys = _mknMap.keySet();
//			Iterator<IModel> it = keys.iterator();
//			while (it.hasNext()) {
//				_nbAgentsConnected++;
//				_beamformingAgents.add(it.next());
//			}
//		}
//		else if(_nbAgentsConnected == 2) {
//			_beamforming = true;
//			_mapBeam.clear();
//			int nbMknToGenerate = agent._ofdm._nb_sub_carrier/(agent.getEnvironement().getElements().size() - agent.getEnvironement()._mainAgent.size()); //<------------------------------------------remplacer par nb mobiles
//
//			
//			this.bestSubCarrierOnEachTimeslot.clear();
//			try {
//				this.bestSubCarrierOnEachTimeslot = this.calculator.getBeamSubCarrierByTimeslot(1);
//			} catch (Exception e) {
//				System.err.println(e.getMessage());
//				e.printStackTrace();
//				System.exit(0);
//			}
//			
//			ConcurrentHashMap<Integer, ArrayList<Double>> map = _mknMap.get(_beamformingAgents.get(0));
//			Set<Integer> keys = map.keySet(); // Set des timeslots de l'agent 1
//			Iterator<Integer> it1 = keys.iterator();
//
//			ConcurrentHashMap<Integer, ArrayList<Double>> map2 = _mknMap.get(_beamformingAgents.get(1));
//			Set<Integer> keys2 = map2.keySet(); // Set des timeslots de l'agent 2
//			Iterator<Integer> it2 = keys2.iterator();

//			while(it1.hasNext()) { // boucle sur les timeslots
//				ArrayList<BeamMkn> listeMkn = new ArrayList<>();
//				ArrayList<Double> list1 = map.get(it1.next()); // liste des subcarriers de l'agent 1
//				ArrayList<Double> list2 = map.get(it2.next()); // liste des subcarriers de l'agent 2
//
//				int timeslot = it1.next();
//				it2.next();
//
//				Iterator<Double> it11 = list1.iterator(); // iterateur sur les sub
//				Iterator<Double> it22 = list2.iterator();
//				int j=0;
//				for(; it11.hasNext() && j<nbMknToGenerate; j++) { // remplissage des nbMknToGenerate<nbSubcarriers premières valeurs de la liste des subcarriers
//					Double mknA = it11.next();
//					Double mknB = it22.next();
//					BeamMkn newBeam = new BeamMkn(mknA+mknB, mknA, mknB, j);
//					listeMkn.add(newBeam);
//				}
//				BeamMkn.sortByCumul(listeMkn); // tri de la liste par ordre croissant des valeurs
//				while(it11.hasNext()) { // remplacement des valeurs si plus grandes
//					Double mknA = it11.next();
//					Double mknB = it22.next();
//					Iterator<BeamMkn> it = listeMkn.iterator();
//					for(int x=0; it.hasNext(); x++) { // remplacement
//						j++;
//						BeamMkn beam = it.next();
//						if((mknA+mknB) < (beam.getCumulMkn())){
//							if(x != 0) {
//								listeMkn.remove(x-1);
//								listeMkn.add(x-1, new BeamMkn(mknA+mknB, mknA, mknB, j));
//							}
//						}
//					}
//				}
//				Iterator<BeamMkn> it = listeMkn.iterator();
//				
//				ConcurrentHashMap<Integer, Double> mapMknA = new ConcurrentHashMap<>();
//				ConcurrentHashMap<Integer, Double> mapMknB = new ConcurrentHashMap<>();
//				
//				while(it.hasNext()) {
//					BeamMkn value = it.next();
//					
//					mapMknA.put(value.getSubcarrier(), value.getMknA());
//					mapMknB.put(value.getSubcarrier(), value.getMknB());
//				}
//				
//				BeamSubCarriers b1 = new BeamSubCarriers(mapMknA);
//				BeamSubCarriers b2 = new BeamSubCarriers(mapMknB);
//
//				ConcurrentHashMap<Integer, BeamSubCarriers> mapBeam1 = new ConcurrentHashMap<>();
//				ConcurrentHashMap<Integer, BeamSubCarriers> mapBeam2 = new ConcurrentHashMap<>();
//				
//				mapBeam1.put(timeslot, b1);
//				mapBeam2.put(timeslot, b2);
//				
//				_mapBeamAgents.put(_beamformingAgents.get(0), mapBeam1);
//				_mapBeamAgents.put(_beamformingAgents.get(1), mapBeam2);
//			}
//		}
//	}

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
			int allowedSubCarriers = Config.OFDM_NB_SUB_CARRIER / this._env.getElements().size() - this._env._mainAgent.size();
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
