package uninamo_gameplaysupport;

import uninamo_userinterface.FinishButton;
import utopia_handleds.Handled;
import utopia_handlers.Handler;

/**
 * VictoryHandler keeps track of all the victoryConditions in a stage and 
 * decides when a stage has been cleared.
 * 
 * @author Mikko Hilpinen
 * @since 11.3.2014
 */
public class VictoryHandler extends Handler implements VictoryCondition
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean lastClearStatus;
	private FinishButton finishButton;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty victoryHandler
	 * @param finishButton The finishButton that will be shown after the 
	 */
	public VictoryHandler(FinishButton finishButton)
	{
		super(false, null);
		
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
	protected Class<?> getSupportedClass()
	{
		return VictoryCondition.class;
	}

	@Override
	protected boolean handleObject(Handled h)
	{
		if (!((VictoryCondition) h).isClear())
		{
			this.lastClearStatus = false;
			return false;
		}
		
		return true;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds a new victoryCondition to the list of required victory conditions
	 * @param v The victoryCondition to be considered in the future
	 */
	public void addVictoryCondition(VictoryCondition v)
	{
		addHandled(v);
	}
	
	/**
	 * Prompts the handler to check its victory status and react accordingly
	 */
	public void recheck()
	{
		// Shows the victory button
		if (isClear() && this.finishButton != null)
		{
			this.finishButton.setVisible();
			this.finishButton.activate();
		}
	}
}
