package ntr.ihm;

import ntr.environement.Environement;
import ntr.model.IModel;
import ntr.model.Location;

public class IHMTerminal {
	
	public static void displayEnv(Environement env) {
		
		IModel[][] _map = new IModel[env._size][env._size];
		
		for(IModel model : env.getElements())
		{
			Location loc = model.getLocation();
			if(loc == null)
			{
				//Warn modil no more on environement
				continue;
			}
			
			if(loc._x >= env._size || loc._y >= env._size)
			{
				//WARN bad location
				continue;
			}
			_map[loc._x][loc._y] = model;
		}
		
		//build spacer
		String space = "";
		space += "-";
		for(int nbChar = 0 ; nbChar < env._size ; nbChar++)
		{
			space += " - -";
		}
		
		
		
		for(int nbLine = 0 ; nbLine < env._size ; nbLine++)
		{
			//space
			System.out.println(space);
			
			//line
			System.out.print("|");
			for(int nbChar = 0 ; nbChar < env._size ; nbChar++)
			{
				IModel model = _map[nbLine][nbChar];
				System.out.print(" "+(model == null ? " " : model.getTag()) +" |");
			}
			System.out.println("");
		}
		System.out.println(space);
	}

}
