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

	
	
	public void setDelta(IModel sender, IModel receiver) {
		int _deltaX = sender.getLocation()._x - receiver.getLocation()._x;
		int _deltaY = sender.getLocation()._y - receiver.getLocation()._y;
		_delta = Math.sqrt(Math.pow(_deltaX, 2) + Math.pow(_deltaY, 2));
	}
	
	public double getDelta(){
		return _delta;
	}
	
	private Random generator = new Random();
	int getRandom(){
		return generator.nextInt(100);
	}
	
	public void PathLossAlteration(Packet packet, Environement env){
		
		double delta = getDelta();
		double maxSize = Math.sqrt(2)*env._size;
		setDelta(packet._sender, packet._target);
			
		// Distance between sender and receiver is far
		if(  (3/4)*maxSize <= delta && delta <= maxSize) {
			packet._sender.setNetworkCondition(25);
			if(getRandom() < 25) packet._isValid = true;
			else packet._isValid = false;
		}
		
		else if ( (1/2)*maxSize <= delta && delta < (3/4)*maxSize) {
			packet._sender.setNetworkCondition(50);
			if(getRandom() < 50) packet._isValid = true;
			else packet._isValid = false;
		}
		
		else if ( (1/4)*maxSize <= delta && delta < (1/2)*maxSize) {
			packet._sender.setNetworkCondition(75);
			if(getRandom() < 75) packet._isValid = true;
			else packet._isValid = false;
		}
		
		else if ( 0 <= delta && delta < (1/4)*maxSize) {
			packet._sender.setNetworkCondition(100);
			if(getRandom() < 100) packet._isValid = true;
			else packet._isValid = false;
		}
		
		// Mobile is not in the area
		else {
			packet._sender.setNetworkCondition(0);
			packet._isValid = false;
		}
	}
	
}
