package ntr.ui.jswing.frames.module;

import ntr.ui.jswing.Window;
import ntr.ui.jswing.frames.ModuleFrame;

public class TickFrame extends ModuleFrame{
	private static final long serialVersionUID = 6967282385001182126L;

	
	public TickFrame(Window window, int x, int y)
	{
		super(window, x, y);
		this.setBounds(x, y, 100, 50);
	}
	
	@Override
	public void render()
	{		
		_content.setText("<html><center> "+ String.valueOf(super._window.getEnvironement().getCurrentTick())+" </center></html>");
		this.repaint();
	}
}
