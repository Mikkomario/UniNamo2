package utopia_fileio;

import java.awt.event.KeyEvent;

import utopia_handlers.KeyListenerHandler;
import utopia_listeners.AdvancedKeyListener;

/**
 * Inputreader listens to the keyboard and tries to form string depending 
 * on which key was pressed
 *
 * @author Mikko Hilpinen.
 *         Created 25.8.2013.
 */
public class InputReader implements AdvancedKeyListener
{
	// ATTRIBUTES	-------------------------------------------------------
	
	private String input;
	private boolean active, dead;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new inputreader
	 * 
	 * @param keylistenerhandler The keylistenerhandler that will inform the 
	 * reader about key events
	 */
	public InputReader(KeyListenerHandler keylistenerhandler)
	{
		// Initializes attributes
		this.input = "";
		this.active = true;
		this.dead = false;
		
		// Adds the object to the handler if possible
		if (keylistenerhandler != null)
			keylistenerhandler.addKeyListener(this);
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The input the object has received
	 */
	public String getInput()
	{
		return this.input;
	}
	
	/**
	 * Changes the input to a certain value
	 * @param newinput the new input
	 */
	public void setInput(String newinput)
	{
		this.input = newinput;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------------
	
	@Override
	public void onKeyPressed(char key, int keyCode, boolean coded)
	{
		// Doesn't react to coded keys
		if (coded)
			return;
		
		// If backspace was pressed, removes the last character
		if (key == KeyEvent.VK_BACK_SPACE && this.input.length() >= 1)
			this.input = this.input.substring(0, this.input.length()-1);
		// If other (SPACE, TAB, ENTER, ESC, and DELETE), doesn't react
		else if (key == KeyEvent.VK_DELETE || key == KeyEvent.VK_TAB || 
				key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE 
				|| key == KeyEvent.VK_SPACE)
			return;
		// Otherwise adds the character to the string
		else
			this.input += key;
	}

	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}

	@Override
	public boolean isDead()
	{
		return this.dead;
	}

	@Override
	public void kill()
	{
		this.dead = true;
	}

	@Override
	public void onKeyDown(char key, int keyCode, boolean coded, double steps)
	{
		// Doens't do anything
	}

	@Override
	public void onKeyReleased(char key, int keyCode, boolean coded)
	{
		// Doesn't do anything
	}
}
