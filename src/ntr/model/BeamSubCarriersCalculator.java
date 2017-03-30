package ntr.model;

import java.util.HashMap;
import java.util.List;

import ntr.utils.Config;

public class BeamSubCarriersCalculator {
	private List<Agent> agents;
	private Mobile mobile;
	
	public BeamSubCarriersCalculator(List<Agent> agents, Mobile beamforminged) {
		this.agents = agents;
		this.mobile = beamforminged;
	}
	
	public HashMap<Long, HashMap<Integer, Double>> getBeamSubCarrierByTimeslot() throws Exception {
		if(this.agents.size() == 0)
			throw new Exception("Il ne peux pas y avoir aucun agents");
		
		HashMap<Long, HashMap<Integer, Double>> returnDatas = new HashMap<>();
		
		
		for(long i = this.agents.get(0).getEnvironement()._currentTick ; i < this.agents.get(0).getEnvironement()._currentTick + Config.OFDM_NB_TIME_SLOT ; i++) {
			returnDatas.put(i, this.computeTimeslot(i));
		}
		
		return returnDatas;
	}
	
	private HashMap<Integer, Double> computeTimeslot(long timeslot) {
		HashMap<Integer, Double> returnDatas = new HashMap<>();
		
		for(int i = 0 ; i < Config.OFDM_NB_SUB_CARRIER ; i++) {
			
			returnDatas.put(i, this.computeMknSum(timeslot, i));
		}
		return returnDatas;
	}
	
	private Double computeMknSum(long timeslot, int subcarrier) {
		
		Double sum = 0.;
		
		for(int i = 0 ; i < this.agents.size() ; i++) {
			sum += mobile.getSNR( this.agents.get(i), subcarrier, (int)timeslot);
		}
		
		return sum;
	}
	
	public static void printComputation(List<Agent> agents, Mobile beamforminged) throws Exception {
		BeamSubCarriersCalculator calculator = new BeamSubCarriersCalculator(agents, beamforminged);
		
		HashMap<Long, HashMap<Integer, Double>> datastructure = calculator.getBeamSubCarrierByTimeslot();
		
		
		for(long i = calculator.agents.get(0).getEnvironement()._currentTick ; i < calculator.agents.get(0).getEnvironement()._currentTick + Config.OFDM_NB_TIME_SLOT ; i++) {
			System.out.print("[");
			for(int j = 0 ; j < Config.OFDM_NB_SUB_CARRIER ; j++) {
				
				System.out.print(datastructure.get(i).get(j) + ", ");
			}
			System.out.println("]");
		}
	}
}
