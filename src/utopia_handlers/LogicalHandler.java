package utopia_handlers;

import utopia_handleds.Handled;
import utopia_handleds.LogicalHandled;

/**
 * LogicalHandlers specialize in logicalhandleds instead of just any handlers. 
 * This class provides some methods necessary for all subclasses and can be used as
 * a logical handled in other handlers.
 *
 * @author Mikko Hilpinen.
 *         Created 8.12.2012.
 */
public abstract class LogicalHandler extends Handler implements LogicalHandled
{
	// CONSTRUCTOR	-------------------------------------------------------

	/**
	 * Creates a new logicalhandler. Handled objects must be added manually later
	 *
	 * @param autodeath Will the handler die if it runs out of living handleds
	 * @param superhandler The handler that will handle this handler (optional)
	 */
	public LogicalHandler(boolean autodeath, LogicalHandler superhandler)
	{
		super(autodeath, superhandler);
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public boolean isActive()
	{
		// Tests if any handlers are active
		ActiveCheckOperator checkoperator = new ActiveCheckOperator();
		handleObjects(checkoperator);
		
		return checkoperator.isActive();
	}

	@Override
	public void activate()
	{
		// tries to activate all the handled objects
		handleObjects(new ActivateOperator());
	}

	@Override
	public void inactivate()
	{
		// tries to inactivate all the handled objects
		handleObjects(new InactivateOperator());
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class ActivateOperator extends HandlingOperator
	{
		@Override
		protected boolean handleObject(Handled h)
		{
			// Makes the handled active
			((LogicalHandled) h).activate();
			return true;
		}
	}
	
	private class InactivateOperator extends HandlingOperator
	{
		@Override
		protected boolean handleObject(Handled h)
		{
			// Makes the handled inactive
			((LogicalHandled) h).inactivate();
			return true;
		}	
	}
	
	private class ActiveCheckOperator extends HandlingOperator
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private boolean foundactive;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public ActiveCheckOperator()
		{
			this.foundactive = false;
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------
		
		@Override
		protected boolean handleObject(Handled h)
		{
			// Checks if the handled is active
			if (((LogicalHandled) h).isActive())
			{
				this.foundactive = true;
				return false;
			}
			
			return true;
		}
		
		
		// GETTERS & SETTERS	-----------------------------------------
		
		public boolean isActive()
		{
			return this.foundactive;
		}
	}
}
