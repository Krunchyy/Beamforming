package ntr.signal;

public class Signal {
	private final long _frequency;
	private final int _nbModulation;
	
	//table of data by symbol
	//_data.get(0) -> get char for first symbol
	private char[] _symbolTable;
	
	//arrays of symbol to send
	private final int[] _data;
	private int[] _dataAltered;
	public Signal(long freq, char[] symbolTable, int[] data)
	{
		_frequency = freq;
		_nbModulation = symbolTable.length;
		_symbolTable = symbolTable;
		_data = data;
		setAlteredData(data);
	}
	
	public void alterate(Alteration alte)
	{
		//DO data alteration (
	}

	public int[] getAlteredData() {
		return _dataAltered;
	}

	public void setAlteredData(int[] dataAltered) {
		_dataAltered = dataAltered;
	}

	public int[] getData() {
		return _data;
	}

	public int getModulationSize() {
		return _nbModulation;
	}

	public long getFrequency() {
		return _frequency;
	}
}
