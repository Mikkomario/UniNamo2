package uninamo_manual;

import utopia_handleds.Handled;

/**
 * Pages can be opened and closed. They may or may not contain information
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public interface Page extends Handled
{
	/**
	 * Opens the page, revealing its information
	 */
	public void open();
	
	/**
	 * Closes the page, hiding its information
	 */
	public void close();
}
