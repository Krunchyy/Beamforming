package ntr.ui.jswing.frames.module;

import java.util.ArrayList;

import ntr.simulation.Coordonnee;
import ntr.simulation.Graph;
import ntr.ui.jswing.Window;
import ntr.ui.jswing.frames.ModuleFrame;
import ntr.utils.Config;

public class InputModule extends ModuleFrame{
	private static final long serialVersionUID = 6967282385001182126L;

	
	public InputModule(Window window, int x, int y)
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
			int tick = 0;
			for(int nbPacket : super._window.getEnvironement()._mainAgent.generator.totals)
			{
				coord.add(new Coordonnee(tick++, nbPacket));
			}
			
			in = Graph.displayGraph(coord.toArray(new Coordonnee[0]), "Tick", "Qte", false);
			in = in.replaceAll("\n", "<br/> ");
			in = in.replaceAll(" ", "&ensp;");
		}
		value += in;
		value += "</td></tr></html>";
		
		_content.setText(value);
		this.repaint();
	}
}
