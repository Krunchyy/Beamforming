package ntr.ui.jswing.frames.module;

import java.util.List;

import ntr.model.Agent;
import ntr.ui.jswing.Window;
import ntr.ui.jswing.frames.ModuleFrame;
import ntr.utils.Config;

public class BufferModule extends ModuleFrame{
	private static final long serialVersionUID = 6967282385001182126L;

	
	public BufferModule(Window window, int x, int y)
	{
		super(window, x, y);
	}
	
	@Override
	public void render()
	{
		List<Agent> agents = super._window.getEnvironement()._mainAgent;
		
		String value = 	"<html><table border=1 width=400 ><tr><td height=350>";
		String in = "";
		for(int i=0 ; i < agents.size() ; i++)
		{
			in += "<center>Buffer Max Size : "+ Config.BUFFER_SIZE + "\nAgent : "+ agents.get(i).getTag() + "\n "+agents.get(0)._ordonnanceur.getClass().getSimpleName()+"</center>\n\n";
			in += 	agents.get(i).displayBuffer(40);
			in = in.replaceAll("\n", "<br/> ");
			in = in.replaceAll(" ", "&ensp;");
		}
		value += in;
		value += "</td></tr></html>";
		
		_content.setText(value);
		this.repaint();
	}
}
