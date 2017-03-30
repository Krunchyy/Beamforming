package ntr.simulation;

public class CoordonneeL {
	public long _x;
	public double _y;
	public char _tag;
	
	public CoordonneeL(long x, double y)
	{
		this(x, y, 'X');
	}
	
	public CoordonneeL(long x, double y, char tag)
	{
		_x=x;
		_y=y;
		_tag = tag;
	}
}
