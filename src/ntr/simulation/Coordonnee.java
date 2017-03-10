package ntr.simulation;

public class Coordonnee {
	public int _x;
	public int _y;
	public char _tag;
	
	public Coordonnee(int x, int y)
	{
		this(x, y, 'X');
	}
	
	public Coordonnee(int x, int y, char tag)
	{
		_x=x;
		_y=y;
		_tag = tag;
	}
}
