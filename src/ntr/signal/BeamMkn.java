package ntr.signal;

import java.util.ArrayList;
import java.util.Iterator;

public class BeamMkn {
	private double _cumulMkn;
	private double _mknA;
	private double _mknB;
	private int subcarrier;
	
	public BeamMkn(int subcarrier) {
		this.setSubcarrier(subcarrier);
		_cumulMkn = -1.;
		_mknA = 0.;
		_mknB = 0.;
	}
	
	public BeamMkn(double cumul, double mknA, double mknB, int subcarrier) {
		this.setSubcarrier(subcarrier);
		_cumulMkn = cumul;
		_mknA = mknA;
		_mknB = mknB;
	}
	
	public double getCumulMkn() {
		return _cumulMkn;
	}
	public void setCumulMkn(double _cumulMkn) {
		this._cumulMkn = _cumulMkn;
	}
	public double getMknA() {
		return _mknA;
	}
	public void setMknA(double _mknA) {
		this._mknA = _mknA;
	}
	public double getMknB() {
		return _mknB;
	}
	public void setMknB(double _mknB) {
		this._mknB = _mknB;
	}
	
	public static ArrayList<BeamMkn> sortByCumul(ArrayList<BeamMkn> list) {
		ArrayList<BeamMkn> sort = new ArrayList<>();
		while(! list.isEmpty()) {
			Iterator<BeamMkn> it = list.iterator();
			BeamMkn worst = new BeamMkn(-1);
			int index = 0;
			for(int i=0; it.hasNext(); i++) {
				BeamMkn val = it.next();
				if(worst.getCumulMkn() == -1) {
					worst.setCumulMkn(val.getCumulMkn());
					worst.setMknA(val.getMknA());
					worst.setMknB(val.getMknB());
					index = i;
				}
				else if(val.getCumulMkn() < worst.getCumulMkn()) {
					worst.setCumulMkn(val.getCumulMkn());
					worst.setMknA(val.getMknA());
					worst.setMknB(val.getMknB());
					index = i;
				}
			}
			sort.add(worst);
			list.remove(index);
		}
		return sort;
	}

	public int getSubcarrier() {
		return subcarrier;
	}

	public void setSubcarrier(int subcarrier) {
		this.subcarrier = subcarrier;
	}
	
/*	public static void main(String[] args) {
		BeamMkn b1 = new BeamMkn(7, 3, 4);
		BeamMkn b2 = new BeamMkn(1, 1, 0);
		BeamMkn b3 = new BeamMkn(9, 5, 4);
		BeamMkn b4 = new BeamMkn(4, 3, 1);
		
		ArrayList<BeamMkn> liste = new ArrayList<>();
		liste.add(b1);
		liste.add(b2);
		liste.add(b3);
		liste.add(b4);
		System.out.print("Liste initiale : {");
		Iterator<BeamMkn> it1 = liste.iterator();
		while(it1.hasNext()) {
			System.out.print(it1.next().getCumulMkn());
			if(it1.hasNext()) System.out.print(", ");
		}
		System.out.println("}");
		ArrayList<BeamMkn> sorted = new ArrayList<>();
		sorted = sortByCumul(liste);
		System.out.print("Liste triée : {");
		Iterator<BeamMkn> it2 = sorted.iterator();
		while(it2.hasNext()) {
			System.out.print(it2.next().getCumulMkn());
			if(it2.hasNext()) System.out.print(", ");
		}
		System.out.println("}");
	}*/
}
