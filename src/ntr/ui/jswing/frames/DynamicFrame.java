package ntr.ui.jswing.frames;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

import ntr.ui.jswing.Window;

public class DynamicFrame extends JPanel {
	private static final long serialVersionUID = 6020445606327649541L;
	public  JLabel _content  = new JLabel();
	
	private Window _window;
	public DynamicFrame(Window window){
		super();
		_window = window;
		
		AbstractBorder brdr = new TextBubbleBorder(Color.BLACK,2,16,0);
		
		setFocusable(false);
		setVisible(true);

		//_content.setBackground(Color.GRAY);
		this.setBounds(10, 50,1170, 200);
		
		
		_content.setOpaque(true);
        _content.setBounds(0, 0, 90, 40);
        _content.setBorder(brdr);
        _content.setBackground(Color.GRAY);
        _content.setPreferredSize( new Dimension( 1100, 160 ) );
        renderText();
		this.add(_content);
	}
	
	public void renderText(){
		/*
    	String display = _window.getEnvironement().getDynamicBuffer();
    	//System.out.println("Buff   : "+display);
    	display = display.replaceAll("<", "&lt;");
    	display = display.replaceAll(">", "&gt;");
    	display = display.replaceAll(" ", "&ensp;");
    	display = display.replaceAll("\n", "<br/>");

    	//System.out.println("Display  : "+display);
    	_content.setText("<html> "+ display + "<span style='background:#FFFFFF'>&ensp;</span></html>");
	*/
	}
}
