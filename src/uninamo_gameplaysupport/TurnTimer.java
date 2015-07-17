package uninamo_gameplaysupport;

import omega_util.SimpleGameObject;
import exodus_world.Area;
import exodus_world.AreaListener;
import flash_timers.ContinuousTimer;
import flash_timers.TimerEventListener;
import genesis_event.HandlerRelay;
import uninamo_gameplaysupport.TestEvent.TestEventType;
import uninamo_main.GameSettings;

/**
 * The turnTimer informs multiple objects about the pass of turns. The timer 
 * continues to inform the objects until the room ends
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class TurnTimer extends SimpleGameObject implements AreaListener, TestListener, 
		TimerEventListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private ContinuousTimer timer;
	private TurnHandler listenerHandler;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new turnTimer that will instantly start to produce turn events
	 * @param handlers The handlers that will handle the timer and its components (
	 * actorHandler, areaListenerHandler, TestHandler)
	 */
	public TurnTimer(HandlerRelay handlers)
	{
		super(handlers);
		
		// Initializes attributes
		this.listenerHandler = new TurnHandler(null);
		this.timer = new ContinuousTimer(GameSettings.turnDuration, 0, handlers);
		
		this.timer.setIsActiveStateOperator(getIsActiveStateOperator());
		this.timer.setIsDeadStateOperator(getIsDeadStateOperator());
		
		this.timer.getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onTimerEvent(int timerid)
	{
		// Informs the objects about a turn
		this.listenerHandler.onTurnEvent();
	}
	
	@Override
	public void onTestEvent(TestEvent event)
	{
		// Resets the timer on test start
		if (event.getType() == TestEventType.START)
			this.timer.reset();
	}

	@Override
	public void onAreaStateChange(Area area, boolean newState)
	{
		// TODO: Maybe this is not necessary?
		
		// Dies on room end
		if (!newState)
			getIsDeadStateOperator().setState(true);
	}
	
	
	// GETTERS & SETTERS	---------------------
	
	/**
	 * @return The turnHandler that informs the objects about turn events
	 */
	public TurnHandler getListenerHandler()
	{
		return this.listenerHandler;
	}
}
