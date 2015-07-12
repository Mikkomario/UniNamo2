package uninamo_gameplaysupport;

import genesis_event.EventSelector;
import genesis_event.Handler;
import genesis_event.HandlerType;
import genesis_event.StrictEventSelector;

/**
 * TestHandler informs multiple objects that a test should begin or end.
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class TestHandler extends Handler<TestListener> implements TestListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private TestEvent lastEvent;
	private EventSelector<TestEvent> selector;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty testHandler
	 * 
	 * @param superhandler The testHandler that will inform this handler (optional)
	 */
	public TestHandler(TestHandler superhandler)
	{
		super(false);
		
		// Initializes attributes
		this.selector = new StrictEventSelector<>();
		this.lastEvent = null;
		
		if (superhandler != null)
			superhandler.add(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onTestEvent(TestEvent event)
	{
		this.lastEvent = event;
		handleObjects();
	}

	@Override
	public EventSelector<TestEvent> getTestEventSelector()
	{
		return this.selector;
	}

	@Override
	public HandlerType getHandlerType()
	{
		return UninamoHandlerType.TEST;
	}

	@Override
	protected boolean handleObject(TestListener h)
	{
		// Informs the object about an event, if necessary
		if (h.getTestEventSelector().selects(this.lastEvent))
			h.onTestEvent(this.lastEvent);
		
		return true;
	}
}
