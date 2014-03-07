package utopia_gameobjects;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import utopia_handleds.Drawable;
import utopia_handlers.DrawableHandler;
import utopia_handlers.TransformationListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_helpAndEnums.HelpMath;
import utopia_helpAndEnums.Movement;
import utopia_listeners.TransformationListener.TransformationAxis;
import utopia_listeners.TransformationListener.TransformationEvent;
import utopia_listeners.TransformationListener.TransformationType;



/**
 * An object from this class is can be drawed on screen as an two dimensional 
 * object. The object has a certain position, angle and scale.
 *
 * @author Mikko Hilpinen.
 *         Created 26.11.2012.
 */
public abstract class DrawnObject extends GameObject implements Drawable
{	
	// ATTRIBUTES	-------------------------------------------------------
	
	private double xscale, yscale, x, y, angle, xshear, yshear, currentdeterminant;
	private float alpha;
	private boolean visible;
	private int depth;
	private AffineTransform currenttransformation;
	private boolean transformationneedsupdating;
	private TransformationListenerHandler listenerhandler;
	
	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * Creates a new drawnobject with the given position. The object visible 
	 * upon creation.
	 *
	 * @param x The new x-coordinate of the object (Game world Pxl)
	 * @param y The new y-coordinate of the object (Game world Pxl)
	 * @param depth How 'deep' the object is drawn
	 * @param drawer The handler that draws the object (optional)
	 * @see DepthConstants
	 */
	public DrawnObject(int x, int y, int depth, DrawableHandler drawer)
	{
		// Initializes the attributes
		this.x = x;
		this.y = y;
		this.xscale = 1;
		this.yscale = 1;
		this.xshear = 0;
		this.yshear = 0;
		this.visible = true;
		this.angle = 0;
		this.depth = depth;
		this.alpha = 1;
		
		this.currenttransformation = new AffineTransform();
		this.currentdeterminant = 0;
		this.transformationneedsupdating = true;
		
		this.listenerhandler = new TransformationListenerHandler(false, null);
		
		// Adds the object to the drawer (if possible)
		// TODO: May cause deadlock
		if (drawer != null)
			drawer.addDrawable(this);
		
		//this.updateTransformation();
	}
	
	
	// ABSTRACT METHODS	---------------------------------------------------
	
	/**
	 * @return The Object's origin's x-translation from the left
	 */
	public abstract int getOriginX();
	
	/**
	 * @return The Object's origin's y-translation from the top
	 */
	public abstract int getOriginY();
	
	/**
	 * In this method, the object should draw itself as without any concerns 
	 * about the position, transformation or origin position.
	 *
	 * @param g2d the graphics object with which the object is drawn
	 */
	public abstract void drawSelfBasic(Graphics2D g2d);
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public boolean isVisible()
	{
		return this.visible;
	}

	@Override
	public void setVisible()
	{
		this.visible = true;
	}

	@Override
	public void setInvisible()
	{
		this.visible = false;
	}
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Remembers the previous transformation and transparency
		AffineTransform trans = g2d.getTransform();
		Composite originalcomposite = g2d.getComposite();
		
		// Changes the object's transparency
		int type = AlphaComposite.SRC_OVER; 
		g2d.setComposite(AlphaComposite.getInstance(type, getAlpha()));
		
		/*
		// Translates the sprite to the object's position
		g2d.translate(getX(), getY());
		// rotates it depending on its angle
		g2d.rotate(Math.toRadians((360 - getAngle())));
		// scales it depending on it's xscale and yscale
		g2d.scale(getXScale(), getYScale());
		// and translates the origin to the right position
		g2d.translate(-getOriginX(), -getOriginY());
		
		this.currenttransformation = g2d.getTransform();
		*/
		
		// Updates the transformation and uses it to transform the object
		updateTransformation();
		g2d.transform(this.currenttransformation);
		
		// Finally draws the object
		drawSelfBasic(g2d);
		
