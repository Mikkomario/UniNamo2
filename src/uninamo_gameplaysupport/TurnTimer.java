package uninamo_gameplaysupport;

import uninamo_main.GameSettings;
import utopia_handlers.ActorHandler;
import utopia_listeners.RoomListener;
import utopia_listeners.TimerEventListener;
import utopia_timers.ContinuousTimer;
import utopia_worlds.Room;

/**
 * The turnTimer informs multiple objects about the pass of turns. The timer 
 * continues to inform the objects until the room ends
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class TurnTimer extends TurnHandler implements RoomListener, Testable, 
		TimerEventListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private ContinuousTimer timer;
	private boolean active;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new turnTimer that will instantly start to produce turn events
	 * 
	 * @param testHandler The testHandler that will inform the timer about 
	 * test events
	 * @param room The room where the timer resides
	 * @param actorHandler The ActorHandler that informs the object about 
	 * step events
	 */
	public TurnTimer(TestHandler testHandler, Room room, 
			ActorHandler actorHandler)
	{
		super(null);
		
		// Initializes attributes
		this.active = true;
		this.timer = new ContinuousTimer(this, GameSettings.turnDuration, 0, 
				actorHandler);
		
		// Adds the object to the handler(s)
		if (testHandler != null)
			testHandler.addTestable(this);
		if (room != null)
			room.addRoomListener(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies
		kill();
	}

	@Override
	public void startTesting()
	{
		// Resets the timer
		this.timer.reset();
	}

	@Override
	public void endTesting()
	{
		// Does nothing
	}

	@Override
	public boolean isActive()
	{
		return this.active;
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
	public void onTimerEvent(int timerid)
	{
		// Informs the objects about a turn
		onTurnEvent();
	}
	
	@Override
	public void kill()
	{
		// Also kills the timer
		this.timer.kill();
		super.kill();
	}
}
