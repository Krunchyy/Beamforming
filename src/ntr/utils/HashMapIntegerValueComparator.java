package ntr.utils;

import java.util.Comparator;
import java.util.HashMap;

public class HashMapIntegerValueComparator<T> implements Comparator<T> {
	//Source: http://www.programcreek.com/2013/03/java-sort-map-by-value/
	private HashMap<T, Integer> map;
	
	public HashMapIntegerValueComparator(HashMap<T, Integer> map) {
		this.map = map;
	}
	
	@Override
	public int compare(T o1, T o2) {
		if(map.get(o1) >= map.get(o2)){
			return -1;
		}else{
			return 1;
		}	
	}

}
