package ntr.ui.jswing.frames;

import java.awt.Color;

import javax.swing.JLayeredPane;
import javax.swing.border.AbstractBorder;

import ntr.ui.jswing.Window;

public class Onglets extends JLayeredPane {
	private static final long serialVersionUID = 6020445606327649542L;
	
	private final Window _window;
	public Onglets(Window window){
		super();

		 _window = window;
		AbstractBorder brdr = new TextBubbleBorder(Color.BLACK,2,16,0);
		
		setFocusable(false);
		setVisible(true);
		setOpaque(true);
		setBackground(Color.GRAY);
		setBounds(10, 270,1170, 40);
		setBorder(brdr);
		printOnglets();
	}
	
	
	public void printOnglets(){
		/*List<WorkPage> workpages = _window.getEditor().getWorkPages();
		
		for(int i = 0; i < workpages.size() ; i++)
		{
			WorkPage workepage = workpages.get(i);
			JLabel onglet = new Onglet();
			onglet.setLocation(60+ (210*i), 5);
			if(_window.getEditor().getActiveWorkPage() == workepage)
			{
				onglet.setBackground(Color.GREEN);
			}
			onglet.setText(workepage.getFileName());
			this.add(onglet);
		}*/
	}
	
}
