package utopia_timers;

import utopia_handlers.ActorHandler;
import utopia_listeners.TimerEventListener;

/**
 * SingularTimer is a timer that causes only a single TimerEvent and then dies.
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public class SingularTimer extends AbstractTimer
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new timer that will inform the user about an timer event after 
	 * <i>delay</i> steps.
	 *
	 * @param user The timer listener that will be informed about the timerevent
	 * @param delay How many steps will pass before the event is thrown
	 * @param id The identifier of the timer, this will be given with the 
	 * thrown event. The user can differentiate events caused by this 
	 * particular timer using this id.
	 * @param actorhandler The actorhandler that will inform the object about 
	 * steps
	 */
	public SingularTimer(TimerEventListener user, int delay, int id, 
			ActorHandler actorhandler)
	{
		super(user, delay, id, actorhandler);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onTimerEvent()
	{
		// SingularTimer can only be used once
		kill();
	}
}
