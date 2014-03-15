package utopia_camera;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import utopia_gameobjects.BasicPhysicDrawnObject;
import utopia_gameobjects.CollidingDrawnObject;
import utopia_gameobjects.DimensionalDrawnObject;
import utopia_gameobjects.DrawnObject;
import utopia_handleds.Collidable;
import utopia_handleds.Drawable;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_helpAndEnums.HelpMath;

/**
 * This object acts as the camera of the game, drawing multiple elements from the 
 * world into a smaller screen
 *
 * @author Mikko Hilpinen.
 *         Created 16.6.2013.
 */
public class BasicCamera extends BasicPhysicDrawnObject
{
	// ATTRIBUTES	------------------------------------------------------
	
	private CameraDrawer drawer;
	private int screenWidth, screenHeight;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new basic camera added to the given handlers and starting 
	 * from the given position
	 *
	 * @param x The camera's new x-coordinate
	 * @param y The camera's new y-coordinate
	 * @param drawer The drawablehandler that will draw the camera and objects 
	 * it shows
	 * @param actorhandler The actorhandler that informs the camera about 
	 * the act-event
	 * @param screenWidth The width of the screen
	 * @param screenHeight The height of the screen
	 * @param depthLayers How many layers of depth handling there should be. 
	 * The less the content's depth changes, the more there should be. [1, 6]
	 */
	public BasicCamera(int x, int y, int screenWidth, int screenHeight, 
			int depthLayers, DrawableHandler drawer, ActorHandler actorhandler)
	{
		super(x, y, DepthConstants.BACK, false, CollisionType.BOX, drawer, 
				null, null, actorhandler);
		
		// Initializes attributes
		this.drawer =  new CameraDrawer(false, depthLayers, this);
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public int getOriginX()
	{
		return this.screenWidth / 2;
	}

	@Override
	public int getOriginY()
	{
		return this.screenHeight / 2;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		if (this.drawer != null)
			this.drawer.drawSelf(g2d);
	}
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Uses transformations that are opposite to the 
		// usual transformations
		AffineTransform trans = g2d.getTransform();
		
		// Translates the origin to the right position
		g2d.translate(getOriginX(), getOriginY());
		// scales it depending on it's xscale and yscale
		g2d.scale(1/getXScale(), 1/getYScale());
		// rotates it depending on its angle
		g2d.rotate(Math.toRadians((getAngle())));
		// Translates the sprite to the object's position
		g2d.translate(-getX(), -getY());
		
		// Adds the opposing transformations
		// TODO: Did not work, try to find another way to do this
		//g2d.transform(getOpposingTransform());
		
		// Finally draws the object
		drawSelfBasic(g2d);
		
		// Loads the previous transformation
		g2d.setTransform(trans);
		//drawSelfAsContainer(g2d);
	}
	
	@Override
	public int getWidth()
	{
		return this.screenWidth;
	}

	@Override
	public int getHeight()
	{
		return this.screenHeight;
	}
	
	@Override
	public void onCollision(ArrayList<Point2D.Double> collisionpoints, 
			Collidable collided, double steps)
	{
		// Doesn't do anything upon collision
	}
	
	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// Camera doesn't limit its collided objects in any way
		return null;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds an drawable to the drawables the camera draws
	 *
	 * @param drawable The object the camera will draw
	 */
	public void addDrawable(Drawable drawable)
	{
		this.drawer.addDrawable(drawable);
	}
	
	/**
	 * @return The drawablehandler used to draw the contents of the camera. 
	 * Notice that the drawer only supports drawnObjects and not drawableHandlers
	 */	
	public CameraDrawer getDrawer()
	{
		return this.drawer;
	}
	
	/**
	 * Tells whether an object should be drawn on the camera or not
	 *
	 * @param d The object that may be drawn
	 * @return Should the object be drawn
	 */
	protected boolean objectShouldBeDrawn(DrawnObject d)
	{
		// If the drawnobject is collidingdrawnobject, checks the drawing more 
		// carefully
		if (d instanceof CollidingDrawnObject)
		{
			CollidingDrawnObject cd = (CollidingDrawnObject) d;
			
			Point2D.Double[] collisionpoints = cd.getCollisionPoints();
			
			// Does NOT check if the object is solid or not! (used for drawing 
			// so the visible-status is used instead)
			// Invisible objects are never drawn
			if (!cd.isVisible())
				return false;
						
			// Returns true if any of the collisionpoints collides
			for (int i = 0; i < collisionpoints.length; i++)
			{
				if (pointCollides(collisionpoints[i]))
					return true;
			}
			
			return false;
		}
		// Dimensionalobjects are drawn if they are near the camera
		// Draws a bit more objects than necessary
		else if (d instanceof DimensionalDrawnObject)
		{
			DimensionalDrawnObject dd = (DimensionalDrawnObject) d;
			
			// Checks if it's possible that any point of the object would be shown
			double maxrange = dd.getMaxRangeFromOrigin() + getMaxRangeFromOrigin();
			
			return HelpMath.pointDistance(getX(), getY(), dd.getX(), dd.getY()) 
					<= maxrange;
		}
		// Other objects are always drawn
		return true;
	}
}
