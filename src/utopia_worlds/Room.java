package utopia_worlds;

import java.util.ArrayList;

import utopia_backgrounds.Background;
import utopia_gameobjects.GameObject;
import utopia_handleds.Drawable;
import utopia_handleds.Handled;
import utopia_handleds.LogicalHandled;
import utopia_handlers.Handler;
import utopia_handlers.RoomListenerHandler;
import utopia_listeners.RoomListener;



/**
 * Room represents a single restricted area in a game. A room contains a 
 * background and a group of objects. A room can start 
 * and end and it will inform objects about such events.<p>
 * 
 * A room has to be initialized before it is used but can also be uninitialized 
 * to save memory when it is not needed.
 *
 * @author Mikko Hilpinen.
 *         Created 11.7.2013.
 */
public class Room extends Handler
{	
	// ATTRIBUTES	-----------------------------------------------------
	
	private ArrayList<Background> backgrounds;
	private RoomListenerHandler listenerhandler;
	private boolean active, isinitializing;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new room, filled with backgrounds, and objects. 
	 * The room will remain inactive until started.
	 *
	 * @param backgrounds The background(s) used in the room. Use empty list or 
	 * null if no backgrounds will be used.
	 * @see #end()
	 * @see #start()
	 */
	public Room(ArrayList<Background> backgrounds)
	{
		// Rooms aren't handled by anything by default
		super(false, null);
		
		// Initializes attributes
		this.backgrounds = backgrounds;
		this.active = true;
		this.listenerhandler = new RoomListenerHandler(false, null);
		this.isinitializing = false;
		
		// Uninitializes the room
		uninitialize();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return GameObject.class;
	}
	
	@Override
	public void killWithoutKillingHandleds()
	{
		// In addition to the normal killing process, kills the 
		// backgrounds as well
		if (this.backgrounds != null)
		{
			for (int i = 0; i < this.backgrounds.size(); i++)
				this.backgrounds.get(i).kill();
			
			this.backgrounds.clear();
			this.backgrounds = null;
		}
		
		super.killWithoutKillingHandleds();
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * Changes the backgrounds shown in the room
	 *
	 * @param backgrounds The new background(s) shown in the room (null if 
	 * no background is used in the room)
	 */
	public void setBackgrounds(ArrayList<Background> backgrounds)
	{
		this.backgrounds = backgrounds;
	}
	
	/**
	 * @return The current background(s) shown in the room
	 */
	public ArrayList<Background> getBackgrounds()
	{
		return this.backgrounds;
	}
	
	/**
	 * @return Is the room currently in use.
	 */
	public boolean isActive()
	{
		return this.active && !isDead();
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		// Either initializes or uninitializes the object (if possible)
		if (h instanceof Drawable)
		{
			Drawable d = (Drawable) h;
			
			if (this.isinitializing)
				d.setVisible();
			else
				d.setInvisible();
		}
		if (h instanceof LogicalHandled)
		{
			LogicalHandled l = (LogicalHandled) h;
			
			if (this.isinitializing)
				l.activate();
			else
				l.inactivate();
		}
		
		return true;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	
	/**
	 * Creates a room that has a single background in it
	 *
	 * @param background The background the room will have
	 * @return A new room with the background
	 */
	public static Room createSimpleRoom(Background background)
	{
		ArrayList<Background> backs = new ArrayList<Background>();
		backs.add(background);
		
		return new Room(backs);
	}
	
	/**
	 * Adds a new object to the room. If the object is an (active) roomlistener, 
	 * it will be automatically informed about the events in the room.
	 *
	 * @param g The object to be added
	 */
	public void addObject(GameObject g)
	{
		addHandled(g);
		// If the object is a roomlistener, adds it to the listenerhandler as well
		if (g instanceof RoomListener)
		{
			//System.out.println("Added " + g + " as an room listener");
			this.listenerhandler.addRoomListener((RoomListener) g);
		}
	}
	
	/**
	 * Adds an object to be informed about room events
	 *
	 * @param l The listener that will be informed about room events
	 */
	public void addRoomListener(RoomListener l)
	{
		this.listenerhandler.addRoomListener(l);
	}
	
	/**
	 * Removes a gameobject from the room
	 *
	 * @param g The gameobject to be removed
	 */
	public void removeObject(GameObject g)
	{
		removeHandled(g);
		// If the object was a roomlistener, removes it from there as well
		if (g instanceof RoomListener)
			this.listenerhandler.removeHandled(g);
	}
	
	/**
	 * Starts the room, activating all the objects and backgrounds in it
	 * 
	 * @see #end()
	 */
	public void start()
	{
		// If the room had already been started, nothing happens
		if (this.active)
			return;
		
		this.active = true;
		initialize();
		
		// Informs the listeners about the event
		this.listenerhandler.onRoomStart(this);
	}
	
	/**
	 * Ends the room, deactivating all the objects and backgrounds in it
	 * 
	 * @see #start()
	 */
	public void end()
	{
		// If the room had already been ended, nothing happens
		if (!this.active)
			return;
		
		// Informs the listeners about the event
		//System.out.println("informs about room end");
		this.listenerhandler.onRoomEnd(this);
		
		this.active = false;
		uninitialize();
	}
	
	/**
	 * Here the room (re)initializes all its content
	 * 
	 * @see #uninitialize()
	 */
	protected void initialize()
	{
		this.active = true;
		//System.out.println("initializes the room");
		// Sets the backgrounds visible and animated
		if (this.backgrounds != null)
		{
			for (int i = 0; i < this.backgrounds.size(); i++)
			{
				Background b = this.backgrounds.get(i);
				b.setVisible();
				b.getSpriteDrawer().activate();
			}
		}
		// Activates all the objects and sets them visible (if applicable)
		this.isinitializing = true;
		handleObjects();
	}
	
	/**
	 * Here the room uninitializes its content
	 * 
	 * @see #initialize()
	 */
	protected void uninitialize()
	{
		this.active = false;
		// Sets the backgrounds invisible and unanimated
		if (this.backgrounds != null)
		{
			for (int i = 0; i < this.backgrounds.size(); i++)
			{
				Background b = this.backgrounds.get(i);
				b.setInvisible();
				
				if (b.getSpriteDrawer() != null)
					b.getSpriteDrawer().inactivate();
			}
		}
		// InActivates all the objects and sets them invisible (if applicable)
		this.isinitializing = false;
		handleObjects();
	}
}
