package uninamo_gameplaysupport;

import java.util.ArrayList;
import java.util.List;

import genesis_event.Event;

/**
 * Test events are generated when a test starts or ends
 * @author Mikko Hilpinen
 * @since 12.7.2015
 */
public class TestEvent implements Event
{
	// ATTRIBUTES	-------------------------
	
	private TestEventType type;
	
	
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new test event
	 * @param type The type of the event
	 */
	public TestEvent(TestEventType type)
	{
		this.type = type;
	}
	
	
	// IMPLEMENTED METHODS	----------------

	@Override
	public List<Event.Feature> getFeatures()
	{
		List<Event.Feature> features = new ArrayList<>();
		features.add(getType());
		return features;
	}
	
	
	// GETTERS & SETTERS	----------------
	
	/**
	 * @return The type of the event
	 */
	public TestEventType getType()
	{
		return this.type;
	}
	
	/**
	 * @return if the test is currently running
	 */
	public boolean testRunning()
	{
		return this.type == TestEventType.START;
	}
	
	
	// INTERFACES	-----------------------
	
	/**
	 * This interface is implemented by all features that are applicable for a testEvent
	 * @author Mikko Hilpinen
	 * @since 12.7.2015
	 */
	public interface Feature extends Event.Feature
	{
		// Used as a wrapper
	}

	
	// ENUMERATIONS	-----------------------
	
	/**
	 * These are the different basic types of test events
	 * @author Mikko Hilpinen
	 * @since 12.7.2015
	 */
	public enum TestEventType implements Feature
	{
		/**
		 * The event was generated when a test started
		 */
		START,
		/**
		 * The event was generated when a test ended
		 */
		END;
	}
}
