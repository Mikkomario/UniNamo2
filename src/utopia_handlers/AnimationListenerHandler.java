package utopia_handlers;

import utopia_graphic.SpriteDrawer;
import utopia_handleds.Handled;
import utopia_listeners.AnimationListener;

/**
 * Animationlistenerhandler informs numerous animationlisteners about animation 
 * events
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 * @see utopia_graphic.SpriteDrawer
 */
public class AnimationListenerHandler extends LogicalHandler implements 
		AnimationListener
{
	// ATTRIBUTES	----------------------------------------------------
	
	private SpriteDrawer lastdrawer;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new empty animationlistenerhandler with the given information
	 *
	 * @param autodeath Will the handler die when it runs out of listeners
	 * @param superhandler The animationlistenerhandler that will inform 
	 * the handler about animation events (optional)
	 */
	public AnimationListenerHandler(boolean autodeath,
			AnimationListenerHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.lastdrawer = null;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected Class<?> getSupportedClass()
	{
		return AnimationListener.class;
	}

	@Override
	public void onAnimationEnd(SpriteDrawer spritedrawer)
	{
		// Remembers the data
		this.lastdrawer = spritedrawer;
		// Informs all listeners about the event
		handleObjects();
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		// Informs active animationlisteners about the event
		AnimationListener l = (AnimationListener) h;
		
		if (l.isActive())
			l.onAnimationEnd(this.lastdrawer);
		
		return true;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds a new animationlistener to the informed listeners
	 *
	 * @param l The animationlistener added
	 */
	public void addAnimationListener(AnimationListener l)
	{
		addHandled(l);
	}
}
