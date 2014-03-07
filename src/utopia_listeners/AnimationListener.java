package utopia_listeners;

import utopia_graphic.SpriteDrawer;
import utopia_handleds.LogicalHandled;
import utopia_handlers.AnimationListenerHandler;

/**
 * Animationlistener is informed when an animation cycle ends.<br>
 * Remember to add the object into an AnimationListenerHandler
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 * @see AnimationListenerHandler
 * @see SpriteDrawer
 */
public interface AnimationListener extends LogicalHandled
{
	/**
	 * This method is called when an animation of the sprite ends or, more 
	 * precisely, a cycle in the animation ends.
	 *
	 * @param spritedrawer The spritedrawer that draws the sprite who's 
	 * animation just completed a cycle. 
	 */
	public void onAnimationEnd(SpriteDrawer spritedrawer);
}
