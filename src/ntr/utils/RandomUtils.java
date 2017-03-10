package ntr.utils;

import java.util.Random;

import ntr.model.IModel;

public class RandomUtils {
	private static final Random random = new Random();
	
	/**
	 * 
	 * @param min parameter include
	 * @param max parameter exclude
	 * @return
	 */
	public static int get(int min, int max) {
		return random.nextInt(max - min);
	}
	
	/**
	 * little workaround to compute easily multipathFading value
	 * @return value in range [0, 100[
	 */
	public static double multitrajet() {
		return RandomUtils.get(0, 101)/101.0;
	}
	
	public static double setDelta(IModel sender, IModel receiver) {
		int _deltaX = sender.getLocation()._x - receiver.getLocation()._x;
		int _deltaY = sender.getLocation()._y - receiver.getLocation()._y;
		return Math.sqrt(Math.pow(_deltaX, 2) + Math.pow(_deltaY, 2));
	}
}
