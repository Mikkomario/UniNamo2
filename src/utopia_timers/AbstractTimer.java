package utopia_timers;

import utopia_handleds.Actor;
import utopia_handlers.ActorHandler;
import utopia_listeners.TimerEventListener;

/**
 * AbstractTimer informs an object about the passing of time. Different Timers 
 * may inform the object differently. once or repeatedly, for example.
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public abstract class AbstractTimer implements Actor
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private TimerEventListener user;
	private double timeleft;
	private boolean dead, active;
	private int id;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new timer and sets it to inform an user after delay steps.
	 *
	 * @param user The listener that the timer will inform about the event(s)
	 * @param delay The delay before the event
	 * @param actorhandler The handler which will inform the timer about 
	 * act-events, null if the timer will be handled manually
	 * @param id The identifier of the timer, this will be given with the 
	 * thrown event. The user can differentiate events caused by this 
	 * particular timer using this id.
	 */
	public AbstractTimer(TimerEventListener user, int delay, int id,
			ActorHandler actorhandler)
	{
		// Initializes attributes
		this.user = user;
		this.timeleft = delay;
		this.dead = false;
		this.active = true;
		this.id = id;
		
		// Adds the object to the handler
		if (actorhandler != null)
			actorhandler.addActor(this);
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * This method is called when the timer informs the user about an timer 
	 * event. The subclass should react to this event either by resetting or 
	 * stopping the timer
	 */
	protected abstract void onTimerEvent();
	
	
	// IMPELENTED METHODS	---------------------------------------------

	@Override
	public boolean isActive()
	{
		// Timer is only considered active if the user is also active
		return (this.active && this.user.isActive());
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}

	@Override
	public boolean isDead()
	{
		// The Timer is considered alive only if the user is alive as well
		return (this.dead || this.user.isDead());
	}

	@Override
	public void kill()
	{
		this.dead = true;
	}

	@Override
	public void act(double steps)
	{
		// Depletes the timer and may inform the user if the delay has passed
		this.timeleft -= steps;
		
		if (this.timeleft <= 0)
		{
			this.user.onTimerEvent(this.id);
			// Also informs the subclass if it wants to react somehow
			onTimerEvent();
		}
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Delays the event by <i>delay</i> steps
	 *
	 * @param delay How many steps the event will be dalayed
	 */
	protected void delay(int delay)
	{
		this.timeleft += delay;
	}
	
	/**
	 * Sets the event to be caused after <i>delay</i> steps
	 *
	 * @param delay How many steps there will be before an event is thrown
	 */
	public void setDelay(int delay)
	{
		this.timeleft = delay;
	}
}
