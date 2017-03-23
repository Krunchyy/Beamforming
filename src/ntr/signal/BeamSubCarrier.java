package ntr.signal;

public class BeamSubCarrier {
	
	private int _subcarrier;
	private double _mkn;
	
	public BeamSubCarrier(int subcarrier, double mkn) {
		_subcarrier = subcarrier;
		_mkn = mkn;
	}
	
	public int getSubCarrier() {
		return _subcarrier;
	}
	
	public double getMkn() {
		return _mkn;
	}
}
