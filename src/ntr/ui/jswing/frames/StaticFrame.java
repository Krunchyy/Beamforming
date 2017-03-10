package ntr.ui.jswing.frames;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import ntr.ui.jswing.Window;

public class StaticFrame extends JPanel{
	private static final long serialVersionUID = -5686393694681124990L;
	
	public JLabel _content = new JLabel();
	public int displayQuote = 0;
	public int MAX_DISPLAY_TIME = 1;
	public int TICK = 1000;
	
	private final Window _window;
	public StaticFrame(Window window){
		super();
		_window = window;
		 
		AbstractBorder brdr = new TextBubbleBorder(Color.BLACK,2,0,0);
		
        setBorder(new EmptyBorder(10,10,10,10));
		setFocusable(false);
		setVisible(true);
		this.setBackground(Color.GRAY);
		this.setBounds(10, 300, 1170, 550);
		this.setBorder(brdr);
		this.add(_content);

		renderText();
	}
	
	
	public void renderText(){
		/*
		WorkPage wp = _window.getEditor().getActiveWorkPage();
		String buffer = "";
		if(wp == null)
		{
			buffer = "<html> Page not found </html>";
		}
		else
		{
			Selection[] selection = wp.getSelection();
			String[] buff = wp.getStaticBuffer();
			String line; 
			
			
			buffer = buffer.replaceAll("<", "&lt;");
			buffer = buffer.replaceAll(">", "&gt;");
			buffer = buffer.replaceAll(" ", "&ensp;");
			buffer = buffer.replaceAll("\n", "<br/>");
			
			buffer = "<html>" + buffer;
			for(int ligneIndex  = 0 ; ligneIndex < buff.length ; ligneIndex++)
			{
				line = buff[ligneIndex];

				if(ligneIndex == selection[0].indexOfLine)
				{
					
					int pos = selection[0].indexOnLine;
					if(pos > line.length())
						pos = line.length();
					line = line.substring(0, pos) + "<span style='background:#FFFFFF'>" + line.substring(pos, line.length());
					
				}
				
				if(ligneIndex == selection[1].indexOfLine)
				{
					
					int pos = selection[1].indexOnLine;
					if(ligneIndex == selection[0].indexOfLine)
						pos += "<span style='background:#FFFFFF'>".length();
					if(pos > line.length())
						pos = line.length();
					line = line.substring(0, pos) + "</span>" + line.substring(pos, line.length());
				}
				line += "<br>";
				buffer += line;
			}
			buffer += "</html>";
		}

		displayQuote++;
		_content.setText(buffer);
		*/
		this.repaint();
	}
}
