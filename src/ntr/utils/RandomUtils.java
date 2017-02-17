package ntr.utils;

import java.util.Random;

public class RandomUtils {
	private static final Random random = new Random();
	
	public static int get(int min, int max) {
		return random.nextInt(max - min);
	}
}
