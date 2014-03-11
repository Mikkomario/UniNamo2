package utopia_gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import utopia_handleds.PhysicalCollidable;
import utopia_handlers.CollidableHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.HelpMath;

/**
 * This is a subclass of the drawnobject that can be used in collisionchecking 
 * and in situations that require the object to have dimensions
 *
 * @author Mikko Hilpinen.
 *         Created 30.6.2013.
 */
public abstract class DimensionalDrawnObject extends DrawnObject implements PhysicalCollidable
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean solid;
	private CollisionType collisiontype;
	private int radius;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new dimensionalobject with the given information
	 *
	 * @param x The x-coordinate of the object's position
	 * @param y The y-coordinate of the object's position
	 * @param depth How 'deep' the object is drawn
	 * @param isSolid Is the object solid. In other words, can the object be 
	 * collided with
	 * @param collisiontype What kind of shape the object is collisionwise
	 * @param drawer The drawablehandler that draws the object (optional)
	 * @param collidablehandler The collidablehandler that will handle the 
	 * object's collision checking (optional)
	 */
	public DimensionalDrawnObject(int x, int y, int depth, boolean isSolid, 
			CollisionType collisiontype, DrawableHandler drawer, 
			CollidableHandler collidablehandler)
	{
		super(x, y, depth, drawer);
		
		// Initializes attributes
		this.solid = isSolid;
		this.collisiontype = collisiontype;
		// Negative radius means uninitialized (needs to be done later since it 
		// uses width and height)
		this.radius = -1;
		
		// Adds the object to the handler, if possible
		if (collidablehandler != null)
			collidablehandler.addCollidable(this);
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * @return The width of the object (doesn't include scaling)
	 */
	public abstract int getWidth();
	
	/**
	 * @return The height of the object (doesn't include scaling)
	 */
	public abstract int getHeight();
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
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
	public boolean pointCollides(Point2D absolutepoint)
	{
		// Doesn't do more specific checks if the point is too far from 
		// the origin of the object
		if (HelpMath.pointDistance(getX(), getY(), absolutepoint.getX(), 
				absolutepoint.getY()) > getMaxRangeFromOrigin())
			return false;
		
		// Negates the transformation
		Point2D.Double negatedPoint = negateTransformations(absolutepoint);
		
		// Returns the object if it collides with the point
		// Circular objects react if the point is near enough
		if (this.collisiontype == CollisionType.CIRCLE)
		{
			return (HelpMath.pointDistance(getOriginX(), getOriginY(), 
					negatedPoint.x, negatedPoint.y) <= getRadius());
		}
		// Boxes collide if the point is within them
		else if (this.collisiontype == CollisionType.BOX)
		{
			return (HelpMath.pointIsInRange(negatedPoint, 0, 
					getWidth(), 0, getHeight()));
		}
		// Walls collide if the point is on their left side (relatively)
		else
			return (negatedPoint.x < 0);
	}
	
	@Override
	public double getCollisionForceDirection(Point2D.Double collisionpoint)
	{
		// Circles simply push the object away
		if (this.collisiontype == CollisionType.CIRCLE)
			return HelpMath.pointDirection(getX(), getY(), 
					collisionpoint.x, collisionpoint.y);
		
		// Walls simply push the object to the right (relative)
		else if (this.collisiontype == CollisionType.WALL)
			return getAngle();
		
		// Boxes are the most complicated
		else if (this.collisiontype == CollisionType.BOX)
		{
			// Calculates the side which the object touches
			Point2D.Double relativepoint = negateTransformations(collisionpoint);
			double relxdiffer = -0.5 + relativepoint.x / getWidth();
			double relydiffer = -0.5 + relativepoint.y / getHeight();
			
			// Returns drection of one of the sides of the object
			if (Math.abs(relxdiffer) == Math.abs(relydiffer))
			{
				System.out.println("Secret collision");
				
				if (relxdiffer >= 0 && relydiffer >= 0)
					return HelpMath.checkDirection(getAngle() + 45);
				else if (relxdiffer <= 0 && relydiffer >= 0)
					return HelpMath.checkDirection(getAngle() + 135);
				else if (relxdiffer <= 0 && relydiffer <= 0)
					return HelpMath.checkDirection(getAngle() + 225);
				else
					return HelpMath.checkDirection(getAngle() + 315);
			}
			else if (Math.abs(relxdiffer) > Math.abs(relydiffer))
			{
				if (relxdiffer >= 0)
					return getAngle();
				else
					return HelpMath.checkDirection(getAngle() + 180);
			}
			else
			{
				if (relydiffer >= 0)
					return HelpMath.checkDirection(getAngle() + 270);
				else
					return HelpMath.checkDirection(getAngle() + 90);
			}
		}
		
		// In case one of these types wasn't the case, returns 0
		return 0;
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The object's collisiontype
	 */
	protected CollisionType getCollisionType()
	{
		return this.collisiontype;
	}
	
	/**
	 * Changes the object's collision type
	 *
	 * @param newtype The object's new collision type
	 */
	protected void setCollisionType(CollisionType newtype)
	{
		this.collisiontype = newtype;
	}
	
	/**
	 * @return The radius of the object as if it was a circle. If the radius has 
	 * not been specified, returns an approximation. Scaling is not included.
	 */
	public int getRadius()
	{
		// Checks if the radius needs initializing
		if (this.radius < 0)
			initializeRadius();
		return this.radius;
	}
	
	/**
	 * Changes the object's radius
	 *
	 * @param r The object's new radius (not including scaling). Use a 
	 * negative number if you want the radius to be approximated automatically.
	 */
	public void setRadius(int r)
	{
		if (r >= 0)
			this.radius = r;
		else
			initializeRadius();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * @return the longest possible radius of the object (from origin to a 
	 * corner) (this includes scaling)
	 */
	public double getMaxRangeFromOrigin()
	{
		// For circular objects the process is more simple
		if (getCollisionType() == CollisionType.CIRCLE)
			//return Math.max(getWidth() * getYScale(), 
			//		getHeight() * getXScale()) / 2.0;
			return Math.max(getYScale(), getXScale()) * getRadius();
		
		// First checks which sides are larger
		double maxXDist = Math.max(getOriginX(), getWidth() - getOriginX());
		double maxYDist = Math.max(getOriginY(), getHeight() - getOriginY());
		
		// Scales the values according to the object's scaling
		maxXDist *= getXScale();
		maxYDist *= getYScale();
		
		// Calculates the length from origin to the corner of those sides
		return HelpMath.pointDistance(0, 0, maxXDist, maxYDist);
	}
	
	/**
	 * This method draws the area of collision detection of the object, though 
	 * it doesn't take any modifcations made by subclasses into account. 
	 * This method meant for debugging.
	 * 
	 * @param g2d The graphics object used for drawing the collision area
	 */
	protected void drawCollisionArea(Graphics2D g2d)
	{
		g2d.setColor(new Color(255, 0, 0));
		
		switch (getCollisionType())
		{
			case BOX:
				g2d.drawRect(0, 0, getWidth(), getHeight()); break;
			case CIRCLE:
				g2d.drawArc(getOriginX() - getRadius(), 
						getOriginY() - getRadius(), getRadius() * 2, 
						getRadius() * 2, 0, 360); break;
			default:
				System.out.println(
						"DimensionalDrawnObject::drawCollisionArea " +
						"doesn't support " + getCollisionType()); break;
		}
	}
	
	private void initializeRadius()
	{
		this.radius = (getWidth() + getHeight()) / 4;
	}
}
