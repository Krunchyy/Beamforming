package ntr.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import ntr.utils.Config;
import ntr.utils.HashMapIntegerValueComparator;

public class BeamSubCarriersCalculator {
	private List<Agent> agents;
	private Mobile mobile;
	
	public BeamSubCarriersCalculator(List<Agent> agents, Mobile beamforminged) {
		this.agents = agents;
		this.mobile = beamforminged;
	}
	
	/**
	 * @param nb_subcarrier the numbers of subcarriers to finally allow on the ofdm
	 * @return A map with the timeslot as a key and a map <Num_SubCarrier, Mkn_Summed> as a value and all that already sorted
	 * @throws Exception
	 */
	public HashMap<Long, HashMap<Integer, Integer>> getBeamSubCarrierByTimeslot(int nb_subcarrier) throws Exception {
		if(this.agents.size() == 0)
			throw new Exception("Il ne peux pas y avoir aucun agents");
		
		HashMap<Long, HashMap<Integer, Integer>> returnDatas = new HashMap<>();
		
		
		for(long i = 0; i < Config.OFDM_NB_TIME_SLOT ; i++) {
			returnDatas.put(i, this.computeTimeslot(i, nb_subcarrier));
		}
	
		return returnDatas;
	}
	
	private HashMap<Integer, Integer> computeTimeslot(long timeslot, int nb_subcarrier) {
		HashMap<Integer, Integer> map = new HashMap<>();
		for(int i = 0 ; i < Config.OFDM_NB_SUB_CARRIER ; i++) {
			
			map.put(i, new Integer(this.computeMknSum(timeslot, i)));
		}
		
		TreeMap<Integer, Integer> orderedMapByMknsValues = orderedSubcarriersByMkns(map);
		HashMap<Integer, Integer> returnMap = new HashMap<>();
		Iterator<Entry<Integer, Integer>> iterator = orderedMapByMknsValues.entrySet().iterator();
		
		for(int i = 0 ; i < nb_subcarrier ; i++) {
			if(!iterator.hasNext())
				break;
			
			Entry<Integer, Integer> entry = iterator.next();
			returnMap.put(entry.getKey(), entry.getValue());
			
		}	
		
		return returnMap;
	}
	
	private int computeMknSum(long timeslot, int subcarrier) {
		
		int sum = 0;
		
		for(int i = 0 ; i < this.agents.size() ; i++) {
			sum += (int) Math.round(mobile.getSNR( this.agents.get(i), subcarrier, (int)timeslot));
		}
		
		return sum;
	}
	
	//Sort the all subcarriers by Mkn value
	private TreeMap<Integer,Integer> orderedSubcarriersByMkns(HashMap<Integer, Integer> mknSummedonEachSubcarriers) {
		Comparator<Integer> comparator = new HashMapIntegerValueComparator<>(mknSummedonEachSubcarriers);
		//TreeMap is a map sorted by its keys. 
		//The comparator is used to sort the TreeMap by keys. 
		TreeMap<Integer, Integer> result = new TreeMap<Integer, Integer>(comparator);
		result.putAll(mknSummedonEachSubcarriers);
		return result;
	}
	
	public static void printComputation(List<Agent> agents, Mobile beamforminged) throws Exception {
		BeamSubCarriersCalculator calculator = new BeamSubCarriersCalculator(agents, beamforminged);
		
		HashMap<Long, HashMap<Integer, Integer>> datastructure = calculator.getBeamSubCarrierByTimeslot(5);
		
		System.out.println("Five Best Couple <SubCarrier, MknSummed> for each timeslot:");
		for(long i = 0 ; i < Config.OFDM_NB_TIME_SLOT ; i++) {
			System.out.print("Slot: " + i + "<[");
			for(int j = 0 ; j < Config.OFDM_NB_SUB_CARRIER ; j++) {
				
				System.out.print(datastructure.get(i).get(j) + ", ");
			}
			System.out.println("]");
		}
	}
}
