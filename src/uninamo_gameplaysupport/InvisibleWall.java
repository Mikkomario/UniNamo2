package uninamo_gameplaysupport;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import utopia_gameobjects.GameObject;
import utopia_handlers.CollidableHandler;
import utopia_listeners.RoomListener;
import utopia_worlds.Room;

/**
 * Invisible walls are walls that are not drawn but can be collided with. 
 * They span the whole length of the screen either horizontally or vertically. 
 * The wall dies when the room ends
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class InvisibleWall extends GameObject implements Wall, RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int yForceModifier, xForceModifier;
	private int position;
	private boolean solid;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new invisibleWall to the given position with the given form.
	 * 
	 * @param verticalForce Will the wall apply vertical force and to which 
	 * direction (0 = no vertical force, 1 = forces down, -1 = forces up)
	 * @param horizontalForce Will the wall apply horizontal force and to which 
	 * direction (0 = no horizontal force, 1 = forces right, -1 = forces left). 
	 * HorizontalForce will be forced to 0 if there's already vertical force 
	 * applied.
	 * @param position the wall's position on either y- or x-axis depending on 
	 * the applied force's axis (y if vertical, x if horizontal)
	 * @param collidableHandler The CollidableHandler that handles the object's 
	 * collision detection
	 * @param room The room where the wall will reside at
	 */
	public InvisibleWall(int verticalForce, int horizontalForce, int position, 
			CollidableHandler collidableHandler, Room room)
	{
		// Initializes attributes
		this.yForceModifier = verticalForce;
		this.xForceModifier = horizontalForce;
		this.position = position;
		this.solid = true;
		
		// Checks the arguments
		if (this.yForceModifier == 0 && this.xForceModifier == 0)
		{
			System.err.println("The wall has no proportions or force and will "
					+ "be killed");
			kill();
			return;
		}
		if (this.yForceModifier != 0 && this.xForceModifier != 0)
			this.xForceModifier = 0;
		
		// Adds the object to the handler(s)
		if (collidableHandler != null)
			collidableHandler.addCollidable(this);
		if (room != null)
			room.addObject(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public double getCollisionForceDirection(Double collisionpoint)
	{
		if (this.xForceModifier == 0)
		{
			if (this.yForceModifier > 0)
				return 270;
			else
				return 90;
		}
		else
		{
			if (this.xForceModifier > 0)
				return 0;
			else
				return 180;
		}
	}

	@Override
	public boolean pointCollides(Point2D absolutepoint)
	{
		if (this.xForceModifier == 0)
		{
			if (this.yForceModifier > 0)
				return (absolutepoint.getY() < this.position);
			else
				return (absolutepoint.getY() > this.position);
		}
		else
		{
			if (this.xForceModifier > 0)
				return (absolutepoint.getX() < this.position);
			else
				return (absolutepoint.getX() > this.position);
		}
	}

	@Override
	public boolean isSolid()
	{
		return this.solid;
	}

	@Override
	public void makeSolid()
	{
		this.solid = true;
	}

	@Override
	public void makeUnsolid()
	{
		this.solid = false;
	}

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		return null;
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
}
