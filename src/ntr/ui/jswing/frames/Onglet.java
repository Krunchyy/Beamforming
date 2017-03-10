package ntr.ui.jswing.frames;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.border.AbstractBorder;

public class Onglet extends JLabel{
	private static final long serialVersionUID = 1376997271163178588L;
	
	public Onglet(){
		AbstractBorder brdr = new TextBubbleBorder(Color.BLACK,2,30,0);
		
		setFocusable(false);
		setVisible(true);
		setOpaque(true);
		setBackground(Color.GRAY);
		setBounds(0, 0,200, 20);
		setBorder(brdr);
	}
}
