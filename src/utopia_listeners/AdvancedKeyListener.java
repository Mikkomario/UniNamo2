package utopia_listeners;

import utopia_handleds.LogicalHandled;
import utopia_handlers.KeyListenerHandler;

/**
 * Keylisteners are interested in the user's activities on the keyboard and 
 * should be notified when a key is pressed, down or released.<br>
 * Remember to add the listener to a KeyListenerHandler
 *
 * @author Mikko Hilpinen.
 *         Created 28.11.2012.
 * @see KeyListenerHandler
 */
public interface AdvancedKeyListener extends LogicalHandled
{
	/**
	 * This method is called at each step when a key is down
	 *
	 * @param key The key that is currently pressed
	 * @param keyCode The key's keycode (used for some keys)
	 * @param coded Is the pressed key coded
	 * @param steps How many steps has the key been pressed since the last event
	 */
	public void onKeyDown(char key, int keyCode, boolean coded, double steps);
	
	/**
	 * This method is called once when a key is pressed
	 *
	 * @param key The key that is currently pressed
	 * @param keyCode The key's keycode (used for some keys)
	 * @param coded Is the pressed key coded
	 */
	public void onKeyPressed(char key, int keyCode, boolean coded);
	
	/**
	 * This method is called when a key is released
	 *
	 * @param key The key that is currently pressed
	 * @param keyCode The key's keycode (used for some keys)
	 * @param coded Is the pressed key coded
	 */
	public void onKeyReleased(char key, int keyCode, boolean coded);
}
