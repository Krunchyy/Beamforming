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
import ntr.utils.HashMapDoubleValueComparator;

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
	public HashMap<Long, HashMap<Integer, Double>> getBeamSubCarrierByTimeslot(int nb_subcarrier) throws Exception {
		if(this.agents.size() == 0)
			throw new Exception("Il ne peux pas y avoir aucun agents");
		
		HashMap<Long, HashMap<Integer, Double>> returnDatas = new HashMap<>();
		
		
		for(long i = this.agents.get(0).getEnvironement()._currentTick ; i < this.agents.get(0).getEnvironement()._currentTick + Config.OFDM_NB_TIME_SLOT ; i++) {
			returnDatas.put(i, this.computeTimeslot(i, nb_subcarrier));
		}
		
		return returnDatas;
	}
	
	private HashMap<Integer, Double> computeTimeslot(long timeslot, int nb_subcarrier) {
		HashMap<Integer, Double> map = new HashMap<>();
		for(int i = 0 ; i < Config.OFDM_NB_SUB_CARRIER ; i++) {
			
			map.put(i, this.computeMknSum(timeslot, i));
		}
		
		TreeMap<Integer, Double> orderedMapByMknsValues = orderedSubcarriersByMkns(map);
		HashMap<Integer, Double> returnMap = new HashMap<>();
		Iterator<Entry<Integer, Double>> iterator = orderedMapByMknsValues.entrySet().iterator();
		
		for(int i = 0 ; i < nb_subcarrier ; i++) {
			if(!iterator.hasNext())
				break;
			
			Entry<Integer, Double> entry = iterator.next();
			returnMap.put(entry.getKey(), entry.getValue());
			
		}	
		
		return returnMap;
	}
	
	private Double computeMknSum(long timeslot, int subcarrier) {
		
		Double sum = 0.;
		
		for(int i = 0 ; i < this.agents.size() ; i++) {
			sum += mobile.getSNR( this.agents.get(i), subcarrier, (int)timeslot);
		}
		
		return sum;
	}
	
	//Sort the all subcarriers by Mkn value
	private TreeMap<Integer,Double> orderedSubcarriersByMkns(HashMap<Integer, Double> mknSummedonEachSubcarriers) {
		Comparator<Integer> comparator = new HashMapDoubleValueComparator<>(mknSummedonEachSubcarriers);
		//TreeMap is a map sorted by its keys. 
		//The comparator is used to sort the TreeMap by keys. 
		TreeMap<Integer, Double> result = new TreeMap<Integer, Double>(comparator);
		result.putAll(mknSummedonEachSubcarriers);
		return result;
	}
	
	public static void printComputation(List<Agent> agents, Mobile beamforminged) throws Exception {
		BeamSubCarriersCalculator calculator = new BeamSubCarriersCalculator(agents, beamforminged);
		
		HashMap<Long, HashMap<Integer, Double>> datastructure = calculator.getBeamSubCarrierByTimeslot(5);
		
		System.out.println("Five Best Couple <SubCarrier, MknSummed> for each timeslot:");
		for(long i = calculator.agents.get(0).getEnvironement()._currentTick ; i < calculator.agents.get(0).getEnvironement()._currentTick + Config.OFDM_NB_TIME_SLOT ; i++) {
			System.out.print("Slot: " + i + "<[");
			for(int j = 0 ; j < Config.OFDM_NB_SUB_CARRIER ; j++) {
				
				System.out.print(datastructure.get(i).get(j) + ", ");
			}
			System.out.println("]");
		}
	}
}
