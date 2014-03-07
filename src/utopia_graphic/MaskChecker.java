package utopia_graphic;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import utopia_helpAndEnums.HelpMath;

/**
 * Maskcheckker is uselful for objects that want to use masks in collision 
 * detection. Maskcheckker provides most of the tools needed for that.
 *
 * @author Mikko Hilpinen.
 *         Created 2.7.2013.
 */
public class MaskChecker
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Sprite mask;
	// Mask contains only bright red (255, 0, 0) pixels
	private static int maskcolor = -65536;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new maskchecker that uses the given mask
	 *
	 * @param mask The mask the checker uses for collision detection.
	 */
	public MaskChecker(Sprite mask)
	{
		// Initializes attributes
		this.mask = mask;
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The mask used in the spriteobject
	 */
	public Sprite getMask()
	{
		return this.mask;
	}
	
	/**
	 * @param newmask Changes the object's mask to a new one
	 */
	public void setMask(Sprite newmask)
	{
		this.mask = newmask;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Refines the given relative collisiopoints to only hold the ones 
	 * that the mask contains
	 *
	 * @param collisionpoints The relative collisionpoints to be refined
	 * @param maskindex which index of the mask is used when the collision points 
	 * are refined (if negative number is added, only one of the mask's images 
	 * needs to contain the relative point)
	 * @return The refined relative collisionpoints
	 */
	public Point2D.Double[] getRefinedRelativeCollisionPoints(
			Point2D.Double[] collisionpoints, int maskindex)
	{
		// In case the mask is null (= not used), simply returns the same points
		if (getMask() == null)
			return collisionpoints;
		
		// Removes the collisionpoints that aren't in the mask
		ArrayList<Point2D.Double> templist = new ArrayList<Point2D.Double>();
		// Adds all the relevant points to the list
		for (int i = 0; i < collisionpoints.length; i++)
		{
			if (maskContainsRelativePoint(collisionpoints[i], maskindex))
				templist.add(collisionpoints[i]);
		}
		// Adds all points from the list to the table
		Point2D.Double[] newpoints = new Point2D.Double[templist.size()];
		for (int i = 0; i < templist.size(); i++)
		{
			newpoints[i] = templist.get(i);
		}
		return newpoints;
	}
	
	/**
	 * Tells whether the mask contains the given relative point
	 *
	 * @param relativep The relative point tested
	 * @param maskindex What index of the mask's animation is used in the check 
	 * (if negative number is added, the method returns true if any of the subimages 
	 * contains the point)
	 * @return Does the mask contain the given point
	 */
	public boolean maskContainsRelativePoint(Point2D.Double relativep, int maskindex)
	{		
		// In case mask is not used (mask == null), always returns true
		if (getMask() == null)
			return true;
		
		// Checks if the maskindex was a valid number
		if (maskindex >= getMask().getImageNumber())
			maskindex = getMask().getImageNumber() - 1;
		
		// Checks whether the point is within the mask and returns false if 
		// it isn't
		if (!HelpMath.pointIsInRange(relativep, 0, getMask().getWidth(), 0, 
				getMask().getHeight()))
			return false;

		if (maskindex >= 0)
		{
			int c = this.mask.getSubImage(maskindex).getRGB((int) relativep.x, 
					(int) relativep.y);
			return c == maskcolor;
		}
		// If maskindex was negative has to check each subimage
		else
		{
			for (int i = 0; i < getMask().getImageNumber(); i++)
			{
				int c = getMask().getSubImage(i).getRGB((int) relativep.x, 
						(int) relativep.y);
				if (c == maskcolor)
					return true;
			}
			return false;
		}
	}
}
