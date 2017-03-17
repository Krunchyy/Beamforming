package ntr.ui.jswing.frames.module;

import java.util.ArrayList;

import ntr.signal.Packet;
import ntr.simulation.Coordonnee;
import ntr.simulation.Graph;
import ntr.ui.jswing.Window;
import ntr.ui.jswing.frames.ModuleFrame;
import ntr.utils.Config;

public class PingModule extends ModuleFrame{
	private static final long serialVersionUID = 6967282385001182126L;

	
	public PingModule(Window window, int x, int y)
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
			final ArrayList<Coordonnee> coord = new ArrayList<>();
			for(Packet packet : super._window.getEnvironement().getEnvBuffer())
			{
				coord.add(new Coordonnee((int)packet.getDateArrivee()%Config.OFDM_NB_TIME_SLOT, (int)(packet.getDateArrivee() - packet.getDateCreation()), packet._receiver.getTag()));
			}
			in = Graph.displayGraph(coord.toArray(new Coordonnee[0]), "Tick", "Ping", false);
			in = in.replaceAll("\n", "<br/> ");
			in = in.replaceAll(" ", "&ensp;");
		}
		value += in;
		value += "</td></tr></html>";
		
		_content.setText(value);
		this.repaint();
	}
}
