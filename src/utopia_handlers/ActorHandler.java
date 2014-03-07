package utopia_handlers;

import utopia_handleds.Actor;
import utopia_handleds.Handled;


/**
 * The object from this class will control multiple actors, calling their 
 * act-methods and removing them when necessary
 *
 * @author Mikko Hilpinen.
 *         Created 27.11.2012.
 */
public class ActorHandler extends LogicalHandler implements Actor
{
	// ATTRIBUTES	------------------------------------------------------
	
	private double laststeplength;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new actorhandler. Actors must be added manually later
	 *
	 * @param autodeath Will the handler die if there are no living actors to be handled
	 * @param superhandler The handler that will call the act-event of the object (optional)
	 */
	public ActorHandler(boolean autodeath, ActorHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.laststeplength = 0;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void act(double steps)
	{
		// Updates the steplength and informs objects
		this.laststeplength = steps;
		handleObjects();
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return Actor.class;
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		// Calls the act method of active handleds
		Actor a = (Actor) h;
		
		if (a.isActive())
			a.act(this.laststeplength);
		
		return true;
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Adds a new actor to the handled actors
	 *
	 * @param a The actor to be added
	 */
	public void addActor(Actor a)
	{
		addHandled(a);
	}
}
