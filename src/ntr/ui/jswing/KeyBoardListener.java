package ntr.ui.jswing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ntr.utils.Config;
import ntr.utils.EEvent;
import ntr.utils.EventBroadCaster;

/**
 * This class catch Keyboard input
 * and call linked Event to ISubject
 * @author Roche Kevin
 */
public class KeyBoardListener implements KeyListener, EventBroadCaster{

	private final Window _window;
	public KeyBoardListener(Window window){_window = window;}

	private boolean ShiftPressed = false;
	private boolean ControlPressed = false;
	
	/**
	 * Analyse {@code e} and call onEvent Method of ISubject
	 * @param e
	 * @param keyStatus
	 */
    private void displayInfo(KeyEvent e, String keyStatus){
    	//Only pressed key are intresting
    	if(e.getID() != KeyEvent.KEY_PRESSED)
    		return;

    	//local var
        int keyCode = e.getKeyCode();
        EEvent event = null;
        Object[] args = new Object[0];
        
        /*************************************************************
         * Control Pressed (ShorCut
         **************************************************************/
        
        if(ControlPressed)
        {
        	if(keyCode >= KeyEvent.VK_F1 && keyCode <= KeyEvent.VK_F12)
        	{
        		//event =  EEvent.SELECT_W; 
        		args = new Object[]{keyCode-KeyEvent.VK_F1};
        	}
        	else
        	{
        		switch(keyCode)
        		{
        		/*
        			case KeyEvent.VK_C : event = EEvent.COPY;break;
        			case KeyEvent.VK_V : event = EEvent.PASTE;break;
        			case KeyEvent.VK_X : event = EEvent.CUT;break;
        			case KeyEvent.VK_A : event = EEvent.INSERT;break;
        			case KeyEvent.VK_Z : event = ShiftPressed ? EEvent.REDO : EEvent.UNDO;break;
        			case KeyEvent.VK_R : event = ShiftPressed ? EEvent.STOP_REC : EEvent.START_REC;break;
        			case KeyEvent.VK_P : event = EEvent.REPLAY;break;
        			*/
        		}
        	}
        }
        /*************************************************************
         * Dynamic Buff
         **************************************************************/
        
        else  if(
    		(keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z ) || //a-z
    		(keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9) ||
    		(keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) ||
    		(keyCode == KeyEvent.VK_SPACE) ||
    		(keyCode == KeyEvent.VK_BACK_SPACE) ||
    		(keyCode == KeyEvent.VK_ENTER) ||
    		(keyCode == KeyEvent.VK_COMMA) ||
    		(keyCode == KeyEvent.VK_MINUS) ||
    		(keyCode == KeyEvent.VK_PLUS)  ||
    		(keyCode == KeyEvent.VK_SLASH) || //   :   ,   /
    		(keyCode == KeyEvent.VK_BACK_SLASH) ||
    		(keyCode == KeyEvent.VK_EQUALS)||
    		(keyCode == KeyEvent.VK_OPEN_BRACKET) ||
    		(keyCode == KeyEvent.VK_CLOSE_BRACKET) ||
    		(keyCode == KeyEvent.VK_PERIOD) ||
    		(keyCode == KeyEvent.VK_ASTERISK) ||
    		(keyCode == KeyEvent.VK_EXCLAMATION_MARK) ||
    		(keyCode == KeyEvent.VK_LEFT_PARENTHESIS) ||
    		(keyCode == KeyEvent.VK_RIGHT_PARENTHESIS) ||
    		(keyCode == KeyEvent.VK_NUMBER_SIGN) ||
    		(keyCode == KeyEvent.VK_MULTIPLY) ||
    		(keyCode == KeyEvent.VK_SEMICOLON) ||
    		(keyCode == KeyEvent.VK_CLOSE_BRACKET) ||
    		(keyCode == KeyEvent.VK_LESS) ||
    		(keyCode == KeyEvent.VK_GREATER) ||
    		(keyCode == KeyEvent.VK_DIVIDE) ||
    		(keyCode == KeyEvent.VK_COLON)
          )
        {
        	//Gui -> DynaBuffer
        	/*
        	String text = _window.getEditor().getDynamicBuffer();
        	
        	switch(keyCode){
        		case KeyEvent.VK_BACK_SPACE : 
        			if(text.length() > 0)
        				text = text.substring(0, text.length()-1);
        			break;
        		case KeyEvent.VK_ENTER :
        			text += "\n";
        			break;
        		case KeyEvent.VK_SLASH :
        			text += "\\";break;		
        		default : text += e.getKeyChar();
        	}
        	_window.getEditor().setDynamicBuffer(text);
        	*/
        	
        	//DynaBuffer -> Gui
        	_window.getEnvironement().tick(Config.OFDM_NB_TIME_SLOT);
        	event = EEvent.GUI_UPDATE;
        }
        /*************************************************************
         * Selection Event
         **************************************************************/
        else if(keyCode >= KeyEvent.VK_LEFT &&  keyCode <= KeyEvent.VK_DOWN)
        {
        	int direction = 0;
        	switch(keyCode)
        	{
        		case KeyEvent.VK_UP : direction = 0;break;
        		case KeyEvent.VK_LEFT : direction = 3;break;
        		case KeyEvent.VK_DOWN : direction = 2;break;
        		case KeyEvent.VK_RIGHT : direction = 1;break;
        		default : System.out.println("[Error] on direction KeyLisener");
        	}
        	 //event = EEvent.SELECT_S;
        	if(ShiftPressed)
        	{
        		//event = EEvent.SELECT_E;
        	}
        	args = new Object[]{direction};
        }
        
    	if(event != null)
    	{
    		_window.getDispatcher().onEvent(event, args, this);
    	}
    }

    
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("pressed");
        /*************************************************************
         * ShortCut Key
         **************************************************************/
        if(e.getKeyCode() == KeyEvent.VK_SHIFT)
        {
        	if(!ShiftPressed)
        		//System.out.println("[DEBUG]Shift Pressed");
        	ShiftPressed = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
        {
        	if(!ControlPressed)
        		//System.out.println("[DEBUG]ControlPressed Pressed");
        	ControlPressed = true;
        }
		 displayInfo(e, "KEY TYPED: ");
	}

	
	@Override
	public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SHIFT)
        {
        	if(ShiftPressed)
        		//System.out.println("[DEBUG]Shift Released");
        	ShiftPressed = false;
        	
        }
        else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
        {
        	if(ControlPressed)
        		//System.out.println("[DEBUG]ControlPressed Released");
        	ControlPressed = false;
        }
		displayInfo(e, "KEY PRESSED: ");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		displayInfo(e, "KEY RELEASED: ");
		
	}
}