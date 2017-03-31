package ntr.utils;

public class Config {
	//Shadowing config:
	public static int SHADOWING_UPATE_FREQUENCY = 5;
	public static int SHADOWING_MAXIMUM_ALTERATION = 15;
	
	//IModel config:
	public static char MOBILE_TAG = 'M';
	public static char AGENT_TAG = 'A';
	
	//PacketGenerator config:
	public static int SIZE = 100;
	public static int MIN_AVERAGE = 1; // average quantity of packets
	public static int MAX_AVERAGE = 20;
	public static int MIN_DELAY = 2; // lifetime of this average quantity
	public static int MAX_DELAY = 5;
	public static int MIN_OFFSET = -2; // offset
	public static int MAX_OFFSET = 2;
	
	//OFDM
	public static boolean OFDM_DEBUG = false;
	public static int OFDM_NB_TIME_SLOT = 10;
	public static int OFDM_NB_SUB_CARRIER = 10;
	
	//BeamForming when true ordonnanceur fill the rest of the ofdm with BeamForming Packet Fragment if all other mobile buffers are empty
	public static boolean FILL_OFDM_WITH_BEAM_MOBILE = false;
	
	
	public static int BUFFER_SIZE = 1024;
	
	//Environment
	public static int ENVIRONEMENT_SIZE = 10;
	
	//Debug:
	public static boolean DEBUG_FIX_MKN = false;
	public static int DEBUG_FIX_MKN_VALUE = 6;
	
	//IHM
	public static boolean OFDM_FOR_ALL = false;
	
	
	public static boolean COUNT_PACKETS = false;
	public static int BeamPacketSended = 0;
	public static int noBeamPacketSended = 0;
	
	public static int BeamPacketGenerated = 0;
	public static int noBeamPacketGenerated = 0;
	
}
