package ntr;

import ntr.environement.Environement;
import ntr.ihm.IHMTerminal;
import ntr.model.Agent;
import ntr.model.IModel;
import ntr.model.Location;
import ntr.model.Mobile;

public class Main {
	public static final int SIZE = 5;
	public static void main(String[] args)
	{
		Environement env = new Environement(SIZE);
		
		new Mobile(new Location(0,0), env);//auto env reg
		new Agent(new Location(3,1), env);
		
		
		IModel m = new Mobile(new Location(4,4), null);//manual env reg
		m.setEnv(env);
		
		
		IHMTerminal.displayEnv(env);
	}
}
