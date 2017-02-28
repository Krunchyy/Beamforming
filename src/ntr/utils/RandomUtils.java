package ntr.utils;

import java.util.Random;

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
}
