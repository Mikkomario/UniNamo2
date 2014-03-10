package utopia_handlers;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import utopia_handleds.Actor;
import utopia_handleds.Collidable;
import utopia_handleds.Handled;
import utopia_listeners.CollisionListener;

/**
 * A handler that checks collisions between multiple collisionlisteners and 
 * Collidables
 *
 * @author Mikko Hilpinen.
 *         Created 18.6.2013.
 */
public class CollisionHandler extends LogicalHandler implements Actor
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private CollidableHandler collidablehandler;
	private double lastStepLength;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty collisionhandler
	 *
	 * @param autodeath Will the handler die when it runs out of listeners
	 * @param superhandler Which actorhandler will inform the handler about 
	 * the act-event (Optional)
	 */
	public CollisionHandler(boolean autodeath, ActorHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.collidablehandler = new CollidableHandler(false, null);
		this.lastStepLength = 1;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public void act(double steps)
	{
		// Handles the objects normally = checks collisions
		this.lastStepLength = steps;
		handleObjects();
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return CollisionListener.class;
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		CollisionListener listener = (CollisionListener) h;
		
		// Inactive listeners are not counted
		if (!listener.isActive())
			return true;
		
		Point2D.Double[] colpoints = listener.getCollisionPoints();
		
		// If collision points could not be read, skips the object
		if (colpoints == null)
		{
			System.err.println("CollisionListener " + h + 
					" doesn't provide viable collision points.");
			return true;
		}
		
		HashMap<Collidable, ArrayList<Point2D.Double>> collidedpoints = 
				new HashMap<Collidable, ArrayList<Point2D.Double>>();
		
		// Goes throug each point and checks collided objects
		for (Point2D.Double colpoint : colpoints)
		{
			ArrayList<Collidable> collided = 
					this.collidablehandler.getCollidedObjectsAtPoint(colpoint, 
							listener);
			// If no collisions were detected, moves on
			if (collided == null)
				continue;
			
			// If collisions were detected, adds them to the map
			for (Collidable c : collided)
			{
				// Objects can't collide with themselves
				if (c.equals(listener))
					continue;
				
				if (!collidedpoints.containsKey(c))
					collidedpoints.put(c, new ArrayList<Point2D.Double>());
				
				collidedpoints.get(c).add(colpoint);
			}
		}
		
		// Informs the listener about each object it collided with
		for (Collidable c: collidedpoints.keySet())
			listener.onCollision(collidedpoints.get(c), c, this.lastStepLength);
		
		return true;
	}
	
	@Override
	public void kill()
	{
		// In addition to the normal killing, kills the collidablehandler as well
		getCollidableHandler().kill();
		super.kill();
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The collidablehandler that handles the collisionhandler's 
	 * collision checking
	 */
	public CollidableHandler getCollidableHandler()
	{
		return this.collidablehandler;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds a new collisionlistener to the checked listeners
	 *
	 * @param c The new collisionlistener
	 */
	public void addCollisionListener(CollisionListener c)
	{
		super.addHandled(c);
	}
	
	/**
	 * Adds a new collidable to the list of checkked collidables
	 *
	 * @param c The collidable to be added
	 */
	public void addCollidable(Collidable c)
	{
		this.collidablehandler.addCollidable(c);
	}
}
