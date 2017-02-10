package ntr.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ntr.environement.Environement;

public class Mobile extends Model{
	private static Model _instance;
	static{
		try {
			/*
			 * Mobile mob = new Mobile(null, null)
			 * mob.setEn(new Environement(1));
			 *  char name = mob.getTag();
			 */
			
	        String mobileClassName = "ntr.model.Mobile";
	        Class<?> MobileClass = Class.forName(mobileClassName); // convert string classname to class
	        Constructor<?> mobileConstructor = MobileClass.getConstructor(ntr.model.Location.class, ntr.environement.Environement.class);
	        Object mobile = mobileConstructor.newInstance(null , null);
	        
	        
	        String methodName = "";

	        // with single parameter, return void
	        methodName = "setEnv";
	        Method setEnviMethod = mobile.getClass().getMethod(methodName, ntr.environement.Environement.class);
	        setEnviMethod.invoke(mobile, new Environement(1)); // pass arg

	        // without parameters, return string
	        methodName = "getTag";
	        Method getTagMethod = mobile.getClass().getMethod(methodName);
	        char name = (char) getTagMethod.invoke(mobile); // explicit cast

	        /*
	        // with multiple parameters
	        methodName = "printDog";
	        Class<?>[] paramTypes = {String.class, int.class};
	        Method printDogMethod = mobile.getClass().getMethod(methodName, paramTypes);
	        printDogMethod.invoke(mobile, name, 3); // pass args
	        */
	        
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
		
		//() -> new Integer(1);
	}
	public Mobile(Location loc, Environement env)
	{
		super(loc, env);
	}
	
	protected final static synchronized Map<Integer, ?> truc(final int i)
	{
		return new HashMap<Integer, ArrayList<IModel>>();
	}
	
	@Override
	public char getTag() {
		return 'M';
	}
}