		// Loads the previous transformation and transparency
		g2d.setTransform(trans);
		g2d.setComposite(originalcomposite);
	}
	
	@Override
	public int getDepth()
	{
		return this.depth;
	}
	
	@Override
	public boolean setDepth(int depth)
	{
		this.depth = depth;
		return true;
	}
	
	@Override
	public String toString()
	{
		String status = "invisible ";
		if (isVisible())
			status = "visible ";
		return status + super.toString() + " position: (" + getX() + ", " + 
					getY() + "), angle: " + getAngle() + ", scaling: (" + 
					getXScale() + ", " + getYScale() + "), depth: " + getDepth();
	}
	
	@Override
	public void kill()
	{
		// Also kills the transformationListenerHandler (not the handleds 
		// inside it though)
		this.listenerhandler.killWithoutKillingHandleds();
		super.kill();
	}
	
	
	// GETTERS & SETTERS	-----------------------------------------------
	
	/**
	 * @return The object's rotation around the z-axis in degrees [0, 360[
	 */
	public double getAngle()
	{
		return HelpMath.checkDirection(this.angle);
	}
	
	/**
	 * Changes how much the object is rotated before drawing
	 *
	 * @param angle The angle of the drawn sprite in degrees around the 
	 * z-axis [0, 360[
	 */
	public void setAngle(double angle)
	{
		double oldangle = this.angle;
		
		this.angle = angle;
		checkAngle();
		this.transformationneedsupdating = true;
		
		// Informs the listeners
		this.listenerhandler.onTransformationEvent(
				new TransformationEvent(TransformationType.ROTATION, 
				TransformationAxis.Z, this.angle, this.angle - oldangle));
	}
	
	/**
	 * Increases the object's angle by the given amount
	 *
	 * @param rotation How much the angle around the z-axis is increased (degrees)
	 */
	public void addAngle(double rotation)
	{
		setAngle(getAngle() + rotation);
	}
	
	/**
	 * @return How much the sprite is scaled horizontally (from the original 
	 * angle) (default at 1)
	 */
	public double getXScale()
	{
		return this.xscale;
	}
	
	/**
	 * @return How much the sprite is scaled vertically (from the original 
	 * angle) (default at 1)
	 */
	public double getYScale()
	{
		return this.yscale;
	}
	
	/**
	 * Changes the object's shearing on the x-axis
	 *
	 * @param xshear The object's new horizontal shearing
	 */
	public void setXShear(double xshear)
	{
		double oldshear = this.xshear;
		
		this.xshear = xshear;
		this.transformationneedsupdating = true;
		
		// Informs the listeners
		this.listenerhandler.onTransformationEvent(new TransformationEvent(
				TransformationType.SHEARING, TransformationAxis.X, this.xshear, 
				this.xshear - oldshear));
	}
	
	/**
	 * Changes the object's shearing on the y-axis
	 *
	 * @param yshear The object's new vertical shearing
	 */
	public void setYShear(double yshear)
	{
		double oldshear = this.yshear;
		
		this.yshear = yshear;
		this.transformationneedsupdating = true;
		
		// Informs the listeners
		this.listenerhandler.onTransformationEvent(new TransformationEvent(
				TransformationType.SHEARING, TransformationAxis.Y, this.yshear, 
				this.yshear - oldshear));
	}
	
	/**
	 * @return How much the sprite is sheared horizontally (from the original 
	 * angle) (default at 0)
	 */
	public double getXShear()
	{
		return this.xshear;
	}
	
	/**
	 * @return How much the sprite is sheared vertically (from the original 
	 * angle) (default at 0)
	 */
	public double getYShear()
	{
		return this.yshear;
	}
	
	/**
	 * Changes the object's scaling on the x-axis
	 *
	 * @param xscale The object's new horizontal scaling
	 */
	public void setXScale(double xscale)
	{
		double oldscale = this.xscale;
		
		this.xscale = xscale;
		this.transformationneedsupdating = true;
		
		// Informs the listeners
		this.listenerhandler.onTransformationEvent(new TransformationEvent(
				TransformationType.SCAlING, TransformationAxis.X, this.xscale, 
				this.xscale / oldscale));
	}
	
	/**
	 * Changes the object's scaling on the y-axis
	 *
	 * @param yscale The object's new vertical scaling
	 */
	public void setYScale(double yscale)
	{
		double oldscale = this.yscale;
		
		this.yscale = yscale;
		this.transformationneedsupdating = true;
		
		// Informs the listeners
		this.listenerhandler.onTransformationEvent(new TransformationEvent(
				TransformationType.SCAlING, TransformationAxis.Y, this.yscale, 
				this.yscale / oldscale));
	}
	
	/**
	 * Changes how much the sprite is scaled horizontally and vertically
	 * (from the original angle)
	 *
	 * @param xscale The new horizontal scale of the object (default at 1)
	 * @param yscale The new vertical scale of the object (default at 1)
	 */
	public void setScale(double xscale, double yscale)
	{
		setXScale(xscale);
		setYScale(yscale);
	}
	
	/**
	 * Changes how much the sprite is scaled horizontally and vertically
	 * (from the original angle)
	 *
	 * @param xshear The new horizontal shear of the object (default at 0)
	 * @param yshear The new vertical shear of the object (default at 0)
	 */
	public void setShear(double xshear, double yshear)
	{
		setXShear(xshear);
		setYShear(yshear);
	}
	
	/**
	 * @return X-coordinate of the objects position in the game world (pxl)
	 */
	public double getX()
	{
		return this.x;
	}
	
	/**
	 * @return Y-coordinate of the objects position in the game world (pxl)
	 */
	public double getY()
	{
		return this.y;
	}
	
	/**
	 * @return The position of the object in a DoublePoint format
	 */
	public Point2D.Double getPosition()
	{
		return new Point2D.Double(this.x, this.y);
	}
	
	/**
	 * Changes the object's position in the game world
	 *
	 * @param x The new position's x-coordinate (pxl)
	 * @param y The new position's y-coordinate (pxl)
	 */
	public void setPosition(double x, double y)
	{
		setX(x);
		setY(y);
	}
	
	/**
	 * Changes the object's position on the x-axis
	 *
	 * @param x The object's new x-coordinate
	 */
	public void setX(double x)
	{
		double oldx = this.x;
		
		this.x = x;
		this.transformationneedsupdating = true;
		
		// Informs the listeners
		this.listenerhandler.onTransformationEvent(new TransformationEvent(
				TransformationType.TRANSLATION, TransformationAxis.X, this.x, 
				this.x - oldx));
	}
	
	/**
	 * Changes the object's position on the y-axis
	 *
	 * @param y The object's new y-coordinate
	 */
	public void setY(double y)
	{
		double oldy = this.y;
		
		this.y = y;
		this.transformationneedsupdating = true;
		
		// Informs the listeners
		this.listenerhandler.onTransformationEvent(new TransformationEvent(
				TransformationType.TRANSLATION, TransformationAxis.Y, this.y, 
				this.y - oldy));
	}
	
	/**
	 * Changes the object's position by the given amount
	 *
	 * @param hspeed How much the object is moved horizontally
	 * @param vspeed How much the object is move vertically
	 */
	public void addPosition(double hspeed, double vspeed)
	{
		setPosition(getX() + hspeed, getY() + vspeed);
	}
	
	/**
	 * Changes the object's position according to the given movement
	 *
	 * @param movement The movement the object 'makes'
	 */
	public void addPosition(Movement movement)
	{
		addPosition(movement.getHSpeed(), movement.getVSpeed());
	}
	
	/**
	 * @return The alpha value or the opacity of the object. 1 means that the 
	 * object is fully visible, 0 means that the object is completely transparent.
	 */
	public float getAlpha()
	{
		return this.alpha;
	}
	
	/**
	 * Changes the objects alpha value, which affects its transparency.
	 *
	 * @param alpha The object's new alpha value [0, 1] (0 = invisible, 1 = 
	 * fully visible)
	 */
	public void setAlpha(float alpha)
	{
		// Checks the argument
		if (alpha > 1)
			this.alpha = 1;
		else if (alpha < 0)
			this.alpha = 0;
		else
			this.alpha = alpha;
	}
	
	/**
	 * Changes the object's alpha value relative to the former value
	 *
	 * @param adjustment How much the alpha value is adjusted
	 */
	public void adjustAlpha(float adjustment)
	{
		setAlpha(getAlpha() + adjustment);
	}
	
	/**
	 * @return The transformationListenerHandler that informs listeners about 
	 * the changes in the object's transformations
	 */
	public TransformationListenerHandler getTransformationListenerHandler()
	{
		return this.listenerhandler;
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	// Restores the angle to between 0 and 360
	private void checkAngle()
	{
		this.angle = HelpMath.checkDirection(this.angle);
	}
	
	/**
	 * Scales the object with the given factors. The scaling stacks with previous 
	 * scaling and is not necessarily dependent on the original size of the object
	 *
	 * @param xscale How much the object is scaled horizontally
	 * @param yscale How much the object is scaled vertically
	 */
	public void scale(double xscale, double yscale)
	{
		setScale(getXScale() * xscale, getYScale() * yscale);
	}
	
	/**
	 * Transforms the point so that collisions can be checked without
	 * transformations. In other words, transforms a point from the absolute 
	 * space to the object's relative space
	 *
	 * @param absolutepoint The absolute point to be transformed into negative space
	 * @return The point where all of the object's transformations are negated
	 */
	public Point2D.Double negateTransformations(Point2D absolutepoint)
	{
		updateTransformation();
		
		Point2D.Double newpoint = new Point2D.Double(0, 0);
		AffineTransform inversetransform = getOpposingTransform();
		
		if (inversetransform == null)
			return null;
		
		inversetransform.transform(absolutepoint, newpoint);
		return newpoint;
		
		/*// TODO: Readd this if the new version doesn't work
		try
		{
			if (this.currentdeterminant != 0)
				this.currenttransformation.inverseTransform(oldpoint, newpoint);
			else
			{
				if (getXScale() != 1)
					System.out.println("Non-invertible object: " + this);
				//newpoint = negateTransformations(x, y, getX(), getY(), 
				//		getXScale(), getYScale(), getAngle(), getOriginX(), getOriginY());
				// TODO: Check if this works in all cases (it seems like 
				//everything else than translation is 0 when this happens)
				newpoint = new Point2D.Double(x - getX(), y - getY());
			}
		}
		catch (NoninvertibleTransformException exception)
		{
			System.err.println("Failed to inverse transform a position");
			exception.printStackTrace();
		}
		
		return newpoint;
		*/
		//return negateTransformations(x, y, getX(), getY(), getXScale(), 
		//		getYScale(), getAngle(), getOriginX(), getOriginY());
	}
	
	/**
	 * Transforms the point so that the collision can be checked without
	 * transformations. Uses specific transformations.
	 * 
	 * @param x The x-coordinate of the transformed object's position (absolute pixel)
	 * @param y The y-coordinate of the transformed object's position (absolute pixel)
	 * @param px The x-coordinate of the point to be negated (absolute pixel)
	 * @param py The y-coordinate of the point to be negated (absolute pixel)
	 * @param xscale The x-scale in the transformation
	 * @param yscale The y-scale in the transformation
	 * @param angle The angle in the transformation [0, 360[
	 * @param originx The x-coordinate of the transformation's origin
	 * @param originy The y-coordinate of the transformation's origin
	 * @return The point where all of the object's transformations have been 
	 * negated
	 * 
	 * @deprecated This method isn't supported since the object's transformation 
	 * matrix is used for the position trasformation instead
	 */
	@Deprecated
	protected static Point2D.Double negateTransformations(double px, double py, double x, 
			double y, double xscale, double yscale, double angle, int originx, 
			int originy)
	{
		double tempx = px;
		double tempy = py;
		
		// Position Translate (test this)
		tempx -= x;
		tempy -= y;
		
		// Rotation
		if (angle > 0)
		{
			double prevDir = HelpMath.pointDirection(0, 0, tempx, tempy);
			double newDir = HelpMath.checkDirection(prevDir - angle);
			double dist = HelpMath.pointDistance(0, 0, tempx, tempy);
			
			tempx = HelpMath.lendirX(dist, newDir);
			tempy = HelpMath.lendirY(dist, newDir);
		}
		
		// Scaling
		if (xscale != 1 || yscale != 1)
		{
			double xdist = tempx;
			double ydist = tempy;
			double newxdist = xdist*(1/xscale);
			double newydist = ydist*(1/yscale);
			
			tempx -= xdist - newxdist;
			tempy -= ydist - newydist;
		}
		
		// Origin translate
		tempx += originx;
		tempy += originy;
		
		return new Point2D.Double(tempx, tempy);
	}
	
	/**
	 * Transforms the position depending on the object's current transformation. 
	 * In other words transforms a point from the object's relative space to 
	 * the absolute space.
	 *
	 * @param relativepoint The point to be transformed.
	 * @return Absolute position created by transforming the relative point
	 */
	public Point2D.Double transform(Point2D relativepoint)
	{	
		updateTransformation();
		
		Point2D.Double newpoint = new Point2D.Double(0, 0);
		
		this.currenttransformation.transform(relativepoint, newpoint);
		
		return newpoint;
	}
	
	/**
	 * Transforms the position depending on the object's current transformation
	 *
	 * @param px Position's x-coordinate relative to the object's origin (relative pixel)
	 * @param py Position's y-coordinate relative to the object's origin (relative pixel)
	 * @param x The x-coordinate of the position transformation (absolute pixel)
	 * @param y The y-coordinate of the position transformation (absolute pixel)
	 * @param xscale The xscale transformation
	 * @param yscale The yscale transformation
	 * @param angle The angle transformation [0, 360[
	 * @param originx The x-coordinate of the origin of the transformation (relative pixel)
	 * @param originy The y-coordinate of the origin of the transformation (relative pixel)
	 * @return Absolute position with transformations added
	 * 
	 * @deprecated This method isn't supported anymore since position transformation 
	 * is now done with the object's transformation
	 */
	@Deprecated
	protected Point2D.Double transform(double px, double py, double x, double y, 
			double xscale, double yscale, double angle, int originx, int originy)
	{	
		double tempx = px;
		double tempy = py;
		
		// Origin translate
		tempx -= originx;
		tempy -= originy;
		
		// Scaling
		if (xscale != 1 || yscale != 1)
		{
			double xdist = tempx;
			double ydist = tempy;
			double newxdist = xdist*xscale;
			double newydist = ydist*yscale;
			tempx -= xdist - newxdist;
			tempy -= ydist - newydist;
		}
		
		// Rotation
		if (angle > 0)
		{
			double prevDir = HelpMath.pointDirection(0, 0, tempx, tempy);
			double newDir = HelpMath.checkDirection(prevDir + angle);
			double dist = HelpMath.pointDistance(0, 0, tempx, tempy);
			tempx = HelpMath.lendirX(dist, newDir);
			tempy = HelpMath.lendirY(dist, newDir);
		}
		
		// Position Translate
		tempx += x;
		tempy += y;
		
		return new Point2D.Double(tempx, tempy);
	}
	
	/**
	 * Rotates the object around a certain (absolute) position
	 *
	 * @param angle The amount of degrees the object rotates
	 * @param p The point around which the object rotates (absolute pixel)
	 */
	public void rotateAroundPoint(double angle, Point2D.Double p)
	{
		// Moves the object around the point
		Point2D.Double newposition = 
				HelpMath.getRotatedPosition(p.getX(), p.getY(), getPosition(), angle);
		setPosition(newposition.getX(), newposition.getY());
		// Also rotates the object
		addAngle(angle);
	}
	
	/**
	 * Rotates the object around a relative point. 
	 * A bit heavier than the rotatearoundpoint method
	 *
	 * @param angle The amount of degrees the object is rotated
	 * @param p The relative point around which the object is rotated
	 */
	public void rotateAroundRelativePoint(double angle, Point2D.Double p)
	{
		Point2D.Double abspoint = transform(p);
		rotateAroundPoint(angle, abspoint);
	}
	
	/**
	 * Calculates and returns the direction towards which the other drawnObject 
	 * is from this object's origin.
	 * 
	 * @param other The other drawnObject towards which the direction is 
	 * calculated
	 * @return The direction from this object towards the other object
	 */
	public double getDirectionTowardsObject(DrawnObject other)
	{
		return HelpMath.pointDirection(getX(), getY(), other.getX(), 
				other.getY());
	}
	
	/**
	 * This is an alternate method for drawing the object (instead of DrawSelf) 
	 * and works only with objects that draw multiple objects that then transform 
	 * themselves normally (like cameras, for example). This method should 
	 * replace drawSelf if it is used (not the drawSelfBasic method)
	 *
	 * @param g2d The graphics object that draws the content of the object
	 * @see DrawSelf
	 * @deprecated This method is no longer supported
	 */
	@Deprecated
	protected void drawSelfAsContainer(Graphics2D g2d)
	{
		// TODO: Re-add support for this method since it is actually needed
		
		AffineTransform trans = g2d.getTransform();
		
		// and translates the origin to the right position
		g2d.translate((double) -getOriginX(), (double) -getOriginY());
		// scales it depending on it's xscale and yscale
		g2d.scale(getXScale(), getYScale());
		// rotates it depending on its angle
		g2d.rotate(Math.toRadians((360 - getAngle())));
		// Translates the sprite to the object's position
		g2d.translate(getX(), getY());
		
		// Finally draws the object
		drawSelfBasic(g2d);
		
		// Loads the previous transformation
		g2d.setTransform(trans);
	}
	
	/**
	 * @return An affineTransform matrix opposite to the object's normal 
	 * transformation. This may be used in cameras for example
	 */
	protected AffineTransform getOpposingTransform()
	{
		AffineTransform inversion = null;
		
		try
		{	
			if (this.currentdeterminant != 0)
			{
				inversion = new AffineTransform(this.currenttransformation);
				inversion.invert();
			}
			else
			{
				// In the case the current transformation can't be inverted, 
				// inverts the translations (seems to be enough)
				inversion = new AffineTransform();
				inversion.translate(-getX(), -getY());
			}
		}
		catch (NoninvertibleTransformException exception)
		{
			System.err.println("Failed to inverse transform a position");
			exception.printStackTrace();
		}
		
		return inversion;
		
		// Check if this is even needed or if
		// this.currenttransformation.invert() can be used
		// Notie the negateTransformations -method
		/*
		// Translates the origin to the right position
		trans.translate(getOriginX(), getOriginY());
		// scales it depending on it's xscale and yscale
		trans.scale(1/getXScale(), 1/getYScale());
		// rotates it depending on its angle
		trans.rotate(Math.toRadians((getAngle())));
		// Translates the sprite to the object's position
		trans.translate(-getX(), -getY());
		
		return trans;
		*/
	}
	
	/**
	 * Tells the object that it needs to update its transformations. This 
	 * should be used by object that depend on other objects' transformations
	 */
	protected void forceTransformationUpdate()
	{
		this.transformationneedsupdating = true;
	}
	
	// Updates the current transformation value
	private void updateTransformation()
	{		
		// Only updates the transformation if need be
		if (!this.transformationneedsupdating)
			return;
		
		this.currenttransformation = new AffineTransform();
		
		// Translates the sprite to the object's position
		this.currenttransformation.translate(getX(), getY());
		// rotates it depending on its angle
		this.currenttransformation.rotate(Math.toRadians((360 - getAngle())));
		// scales it depending on it's xscale and yscale
		this.currenttransformation.scale(getXScale(), getYScale());
		// TODO: Check that this is in the right position sometime
		// Shears the object depending on its xshear and yshear
		this.currenttransformation.shear(getXShear(), getYShear());
		// and translates the origin to the right position
		this.currenttransformation.translate(-getOriginX(), -getOriginY());
		
		// Also saves the determinant since it is used often
		this.currentdeterminant = this.currenttransformation.getDeterminant();
		
		this.transformationneedsupdating = false;
	}
}