package uninamo_gameplaysupport;

import genesis_event.Handler;
import genesis_event.HandlerType;
import genesis_util.StateOperator;

/**
 * TurnHandlers inform multiple objects about turn events.
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class TurnHandler extends Handler<TurnBased> implements TurnBased
{
	// ATTRIBUTES	--------------------
	
	private StateOperator listensToEventsOperator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty turnHandler
	 * 
	 * @param superHandler The TurnHandler that will inform the handler about 
	 * turn events (optional)
	 */
	public TurnHandler(TurnHandler superHandler)
	{
		super(false);
		
		this.listensToEventsOperator = new AnyListenToTurnEventsOperator();
		
		if (superHandler != null)
			superHandler.add(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onTurnEvent()
	{
		// Informs the objects
		handleObjects();
	}
	
	@Override
	public StateOperator getListensToTurnEventsOperator()
	{
		return this.listensToEventsOperator;
	}

	@Override
	public HandlerType getHandlerType()
	{
		return UninamoHandlerType.TURN;
	}

	@Override
	protected boolean handleObject(TurnBased h)
	{
		if (h.getListensToTurnEventsOperator().getState())
			h.onTurnEvent();
		
		return true;
	}
	
	
	// SUBCLASSES	------------------
	
	private class AnyListenToTurnEventsOperator extends ForAnyHandledsOperator
	{
		// CONSTRUCTOR	--------------
		
		public AnyListenToTurnEventsOperator()
		{
			super(true);
		}
		
		
		// IMPLEMENTED METHODS	-------

		@Override
		protected StateOperator getHandledStateOperator(TurnBased h)
		{
			return h.getListensToTurnEventsOperator();
		}
	}
}
