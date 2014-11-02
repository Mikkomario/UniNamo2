package uninamo_gameplaysupport;

import omega_gameplay.PhysicalCollidable;

/**
 * Objects that implement this interface imply that they should not be moved 
 * through. Objects should collide with and bounce off of walls.
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public interface Wall extends PhysicalCollidable
{
	// Wall doesn't force any methods
}
