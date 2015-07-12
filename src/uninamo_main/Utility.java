package uninamo_main;

import conflict_util.Polygon;
import vision_sprite.Sprite;
import genesis_util.Vector3D;

/**
 * This is a collection of useful static methods
 * @author Mikko Hilpinen
 * @since 12.7.2015
 */
public class Utility
{
	// CONSTRUCTOR	--------------
	
	private Utility()
	{
		// Static interface
	}

	
	// OTHER METHODS	----------
	
	/**
	 * Calculates the vertices of the sprite's corners
	 * @param sprite The sprite
	 * @return The sprite's corners
	 */
	public static Vector3D[] getSpriteVertices(Sprite sprite)
	{
		Vector3D topLeft = sprite.getOrigin().reverse();
		return Polygon.getRectangleVertices(topLeft, topLeft.plus(sprite.getDimensions()));
	}
}
