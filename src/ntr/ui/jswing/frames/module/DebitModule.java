package ntr.ui.jswing.frames.module;

import java.util.ArrayList;
import java.util.HashMap;

import ntr.signal.PacketFragment;
import ntr.simulation.Coordonnee;
import ntr.simulation.Graph;
import ntr.ui.jswing.Window;
import ntr.ui.jswing.frames.ModuleFrame;
import ntr.utils.Config;

public class DebitModule extends ModuleFrame{
	private static final long serialVersionUID = 6967282385001182126L;

	class debit{
		public int d=0;
	}
	public DebitModule(Window window, int x, int y)
	{
		super(window, x, y);
	}
	
	
	@Override
	public void render()
	{
		String value = 	"<html><table border=1 width=400 ><tr><td height=350>";
		String in = "";
		if(super._window.getEnvironement()._mainAgent != null)
		{
			HashMap<Integer, debit> debits = new HashMap<>();
			
			for(PacketFragment packet : super._window.getEnvironement().getEnvBuffer())
			{
				debit b = debits.get((int)packet._dateExpedition%Config.OFDM_NB_TIME_SLOT);
				if(b == null)
				{
					b = new debit();
					debits.put((int)packet._dateExpedition%Config.OFDM_NB_TIME_SLOT, b);
				}
				b.d += packet._dataAvailableSize;
			}
			ArrayList<Coordonnee> coord = new ArrayList<>();
			for(int key : debits.keySet())
			{
				coord.add(new Coordonnee(key, debits.get(key).d));
			}
			
			in = Graph.displayGraph(coord.toArray(new Coordonnee[0]), "Tick", "Debit", false);
			in = in.replaceAll("\n", "<br/> ");
			in = in.replaceAll(" ", "&ensp;");
		}
		value += in;
		value += "</td></tr></html>";
		
		_content.setText(value);
		this.repaint();
	}
}
