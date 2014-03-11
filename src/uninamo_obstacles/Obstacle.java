package uninamo_obstacles;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.Testable;
import uninamo_gameplaysupport.Wall;
import utopia_gameobjects.BouncingBasicPhysicDrawnObject;
import utopia_graphic.MultiSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_handleds.Collidable;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_helpAndEnums.HelpMath;
import utopia_helpAndEnums.Movement;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * obstacles are the main 'objects' in the game in a sense that the user is 
 * trying to affect them. Obstacles interact with some of the machinery but 
 * are more often the passive part in the interaction.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public abstract class Obstacle extends BouncingBasicPhysicDrawnObject implements 
	RoomListener, Testable
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private MultiSpriteDrawer spritedrawer;
	private boolean started;
	private Point2D.Double startPosition;
	//private static final Class<?> COLLIDEDCLASSES[] = {Machine.class, Obstacle.class};
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 *  Creates a new obstacle to the given position. Remember to set up 
	 *  the collision points after creating the object.
	 *  
	 * @param x The x-coordinate of the obstacle (pixels)
	 * @param y The y-coordinate of the obstacle (pixels)
	 * @param isSolid Can the obstacle be collided with
	 * @param collisiontype What is the shape of the obstacle
	 * @param drawer The drawableHandler that will draw the obstacle
	 * @param collidablehandler The collidableHandler that will handle the 
	 * obstacle's collision checking
	 * @param collisionhandler The collisionHandler that will inform the 
	 * object about collision events (optional)
	 * @param actorhandler The actorHandler that will inform the object about 
	 * step events
	 * @param room The room where the obstacle resides at
	 * @param testHandler The testHandler that will inform the obstacle about test events
	 * @param designSpriteName The name of the sprite used to draw the object 
	 * in the design mode
	 * @param realSpriteName The name of the sprite used to draw the object 
	 * during the test mode 
	 */
	public Obstacle(int x, int y, boolean isSolid,
			CollisionType collisiontype, DrawableHandler drawer,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler, 
			Room room, TestHandler testHandler, String designSpriteName, 
			String realSpriteName)
	{
		super(x, y, DepthConstants.NORMAL, isSolid, collisiontype, drawer, 
				collidablehandler, collisionhandler, actorhandler);
		
		// Initializes attributes
		Sprite[] sprites = new Sprite[2];
		sprites[0] = MultiMediaHolder.getSpriteBank("obstacles").getSprite(
				designSpriteName);
		sprites[1] = MultiMediaHolder.getSpriteBank("obstacles").getSprite(
				realSpriteName);
		this.spritedrawer = new MultiSpriteDrawer(sprites, actorhandler, this);
		this.started = false;
		this.startPosition = new Point2D.Double(x, y);
		
		// Adds the object to the handler(s)
		if (room != null)
			room.addObject(this);
		if (testHandler != null)
			testHandler.addTestable(this);
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * Resets the status of the object back to the original. The position 
	 * of the object need not be reseted since it is done automatically.
	 */
	protected abstract void resetStatus();
	
	/**
	 * This method is called when the obstacle collides with an object it 
	 * doesn't know how to react to. This object is not a wall.
	 * @param colpoints A list of points where the collision happened
	 * @param collided The collided object
	 * @param steps How long the collision took place (in steps)
	 */
	protected abstract void onSpecialCollision(ArrayList<Double> colpoints, 
			Collidable collided, double steps);
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onCollision(ArrayList<Double> colpoints, Collidable collided, 
			double steps)
	{
		// if the collided object is a wall, bounces away from it
		if (collided instanceof Wall)
		{
			Wall wall = (Wall) collided;
			
			//System.out.println(getMovement().getVSpeed());
			
			// TODO: Munch these numbers further if need be
			bounceWithoutRotationFrom(wall, HelpMath.getAveragePoint(colpoints), 
					0, 0.25, 1, steps);
		}
	}

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// Doesn't limit colliding classes
		return null;//COLLIDEDCLASSES;
	}

	@Override
	public int getWidth()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getWidth();
	}

	@Override
	public int getHeight()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getHeight();
	}

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the sprite
		if (this.spritedrawer == null)
			return;
		
		this.spritedrawer.drawSprite(g2d, 0, 0);
		
		drawCollisionArea(g2d);
		drawCollisionPoints(g2d);
	}

	@Override
	public void act(double steps)
	{
		// Adds gravity
		addMotion(270, 0.75 * steps);
		
		super.act(steps);
	}
	
	@Override
	public boolean isActive()
	{
		// Only started obstacles are active
		return super.isActive() && this.started;
	}
	
	@Override
	public boolean isSolid()
	{
		// Only started obstacles can be collided with
		return super.isSolid() && this.started;
	}
	
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
	
	/**
	 * The object should go into test mode in a reseted state
	 */
	@Override
	public void startTesting()
	{
		// Changes the object's graphics and starts it
		getSpriteDrawer().setSpriteIndex(1, false);
		this.started = true;
	}
	
	/**
	 * The object should return from the test mode
	 */
	@Override
	public void endTesting()
	{
		// Returns back to the original position and sprite
		this.started = false;
		getSpriteDrawer().setSpriteIndex(0, false);
		setPosition(this.startPosition);
		setMovement(new Movement(0, 0));
		resetStatus();
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The spriteDrawer used to draw the object
	 */
	protected MultiSpriteDrawer getSpriteDrawer()
	{
		return this.spritedrawer;
	}
}
