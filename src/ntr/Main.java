package ntr;

import ntr.environement.Environement;
import ntr.ihm.IHMTerminal;
import ntr.model.Agent;
import ntr.model.IModel;
import ntr.model.Location;
import ntr.model.Mobile;
import ntr.signal.Signal;

public class Main {
	public static final int SIZE = 5;
	public static void main(String[] args)
	{
		Environement env = new Environement(SIZE);
		
		IModel m1 = new Mobile(new Location(0,0), env);//auto env reg
		IModel a = new Agent(new Location(3,1), env);
		IModel m2 = new Mobile(new Location(4,4), null);//manual env reg
		m2.setEnv(env);
		
		
		//creation du signal
		long frenq = 10L;
		char[] symbol = {'a', 'b'};
		int[] data = {0,0,1};
		Signal signalM1 = new Signal(frenq, symbol, data);
		
		//prepare le peer a envoyer un message
		m2.setSignalInProgress(signalM1);
		m2.sendSignalTo(a);//envoi le message (call environement)
		
		//affiche l'etat des peers dans l'environement 
		IHMTerminal.displayEnv(env);
	}
}
