package uninamo_gameplaysupport;

import genesis_event.EventSelector;
import genesis_event.Handled;

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
	 * This method is called when a test event occurs
	 * @param event The event that just occurred
	 */
	public void onTestEvent(TestEvent event);
	
	/**
	 * @return The eventSelector that tells which events the listener is interested in
	 */
	public EventSelector<TestEvent> getTestEventSelector();
}
