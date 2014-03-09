package utopia_handlers;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import utopia_handleds.Collidable;
import utopia_handleds.Handled;
import utopia_listeners.CollisionListener;

/**
 * This class handles multiple collidables. Any collision checks made for this 
 * object are made for all the collidables.
 *
 * @author Mikko Hilpinen.
 *         Created 18.6.2013.
 */
public class CollidableHandler extends Handler
{	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty collidablehandler
	 *
	 * @param autodeath Will the handler die if it runs out of handleds
	 * @param superhandler The collidablehandler that holds the handler
	 */
	public CollidableHandler(boolean autodeath, CollidableHandler superhandler)
	{
		super(autodeath, superhandler);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return Collidable.class;
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		// All handling is done via operators
		return false;
	}
	
	
	// OTHER METHODS	-----------------------------------------------------
	
	/**
	 * Adds a new collidable to the list of collidables
	 *
	 * @param c The collidable to be added
	 */
	public void addCollidable(Collidable c)
	{
		addHandled(c);
	}

	/**
	 * Returns all the collided objects the collidableHandler handles that 
	 * collide with the given point
	 *
	 * @param collisionPoint The point where the collisions are checked
	 * @param listener The collisionListener listening to the given position. 
	 * The value is only used for checking if the collided object supports the 
	 * listener and no changes are made to the object. Use null if you 
	 * specifically want to skip all support checks.
	 * @return A list of objects colliding with the point or null if no object 
	 * collided with the point
	 */
	public ArrayList<Collidable> getCollidedObjectsAtPoint(Point2D collisionPoint, 
			CollisionListener listener)
	{
		// Initializes the operator
		CollisionCheckOperator checkoperator = 
				new CollisionCheckOperator(collisionPoint, listener);
		
		// Checks collisions through all collidables
		handleObjects(checkoperator);
		
		// If there wasn't collided objects, forgets the data and returns null
		if (checkoperator.getCollidedObjects() == null)
			return null;
		
		// Returns a list of collided objects (no need for cloning since 
		// operator is not an attribute)
		return checkoperator.getCollidedObjects();
	}
	
	/**
	 * Goes through the collidables and makes them solid
	 */
	public void makeCollidablesSolid()
	{
		// Tries to make all of the collidables solid
		handleObjects(new MakeSolidOperator());
	}

	/**
	 * Goes through the collidables and makes the unsolid
	 */
	public void makeCollidablesUnsolid()
	{
		// Tries to make all of the collidables solid
		handleObjects(new MakeUnsolidOperator());
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class MakeSolidOperator extends HandlingOperator
	{
		@Override
		protected boolean handleObject(Handled h)
		{
			((Collidable) h).makeSolid();
			return true;
		}
	}
	
	private class MakeUnsolidOperator extends HandlingOperator
	{
		@Override
		protected boolean handleObject(Handled h)
		{
			((Collidable) h).makeUnsolid();
			return true;
		}	
	}
	
	private class CollisionCheckOperator extends HandlingOperator
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private ArrayList<Collidable> collided;
		private Point2D checkPosition;
		private CollisionListener listener;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		// Listener is not changed in any way during the process
		public CollisionCheckOperator(Point2D checkPosition, 
				CollisionListener listener)
		{
			this.checkPosition = checkPosition;
			this.collided = new ArrayList<Collidable>();
			this.listener = listener;
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------
		
		@Override
		protected boolean handleObject(Handled h)
		{	
			Collidable c = (Collidable) h;
			
			// Non-solid objects can't collide
			if (!c.isSolid())
				return true;
			
			// Checks if the listener's class is supported
			Class<?>[] supportedclasses = c.getSupportedListenerClasses();
			boolean listenerissupported = true;
			// Null supported classes means that the collidable doesn't want 
			// support to be checked.
			// Null listener means that the listener doesn't want support to 
			// be checked
			if (supportedclasses != null && this.listener != null)
			{
				// Otherwise tries to find the listener's class from the list
				listenerissupported = false;
				
				for (int i = 0; i < supportedclasses.length; i++)
				{
					if (supportedclasses[i].isInstance(this.listener))
					{
						listenerissupported = true;
						break;
					}
				}
			}
			// Unsupported classes are ignored
			if (!listenerissupported)
				return true;
			
			// Checks the collision
			if (!c.pointCollides(this.checkPosition))
				return true;
				
			// Adds the collided object to the list
			this.collided.add(c);
			
			// Goes through all the objects
			return true;
		}
		
		
		// GETTERS & SETTERS	-----------------------------------------
		
		public ArrayList<Collidable> getCollidedObjects()
		{
			return this.collided;
		}
	}
}
