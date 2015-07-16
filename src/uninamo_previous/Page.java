package uninamo_previous;

import omega_util.GameObject;

/**
 * Pages can be opened and closed. They may or may not contain information. Pages should 
 * be closed by default.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public interface Page extends GameObject
{
	/**
	 * Changes if the page is opened (visible) or closed
	 * @param newState If the page should be open (true) or closed (false)
	 */
	public void setOpenState(boolean newState);
}
