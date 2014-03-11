package uninamo_gameplaysupport;

import utopia_handleds.Handled;

/**
 * TestListener function differently in test mode and creation mode and 
 * should be informed when the mode changes
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public interface TestListener extends Handled
{
	/**
	 * The object should go into test mode in a reseted state
	 */
	public void onTestStart();
	/**
	 * The object should return from the test mode
	 */
	public void onTestEnd();
}
