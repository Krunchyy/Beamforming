package ntr.ui.jswing.frames;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import ntr.ui.jswing.Window;

public class TextFrame extends JPanel{
	private static final long serialVersionUID = -5686393694681124990L;
	
	public JTextArea _content = new JTextArea(2,10);
	public JLabel _txt = new JLabel();
	
	public final Window _window;
	
	public TextFrame(Window window, int x, int y){
		super();
		_window = window;
		 
		AbstractBorder brdr = new TextBubbleBorder(Color.RED,2,16,0);
		
        setBorder(new EmptyBorder(1,1,1,1));
		setFocusable(false);
		setVisible(true);
		this.setBackground(Color.GRAY);
		this.setBounds(x, y, 150, 100);
		this.setBorder(brdr);
		_content.setLocation(0,0);
		_content.setFocusable(true);
		_content.setEditable(true);
		_content.setText("truc");
		_txt.setText("machin");
		this.add(_txt);
		this.add(_content);
	}
	
	public void render()
	{
		//System.out.println("render");
		//_content.setText(String.valueOf(_window.getEnvironement().getCurrentTick()));
		this.repaint();
	}
}
