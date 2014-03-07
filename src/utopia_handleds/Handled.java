package utopia_handleds;

/**
 * This is the superinterface for all of the objects that can be handled
 * (actor, drawable, ect.). Each handled can be killed at some point so that 
 * it won't be handled anymore and its memory is released
 *
 * @author Mikko Hilpinen.
 *         Created 8.12.2012.
 */
public interface Handled
{
	/**
	 * @return Should the handled object be handled anymore
	 */
	public boolean isDead();
	
	/**
	 * Tries to end the objects all activities and release the memory used in the 
	 * object
	 */
	public void kill();
}
