package ntr.environement.pertubation;

import java.util.List;
import java.util.Random;

import ntr.environement.Environement;
import ntr.model.IModel;
import ntr.signal.Alteration;
import ntr.signal.Packet;

public class PathLost extends Alteration{
	
	private double _delta; // delta between sender and receiver
	private long seed;


	@Override
	public void alterate(List<IModel> elements, Environement env) {
		// TODO Auto-generated method stub
		
	}

	
	
	public double getDelta(IModel sender, IModel receiver) {
		int _deltaX = sender.getLocation()._x - receiver.getLocation()._x;
		int _deltaY = sender.getLocation()._y - receiver.getLocation()._y;
		_delta = Math.sqrt(Math.pow(_deltaX, 2) + Math.pow(_deltaY, 2));
		return _delta;
	}
	
	private Random generator = new Random(seed);
	double getRandom(){
		return generator.nextDouble();
	}
	
	public Packet changeValidityBit (Packet packet){
		double _deltaFromReceiver;
		_deltaFromReceiver = getDelta(packet._sender, packet._target);
		if(_deltaFromReceiver > 1.) {
			if (getRandom() < 0.2) {
				packet._isValid = false;
			}
		}
		else if (_deltaFromReceiver > 2.) {
			if (getRandom() < 0.5) {
				
			}
		}
		
		
		return packet;
	}
	
}
