package uninamo_gameplaysupport;

import conflict_collision.Collidable;

/**
 * Objects that implement this interface imply that they should not be moved 
 * through. Objects should collide with and bounce off of walls.
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public interface Wall extends Collidable
{
	// Wall doesn't force any methods
}
