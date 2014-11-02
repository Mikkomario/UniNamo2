package uninamo_gameplaysupport;

import genesis_logic.Handled;
import genesis_logic.Handler;

/**
 * TurnHandlers inform multiple objects about turn events. They also work as 
 * TestHandlers if need be
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class TurnHandler extends Handler implements TurnBased
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty turnHandler
	 * 
	 * @param superHandler The TurnHandler that will inform the handler about 
	 * turn events (optional)
	 */
	public TurnHandler(TurnHandler superHandler)
	{
		super(false, superHandler);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onTurnEvent()
	{
		// Informs the objects
		handleObjects();
	}

	@Override
	protected Class<?> getSupportedClass()
	{
		return TurnBased.class;
	}

	@Override
	protected boolean handleObject(Handled h)
	{
		// Informs the object about an turn event
		((TurnBased) h).onTurnEvent();
		
		return true;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Adds a turn based instance to the list of informed objects
	 * 
	 * @param t The object to be informed in the future
	 */
	public void addTurnListener(TurnBased t)
	{
		addHandled(t);
	}
}
