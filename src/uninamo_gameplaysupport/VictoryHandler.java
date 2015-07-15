package uninamo_gameplaysupport;

import uninamo_main.UninamoHandlerType;
import gateway_ui.AbstractButton;
import genesis_event.Handler;
import genesis_event.HandlerType;

/**
 * VictoryHandler keeps track of all the victoryConditions in a stage and 
 * decides when a stage has been cleared.
 * 
 * @author Mikko Hilpinen
 * @since 11.3.2014
 */
public class VictoryHandler extends Handler<VictoryCondition> implements VictoryCondition
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean lastClearStatus;
	private AbstractButton finishButton;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty victoryHandler
	 * @param finishButton The finishButton that will be shown after the 
	 */
	public VictoryHandler(AbstractButton finishButton)
	{
		super(false);
		
		// Initializes attributes
		this.finishButton = finishButton;
		this.lastClearStatus = false;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public boolean isClear()
	{
		// Checks if all conditions have been cleared
		this.lastClearStatus = true;
		handleObjects();
		
		return this.lastClearStatus;
	}
	
	@Override
	public HandlerType getHandlerType()
	{
		return UninamoHandlerType.VICTORY;
	}

	@Override
	protected boolean handleObject(VictoryCondition h)
	{
		if (!h.isClear())
		{
			this.lastClearStatus = false;
			return false;
		}
		
		return true;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Prompts the handler to check its victory status and react accordingly
	 */
	public void recheck()
	{
		// Shows the victory button
		if (isClear() && this.finishButton != null)
		{
			this.finishButton.getIsVisibleStateOperator().setState(true);
			this.finishButton.getIsActiveStateOperator().setState(true);
		}
	}
}
