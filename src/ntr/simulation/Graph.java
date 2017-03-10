package ntr.simulation;

public class Graph {
	public static String displayGraph(Coordonnee[] tablePts , String xName, String yName)
	{
		return displayGraph(tablePts, xName, yName, true);
	}
	public static String displayGraph(Coordonnee[] tablePts , String xName, String yName, boolean enableSpace)
	{
		int xMax = 0;
		int yMax = 0;
		//get maxValue;
		for(Coordonnee coor : tablePts)
		{
			if(coor._x > xMax)
				xMax = coor._x;
			if(coor._y > yMax)
				yMax = coor._y;
		}
		
		xMax += 1;
		yMax += 1;
		
		String result ="   "+yName+"\n\n  /|\\ \n";//build header
		for(int y = yMax-1 ; y >= 0 ; y--)
		{
			result += (y<10? y+" " : y)  +"-|";
			result += getLine(tablePts, xMax, y);
		}
		
		//build footer
		result += "    ";
		for(int x = 0 ; x < xMax ; x++)
		{
			result += "___";
		}
		result += "\\  "+xName+"\n    ";
		for(int x = 0 ; x < xMax ; x++)
		{
			result += " | ";
		}
		result += "/ \n";
		result += "    ";
		for(int x = 0 ; x < xMax ; x++)
		{
			if(enableSpace)
				result += " "+(x < 10? x+" " : x);
			else
			{
				result += "&nbsp;"+x+"&nbsp;";
			}
		}
		return result;
	}
	
	public static String getLine(Coordonnee[] tablePts, int xMax, int y){
		String result = "";
		char[] lineA = new char[xMax];
		for(int i = 0 ; i < xMax ; i++)
		{
			lineA[i] = ' ';
		}
		for(Coordonnee coor : tablePts)
		{
			if(coor._y == y)
			{
				lineA[coor._x] = coor._tag;
			}
		}
		for(int i = 0 ; i < xMax ; i++)
		{
			result += " "+lineA[i] +" ";
		}
		result += "\n";
		return result;
	}
}
