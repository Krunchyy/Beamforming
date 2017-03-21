package ntr.ui.jswing.frames.module;

import ntr.signal.OFDM;
import ntr.signal.PacketFragment;
import ntr.ui.jswing.Window;
import ntr.ui.jswing.frames.ModuleFrame;
import ntr.utils.Config;

public class OFDMFrame extends ModuleFrame{
	private static final long serialVersionUID = 6967282385001182126L;

	
	public OFDMFrame(Window window, int x, int y)
	{
		super(window, x, y);
	}
	
	@Override
	public void render()
	{
		String value = 	"<html><table border=1 width=400 style=\"margin: 1px 1px 1px 1px\"><tr><td height=350>";
		String in = "";
		if(super._window.getEnvironement()._mainAgent != null)
		{
			//in += "<center>Buffer Max Size : "+ Config.BUFFER_SIZE + "\nAgent : "+ super._window.getEnvironement()._mainAgent.getTag() + "</center>\n\n";
			in = displayOFDM(super._window.getEnvironement()._mainAgent._ofdm);
		}
		value += in;
		value += "</td></tr></html>";
		_content.setText(value);
		this.repaint();
	}
	
	public String displayOFDM(OFDM ofdm)
	{
		
		String display = "nbPacketSend : "+ super._window.getEnvironement().getEnvBuffer().size() +"<br/><table border=1 width=400>";
		for(int x = 0  ; x < ofdm._nb_time_slot ; x++)
		{
			display += "<tr>";
			for(int y = 0 ; y < ofdm._nb_sub_carrier ; y++)
			{
				PacketFragment packetF= ofdm._ofdm[x][y];
				display += "<td height="+(350/Config.OFDM_NB_TIME_SLOT)+">";
				display += (packetF == null ? " " : packetF.parent._receiver.getTag()+":"+packetF._mkn);
				display += "</td>";
			}
			display += "</tr>";
		}
		display += "</table>";
		return display;
	}
}