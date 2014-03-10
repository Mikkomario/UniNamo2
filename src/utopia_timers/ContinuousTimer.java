package utopia_timers;

import utopia_handlers.ActorHandler;
import utopia_listeners.TimerEventListener;

/**
 * ContinuousTimer will inform the user about timer events constantly after 
 * certain intervals.
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public class ContinuousTimer extends AbstractTimer
{
	// ATTRIBUTES	------------------------------------------------------
	
	private int interval;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new timer that will start to inform the user at given intervals
	 *
	 * @param user The timer listener that will be informed about the timer 
	 * events
	 * @param interval How many steps there are between each timer event
	 * @param actorhandler The actorhandler that will inform the timer about 
	 * steps (optional)
	 * @param id The identifier of the timer, this will be given with the 
	 * thrown event. The user can differentiate events caused by this 
	 * particular timer using this id.
	 */
	public ContinuousTimer(TimerEventListener user, int interval, int id, 
			ActorHandler actorhandler)
	{
		super(user, interval, id, actorhandler);
		
		// Initializes attributes
		this.interval = interval;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public void onTimerEvent()
	{
		// Resets the timer
		reset();
	}
	
	
	// OTHER METHODS	------------------------------------------------
	
	/**
	 * Resets the timer back to the start of the delay as if a cycle had just 
	 * ended
	 */
	public void reset()
	{
		delay(this.interval);
	}
}
