package uninamo_gameplaysupport;

import genesis_logic.ActorHandler;
import omega_world.Room;
import omega_world.RoomListener;
import timers.ContinuousTimer;
import timers.TimerEventListener;
import uninamo_main.GameSettings;

/**
 * The turnTimer informs multiple objects about the pass of turns. The timer 
 * continues to inform the objects until the room ends
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class TurnTimer extends TurnHandler implements RoomListener, TestListener, 
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
		killWithoutKillingHandleds();
	}

	@Override
	public void onTestStart()
	{
		// Resets the timer
		this.timer.reset();
	}

	@Override
	public void onTestEnd()
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
	public void killWithoutKillingHandleds()
	{
		// Also kills the timer
		this.timer.kill();
		super.killWithoutKillingHandleds();
	}
}
