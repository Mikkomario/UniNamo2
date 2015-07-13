package uninamo_obstacles;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import exodus_world.Area;
import exodus_world.AreaListener;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import omega_util.Transformable;
import omega_util.Transformation;
import uninamo_gameplaysupport.TestEvent;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TestListener;
import uninamo_gameplaysupport.Wall;
import vision_sprite.MultiSpriteDrawer;

/**
 * obstacles are the main 'objects' in the game in a sense that the user is 
 * trying to affect them. Obstacles interact with some of the machinery but 
 * are more often the passive part in the interaction.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public abstract class Obstacle extends SimpleGameObject implements 
	AreaListener, TestListener, Drawable, Transformable
{
	// TODO: Any collision systems, as well as any physics have been (temporarily) removed
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private MultiSpriteDrawer spritedrawer;
	private boolean started;
	private Vector3D startPosition;
	//private static final Class<?> COLLIDEDCLASSES[] = {Machine.class, Obstacle.class};
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 *  Creates a new obstacle to the given position. Remember to set up 
	 *  the collision points after creating the object.
	 * @param designSpriteName The name of the sprite used to draw the object 
	 * in the design mode
	 * @param realSpriteName The name of the sprite used to draw the object 
	 * during the test mode 
	 */
	public Obstacle(HandlerRelay handlers, Vector3D position, 
			String designSpriteName, String realSpriteName)
	{
		super(x, y, DepthConstants.NORMAL, isSolid, collisiontype, area);
		
		// Initializes attributes
		Sprite[] sprites = new Sprite[2];
		sprites[0] = OpenSpriteBank.getSpriteBank("obstacles").getSprite(
				designSpriteName);
		sprites[1] = OpenSpriteBank.getSpriteBank("obstacles").getSprite(
				realSpriteName);
		this.spritedrawer = new MultiSpriteDrawer(sprites, area.getActorHandler(), this);
		this.started = false;
		this.startPosition = new Point2D.Double(x, y);
		
		setupRotationOrigin();
		
		// Adds the object to the handler(s)
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
			// If the collided object also is an advancedPhysicDrawnObject, uses 
			// a more "sophisticated" collision method
			if (collided instanceof AdvancedPhysicDrawnObject)
			{
				collideWith((AdvancedPhysicDrawnObject) collided, 
						colpoints, 0.4, 0, steps);
			}
			else
			{
				// TODO: Munch these numbers further if need be
				titaniumCollision((Wall) collided, colpoints, 0.5, steps);
			}
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
		super.act(steps);
		
		// Adds gravity
		// G = mg
		addImpulse(Movement.createMovement(270, 2 * getMass()), 
				getPosition(), steps);
		//scale(1.01, 1.01);
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
	public void onTestStart()
	{
		// Changes the object's graphics and starts it
		getSpriteDrawer().setSpriteIndex(1, false);
		this.started = true;
	}
	
	/**
	 * The object should return from the test mode
	 */
	@Override
	public void onTestEnd()
	{
		// Returns back to the original position and sprite
		this.started = false;
		getSpriteDrawer().setSpriteIndex(0, false);
		setPosition(this.startPosition);
		setMovement(new Movement(0, 0));
		makeSolid();
		setVisible();
		activate();
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


	@Override
	public void onTestEvent(TestEvent event)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAreaStateChange(Area area)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public Transformation getTransformation()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setTrasformation(Transformation arg0)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getDepth()
	{
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
