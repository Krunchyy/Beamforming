package ntr.utils;

import ntr.model.IModel;

public class Distance {
	public static double setDelta(IModel sender, IModel receiver) {
		int _deltaX = sender.getLocation()._x - receiver.getLocation()._x;
		int _deltaY = sender.getLocation()._y - receiver.getLocation()._y;
		return Math.sqrt(Math.pow(_deltaX, 2) + Math.pow(_deltaY, 2));
	}
}
