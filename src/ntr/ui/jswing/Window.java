package ntr.ui.jswing;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ntr.environement.Environement;
import ntr.ui.JSwingGui;
import ntr.ui.jswing.frames.ModuleFrame;
import ntr.ui.jswing.frames.TextFrame;
import ntr.ui.jswing.frames.module.BufferModule;
import ntr.ui.jswing.frames.module.DebitModule;
import ntr.ui.jswing.frames.module.InputModule;
import ntr.ui.jswing.frames.module.TickFrame;
import ntr.ui.jswing.frames.module.OFDMFrame;
import ntr.ui.jswing.frames.module.PingModule;
import ntr.utils.ISubject;


public class Window extends JFrame {
	private static final long serialVersionUID = -1113864882444705613L;

	private static JLayeredPane panel;
	ModuleFrame pingFrame;
	ModuleFrame bufferFrame;
	ModuleFrame inputFrame;
	ModuleFrame OFDMFrame;
	ModuleFrame tickFrame;
	
	private final JSwingGui _gui;
	
	public Window(JSwingGui gui){
		super();
		_gui = gui;
		start();
	}
	
	public void start(){
		
		panel = new JLayeredPane();
        
		/*
		 * Menu Main
		 */
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("Menu");
		
		JMenuItem panelmenu = new JMenuItem();
		menu1.add(panelmenu);
		menuBar.add(menu1);
		panel.add(menuBar);
		
		
		
		//general
		setTitle("NTR"); // On donne un titre a l'application
		setSize(1200, 900); // On donne une taille a notre fenetre
		setLocationRelativeTo(null); // On centre la fenetre sur l'ecran
		setResizable(false); // On interdit la redimensionnement de la fenetre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // On dit a
														// l'application de se
														// fermer lors du clic
														// sur la croix

		
		panel.setBounds(0, 0, 1200, 800);//TODO Config
		setContentPane(panel);	
		setBackground(Color.BLACK);
		
		/*
		ongletsFrame = new Onglets(this);
		staticFrame = new StaticFrame(this);
		dynamicFrame = new DynamicFrame(this);

		panel.add(ongletsFrame);
		panel.add(staticFrame);
		panel.add(dynamicFrame);
		*/
		
		pingFrame = new PingModule(this,10,10);
		bufferFrame = new BufferModule(this,10,430);
		inputFrame = new DebitModule(this,510,10);
		OFDMFrame = new OFDMFrame(this,510,430);
		tickFrame = new TickFrame(this,1010,10);
		
		panel.add(pingFrame);
		panel.add(bufferFrame);
		panel.add(inputFrame);
		panel.add(OFDMFrame);
		panel.add(tickFrame);

		
		panel.add(new TextFrame(this, 1040, 100));
		panel.add(new TextFrame(this, 1040, 200));
		panel.add(new TextFrame(this, 1040, 300));
		panel.add(new TextFrame(this, 1040, 400));
		panel.add(new TextFrame(this, 1040, 500));
		panel.add(new TextFrame(this, 1040, 600));
        this.addKeyListener(new KeyBoardListener(this));
        
		setFocusable(true);
		requestFocusInWindow();
		setVisible(true);
		panel.repaint();
	}

	public void updateRender()
	{
		pingFrame.render();
		bufferFrame.render();
		inputFrame.render();
		OFDMFrame.render();
		tickFrame.render();
	}
	
	public Environement getEnvironement(){
		return _gui.getEnvironement();
	}
	
	public ISubject getDispatcher(){
		return _gui.getDispatcher();
	}

}
