package utopia_gameobjects;


import utopia_handleds.Handled;

/**
 * GameObject represents any game entity. All of the gameobjects can be created, 
 * handled and killed. Pretty much any visible or invisible object in a game 
 * that can become an 'object' of an action should inherit this class.
 *
 * @author Mikko Hilpinen.
 *         Created 11.7.2013.
 */
public abstract class GameObject implements Handled
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean dead;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new gameobject that is alive until it is killed
	 */
	public GameObject()
	{
		// Initializes attributes
		this.dead = false;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public boolean isDead()
	{
		return this.dead;
	}

	@Override
	public void kill()
	{
		this.dead = true;
	}
	
	@Override
	public String toString()
	{
		String status = "alive ";
		if (isDead())
			status = "dead ";
		return status + getClass().getName();
	}
}
