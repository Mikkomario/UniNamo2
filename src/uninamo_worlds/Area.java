package uninamo_worlds;

import java.util.ArrayList;

import utopia_backgrounds.Background;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_resourceHandling.GamePhase;
import utopia_resourceHandling.ResourceActivator;
import utopia_worlds.Room;

/**
 * Areas are certain states or places in the program. Areas can be disabled or 
 * activated in different ways. For example, area may be left active but 
 * without mouse handling. Otherwise they act like normal Rooms
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class Area extends Room
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private MouseListenerHandler mousehandler;
	private ActorHandler actorhandler;
	private DrawableHandler drawer;
	private CollisionHandler collisionhandler;
	private GamePhase phase;
	private boolean drawDisabled, mouseDisabled;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new area with separate handler systems. The area will remain 
	 * inactive until started.
	 * 
	 * @param phase The GamePhase during which the area is active
	 * @param superMouseHandler The MouseListenerHandler that the area's handlers will use
	 * @param superActorHandler The ActorHandler that the area's handlers will use
	 * @param superDrawer The DrawableHandler that the area's handlers will use
	 */
	public Area(GamePhase phase, MouseListenerHandler superMouseHandler, 
			ActorHandler superActorHandler, DrawableHandler superDrawer)
	{
		super(new ArrayList<Background>());
		
		// Initializes attributes
		this.phase = phase;
		this.mousehandler = new MouseListenerHandler(false, superActorHandler, 
				superMouseHandler);
		this.actorhandler = new ActorHandler(false, superActorHandler);
		this.drawer = new DrawableHandler(false, true, DepthConstants.NORMAL, 5, 
				superDrawer);
		this.collisionhandler = new CollisionHandler(false, superActorHandler);
		this.drawDisabled = false;
		this.mouseDisabled = false;
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public void kill()
	{
		// Also kills the handlers
		this.mousehandler.kill();
		this.actorhandler.kill();
		this.drawer.kill();
		this.collisionhandler.kill();
		
		super.kill();
	}
	
	@Override
	protected void initialize()
	{
		// Area makes sure the correct phase is active before initialization
		ResourceActivator.startPhase(this.phase);
		
		super.initialize();
	}
	
	
	// GETTERS & SETTERS	-----------------------------------------------
	
	/**
	 * @return The mouseListenerHandler used in the area
	 */
	public MouseListenerHandler getMouseHandler()
	{
		return this.mousehandler;
	}
	
	/**
	 * @return The actorHandler used in the area
	 */
	public ActorHandler getActorHandler()
	{
		return this.actorhandler;
	}
	
	/**
	 * @return The drawableHandler used in the area
	 */
	public DrawableHandler getDrawer()
	{
		return this.drawer;
	}
	
	/**
	 * @return The collisionHandler used in the area
	 */
	public CollisionHandler getCollisionHandler()
	{
		return this.collisionhandler;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Ends all disables put on the area, making it work normally again
	 */
	public void returnNormal()
	{
		if (this.mouseDisabled)
			getMouseHandler().activate();
		if (this.drawDisabled)
			getDrawer().setVisible();
		
		this.mouseDisabled = false;
		this.drawDisabled = false;
	}
	
	/**
	 * Disables the mouse from working in the area
	 */
	public void disableOnlyMouse()
	{
		returnNormal();
		getMouseHandler().inactivate();
		this.mouseDisabled = true;
	}
	
	/**
	 * Disables mouse and drawing but leaves actors and logical systems working
	 */
	public void disableMouseAndDrawing()
	{
		returnNormal();
		
		getMouseHandler().inactivate();
		getDrawer().setInvisible();
		
		this.drawDisabled = true;
		this.mouseDisabled = true;
	}
}
