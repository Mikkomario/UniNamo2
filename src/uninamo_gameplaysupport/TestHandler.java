package uninamo_gameplaysupport;

import genesis_logic.Handled;
import genesis_logic.Handler;

/**
 * TestHandler informs multiple objects that a test should begin or end.
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class TestHandler extends Handler implements TestListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private int lastEvent;
	
	private static final int START = 1, END = 2;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty testHandler
	 * 
	 * @param superhandler The testHandler that will inform this handler (optional)
	 */
	public TestHandler(TestHandler superhandler)
	{
		super(false, superhandler);
		
		// Initializes attributes
		this.lastEvent = 0;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onTestStart()
	{
		// Informs the objects
		this.lastEvent = START;
		handleObjects();
	}

	@Override
	public void onTestEnd()
	{
		this.lastEvent = END;
		handleObjects();
	}

	@Override
	protected Class<?> getSupportedClass()
	{
		return TestListener.class;
	}

	@Override
	protected boolean handleObject(Handled h)
	{
		// Informs the object about an event
		if (this.lastEvent == START)
			((TestListener) h).onTestStart();
		else
			((TestListener) h).onTestEnd();
		
		return true;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds the testable into the list of informed testables
	 * @param t The testable to be informed
	 */
	public void addTestable(TestListener t)
	{
		addHandled(t);
	}
}
