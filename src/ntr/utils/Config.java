package ntr.utils;

public class Config {
	//Shadowing config:
	public static int SHADOWING_UPATE_FREQUENCY = 5;
	public static int SHADOWING_MAXIMUM_ALTERATION = 15;
	
	//IModel config:
	public static char MOBILE_TAG = 'M';
	public static char AGENT_TAG = 'A';
	
	//PacketFragment config:
	public static int PACKET_DATA_SIZE = 100;
	
	//PacketGenerator config:
	public static int MIN_AVERAGE = 1; // average quantity of packets
	public static int MAX_AVERAGE = 20;
	public static int MIN_DELAY = 2; // lifetime of this average quantity
	public static int MAX_DELAY = 5;
	public static int MIN_OFFSET = -2; // offset
	public static int MAX_OFFSET = 2;
	
	//OFDM
	public static boolean OFDM_DEBUG = false;
}
