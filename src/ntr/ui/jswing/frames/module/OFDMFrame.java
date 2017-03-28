package ntr.ui.jswing.frames.module;

import ntr.model.Agent;
import ntr.signal.OFDM;
import ntr.signal.PacketFragment;
import ntr.ui.jswing.Window;
import ntr.ui.jswing.frames.ModuleFrame;
import ntr.utils.Config;

public class OFDMFrame extends ModuleFrame{
	private static final long serialVersionUID = 6967282385001182126L;
	private Agent agent;
	
	public OFDMFrame(Window window, int x, int y, Agent agent)
	{
		super(window, x, y);
		this.agent = agent;
	}
	
	@Override
	public void render()
	{
		String value = 	"<html><table border=1 width=400 style=\"margin: 1px 1px 1px 1px\"><tr><td height=350>";
		String in = "";
		if(this.agent != null)
		{
			//in += "<center>Buffer Max Size : "+ Config.BUFFER_SIZE + "\nAgent : "+ super._window.getEnvironement()._mainAgent.getTag() + "</center>\n\n";
			in = displayOFDM(this.agent._ofdm);
		}
		value += in;
		value += "</td></tr></html>";
		_content.setText(value);
		this.repaint();
	}
	
	public String displayOFDM(OFDM ofdm)
	{
		
		String display = "Tick: ["+ this._window.getEnvironement()._currentTick + "] nbPacketSend : "+ super._window.getEnvironement().getEnvBuffer().size() +"<br/><table border=1 width=400>";
		for(int x = 0  ; x < ofdm._nb_time_slot ; x++)
		{
			display += "<tr>";
			for(int y = 0 ; y < ofdm._nb_sub_carrier ; y++)
			{
				PacketFragment packetF= ofdm._ofdm[x][y];
				if(packetF.parent._receiver.isBeamforming()) {
					display += "<td height="+(350/Config.OFDM_NB_TIME_SLOT)+" style=\"color:#80BFFF\">";
					display += "" + (packetF == null ? " " : packetF.parent._receiver.getTag()+":"+packetF._mkn) + "";
					display += "</td>";
				}
				else {
					display += "<td height="+(350/Config.OFDM_NB_TIME_SLOT)+">";
					display += (packetF == null ? " " : packetF.parent._receiver.getTag()+":"+packetF._mkn);
					display += "</td>";
				}
			}
			display += "</tr>";
		}
		display += "</table>";
		return display;
	}
}