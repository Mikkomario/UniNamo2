package utopia_helpAndEnums;

/**
 * This enumeration describes the object's shape, which affects how it collides 
 * with other objects. 
 *
 * @author Mikko Hilpinen.
 *         Created 29.6.2013.
 */
public enum CollisionType
{
	/**
	 * The object is a rectangle and has four sides
	 */
	BOX,
	/**
	 * The object is circular and colliding objects simply bounce away from it
	 */
	CIRCLE,
	/**
	 * The object has only one side which is right
	 */
	WALL;
}
