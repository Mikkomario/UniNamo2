package uninamo_gameplaysupport;

import uninamo_main.UninamoHandlerType;
import genesis_event.Handler;
import genesis_event.HandlerType;

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
	public HandlerType getHandlerType()
	{
		return UninamoHandlerType.TEST;
	}

	@Override
	protected boolean handleObject(TestListener h)
	{
		// Informs the object about an event
		h.onTestEvent(this.lastEvent);
		return true;
	}
}
