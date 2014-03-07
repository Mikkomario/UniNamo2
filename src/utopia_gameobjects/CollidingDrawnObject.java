package utopia_gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.HelpMath;
import utopia_listeners.CollisionListener;
import utopia_listeners.TransformationListener;

/**
 * Collidingdrawnobject is a subclass of the drawnobject that can collide with 
 * other objects and may react to collisions
 *
 * @author Mikko Hilpinen.
 *         Created 30.6.2013.
 */
public abstract class CollidingDrawnObject extends DimensionalDrawnObject 
		implements CollisionListener, TransformationListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private Point2D.Double[] relativecollisionpoints, lastabsolutecollisionpoints;
	private boolean active, wastransformed;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new collidingdrawnobject with the given information. The object 
	 * is visible and static by default. A subclass should call one of the 
	 * setCollisionPrecision methods.
	 *
	 * @param x The object's position's x-coordinate
	 * @param y The object's position's y-coordinate
	 * @param depth How 'deep' the object is drawn
	 * @param isSolid Can the object be collided with
	 * @param collisiontype What shape the object is collisionwise
	 * @param drawer Which drawablehandler will draw the object (optional)
	 * @param collidablehandler The collidablehandler that will handle the object's 
	 * collision checking (optional)
	 * @param collisionhandler The collisionhandler that will handle the object's 
	 * collision informing (optional)
	 */
	public CollidingDrawnObject(int x, int y, int depth, boolean isSolid,
			CollisionType collisiontype, DrawableHandler drawer, 
			CollidableHandler collidablehandler, CollisionHandler collisionhandler)
	{
		super(x, y, depth, isSolid, collisiontype, drawer, collidablehandler);
		
		// Initializes attributes
		this.active = true;
		this.wastransformed = true;
		this.relativecollisionpoints = new Point2D.Double[0];
		this.lastabsolutecollisionpoints = null;

		// Adds the object to the handler(s)
		if (collisionhandler != null)
			collisionhandler.addCollisionListener(this);
		getTransformationListenerHandler().addListener(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public boolean isActive()
	{
		return this.active;
	}
	
	@Override
	public void inactivate()
	{
		this.active = false;
	}
	
	@Override
	public void activate()
	{
		this.active = true;
	}
	
	@Override
	public Point2D.Double[] getCollisionPoints()
	{	
		// If the object hasn't been transformed since the last check, returns 
		// the old results
		if (!this.wastransformed)
			return this.lastabsolutecollisionpoints;
		
		Point2D.Double[] relativepoints = getRelativeCollisionPoints();
		
		// if relativepoints don't exist, returns an empty table
		if (relativepoints == null)
			return new Point2D.Double[0];
		
		Point2D.Double[] newpoints = new Point2D.Double[relativepoints.length];
		
		// Transforms each of the points and adds them to the new table
		for (int i = 0; i < relativepoints.length; i++)
		{
			newpoints[i] = transform(relativepoints[i]);
		}
		
		this.wastransformed = false;
		this.lastabsolutecollisionpoints = newpoints;
		return newpoints;
	}
	
	@Override
	public void onTransformationEvent(TransformationEvent e)
	{
		this.wastransformed = true;
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The relative collision coordinates from which the collisions 
	 * are checked
	 */
	protected Point2D.Double[] getRelativeCollisionPoints()
	{
		/*
		System.out.println("Relative collision points:");
		for (int i = 0; i < this.relativecollisionpoints.length; i++)
		{
			System.out.println(this.relativecollisionpoints[i]);
		}
		*/
		
		return this.relativecollisionpoints;
	}
	
	/**
	 * Changes the object's list of collisionpoints
	 *
	 * @param collisionpoints The new set of relative collisionpoints. Use 
	 * null if you wan't no collision points.
	 */
	protected void setRelativeCollisionPoints(Point2D.Double[] collisionpoints)
	{
		if (collisionpoints != null)
			this.relativecollisionpoints = collisionpoints;
		else
			this.relativecollisionpoints = new Point2D.Double[0];
	}
	
	/**
	 * Changes how precisely the object checks collisions. More precision means 
	 * slower checking and more precise results. Large and scaled objects should 
	 * have higher precisions than small objects. This method is meant for objects 
	 * that can be fit into a box, there is another method for circular objects.
	 *
	 * @param edgeprecision How precise is the collision checking on the edges 
	 * of the object? 0 means no collision checking on edges, 1 means only corners 
	 * and 2+ adds more (4*edgeprecision) collisionpoints on the edges.
	 * @param insideprecision How precise is the collision checking inside the 
	 * object? 0 means no collision checking inside the object, 1 means only 
	 * the center of the object is checked and 2+ means alot more 
	 * (insideprecision^2) collisionpoints inside the object.
	 * @see setCircleCollisionPrecision
	 */
	protected void setBoxCollisionPrecision(int edgeprecision, int insideprecision)
	{
		// Doesn't work with negative values
		if (edgeprecision < 0 || insideprecision < 0)
			return;
		
		initializeBoxCollisionPoints(edgeprecision, insideprecision);
	}
	
	/**
	 * Changes how accurte the collisionpoints are. Works with circular objects. 
	 * There is another method for objects that can be fit inside a box.
	 *
	 * @param radius The maximum radius of the collision circle (relative, 
	 * from origin) (>= 0)
	 * @param edgeprecision How many collisionpoints will be added to the outer 
	 * edge (>= 0). Center is added at precision 2 and layers will be added 
	 * between the center and the edge from precision 3 onwards.
	 * @param layers How many collisionpoint layers there will be? (>= 0)
	 * @see setBoxCollisionPrecision
	 */
	protected void setCircleCollisionPrecision(int radius, int edgeprecision, int layers)
	{
		// Checks the arguments
		if (radius < 0 || edgeprecision < 0 || layers < 0)
			return;
		
		initializeCircleCollisionPoints(radius, edgeprecision, layers);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Draws the collision points of the object. This method should be called 
	 * at the drawselfbasic method.
	 *
	 * @param g2d The graphics object that does the actual drawing
	 * @warning This method is supposed to be used only in testing purposes
	 */
	protected void drawCollisionPoints(Graphics2D g2d)
	{
		g2d.setColor(new Color(255, 0, 0));
		
		for (Point2D.Double p: getRelativeCollisionPoints())
		{
			g2d.drawRect((int) p.x, (int) p.y, 5, 5);
		}
		
		g2d.setColor(new Color(0, 0, 0));
	}
	
	private void initializeBoxCollisionPoints(int edgeprecision, int insideprecision)
	{
		// edgeprecision 0 -> no sides or corners
		// insideprecision 0 -> no inside points
		
		// Calculates the number of collisionpoints
		int size = edgeprecision*4 + (int) Math.pow(insideprecision, 2);
		this.relativecollisionpoints = new Point2D.Double[size];
		
		int index = 0;
		
		if (edgeprecision > 0)
		{
			// Goes through the edgepoints and adds them to the table
			for (int ex = 0; ex < edgeprecision + 1; ex++)
			{
				for (int ey = 0; ey < edgeprecision + 1; ey++)
				{
					// Only adds edges
					if (ex != 0 && ex != edgeprecision && ey != 0 && ey != edgeprecision)
						continue;
					
					// Adds a point to the table
					this.relativecollisionpoints[index] = new Point2D.Double(
							(int) (ex / (double) edgeprecision *getWidth()), 
							(int) (ey / (double) edgeprecision *getHeight()));
					
					index++;
				}
			}
		}
		if (insideprecision > 0)
		{
			// Goes through the insidepoints and adds them to the table
			for (int ix = 1; ix < insideprecision + 1; ix++)
			{
				for (int iy = 1; iy < insideprecision + 1; iy++)
				{	
					// Adds a point to the table
					this.relativecollisionpoints[index] = new Point2D.Double(
							(int) (ix / (double) (insideprecision + 1) *getWidth()), 
							(int) (iy / (double) (insideprecision + 1) *getHeight()));
					
					index++;
				}
			}
		}
	}
	
	// Initializes collisionpoints for circular object
	private void initializeCircleCollisionPoints(int radius, int edgeprecision, 
			int layers)
	{
		//System.out.println("Initializes circle collision points");
		// Calculates the number of collisionpoints
		int size = edgeprecision;
		// From layer 2 onwards, center is added
		if (layers >= 2)
			size ++;
		// Larger amount of layers means more collisionpoints
		for (int i = 3; i <= layers; i++)
		{
			int more = (int) ((i - 2.0) / (layers - 1.0) * edgeprecision);
			size += more;
		}
		
		//System.out.println("There will be " + size + " points");
		
		this.relativecollisionpoints = new Point2D.Double[size];
		int index = 0;
		
		for (int i = 1; i <= layers; i++)
		{
			// Calculates the necessary information
			int pointsonlayer = (int) ((i - 1.0)/(layers - 1.0) * edgeprecision);
			// The first layer has an extra 1 point (instead of 0)
			if (i == 1)
				pointsonlayer = 1;
			int layerradius = (int) (radius * ((i - 1.0) / (layers - 1.0)));
			
			//System.out.println("Layer " + i + " will have " + pointsonlayer + " points");
			
			// Creates the points
			for (int a = 0; a < 360; a += 360 / pointsonlayer)
			{
				//System.out.println("Adds a point to the layer " + i);
				this.relativecollisionpoints[index] = new Point2D.Double(
						getOriginX() + (int) HelpMath.lendirX(layerradius, a), 
						getOriginY() + (int) HelpMath.lendirY(layerradius, a));
				//System.out.println("The new point was " + this.relativecollisionpoints[index]);
				index ++;
			}
		}
	}
}
