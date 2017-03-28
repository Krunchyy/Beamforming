package ntr.ui.jswing;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ntr.environement.Environement;
import ntr.model.Agent;
import ntr.ui.JSwingGui;
import ntr.ui.jswing.frames.ModuleFrame;
import ntr.ui.jswing.frames.module.BufferModule;
import ntr.ui.jswing.frames.module.OFDMFrame;
import ntr.ui.jswing.frames.module.PingModule;
import ntr.ui.jswing.frames.module.TickFrame;
import ntr.utils.Config;
import ntr.utils.ISubject;

public class ExtentedWindow extends JFrame {
	
	private ModuleFrame module;
	private final Window window;
	
	public ExtentedWindow(String title, Window window){
		this.window = window;
		this.setTitle(title);
	}
	
	public void start(ModuleFrame module){
		this.module = module;
		this.setSize(500, 500);
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setVisible(true);
	    this.getContentPane().add(module);
	    module.repaint();
	}

	public void updateRender()
	{
		module.render();
		getEnvironement().getEnvBuffer().clear();
		
		for (int i = 0; i < getEnvironement()._mainAgent.size(); i++) {
			getEnvironement()._mainAgent.get(i).generator.totals.clear();
		}
	}
	
	public Environement getEnvironement(){
		return window.getEnvironement();
	}
	
	public ISubject getDispatcher(){
		return window.getDispatcher();
	}
}
