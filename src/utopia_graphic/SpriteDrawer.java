package utopia_graphic;

import java.awt.Graphics2D;





import utopia_gameobjects.DrawnObject;
import utopia_handleds.Actor;
import utopia_handlers.ActorHandler;
import utopia_handlers.AnimationListenerHandler;

/**
 * Spritedrawer is able to draw animated sprites for an object. Object's can 
 * draw the sprite(s) calling the drawSprite method.<p>
 * 
 * The spriteDrawer can be tied into a single object, making it die 
 * when that object does.
 *
 * @author Mikko Hilpinen.
 *         Created 2.7.2013.
 */
public abstract class SpriteDrawer implements Actor
{
	// ATTRIBUTES	-------------------------------------------------------
	
	private double imageSpeed, imageIndex;
	private boolean alive, active;
	private AnimationListenerHandler listenerhandler;
	private DrawnObject user;
		
		
	// CONSTRUCTOR	-------------------------------------------------------
		
	/**
	 * Creates a new spritedrawer with the given sprite to draw.
	 *
	 * @param animator The actorhandler that calls the drawer's animation 
	 * (optional)
	 * @param user The object the drawer is tied into. The spritedrawer will 
	 * automatically die when the user dies. (Optional)
	 */
	public SpriteDrawer(ActorHandler animator, DrawnObject user)
	{
		// Initializes the attributes
		this.listenerhandler = new AnimationListenerHandler(false, null);
		this.user = user;
		
		this.imageSpeed = 0.1;
		this.imageIndex = 0;
		this.alive = true;
		this.active = true;
		
		// Adds the spritedrawer to the handler, if possible
		if (animator != null)
			animator.addActor(this);
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * @return The sprite that is currently being drawn / used
	 */
	protected abstract Sprite getCurrentSprite();
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}

	@Override
	public boolean isDead()
	{
		return (!this.alive || (this.user != null && this.user.isDead()));
	}

	@Override
	public void kill()
	{
		this.alive = false;
	}

	@Override
	public void act(double steps)
	{
		// Animates the sprite
		animate(steps);
	}
	
	
	// GETTERS & SETTERS	-----------------------------------------------
	
	/**
	 * @return The sprite as which the object is represented
	 */
	public Sprite getSprite()
	{
		// TODO: Consider just using the following method instead of this wrapper
		return getCurrentSprite();
	}
	
	/**
	 * @return How fast the frames in the animation change (frames / step) 
	 * (default at 0.1)
	 */
	public double getImageSpeed()
	{
		return this.imageSpeed;
	}
	
	/**
	 * Changes how fast the frames in the animation change
	 * 
	 * @param imageSpeed The new animation speed (frames / step) (0.1 by default)
	 */
	public void setImageSpeed(double imageSpeed)
	{
		this.imageSpeed = imageSpeed;
	}
	
	/**
	 * Changes the image speed so that a single animation cycle will last 
	 * <b>duration</b> steps
	 *
	 * @param duration How many steps will a single animation cycle last
	 */
	public void setAnimationDuration(int duration)
	{
		// Checks the argument
		if (duration == 0)
			setImageSpeed(0);
		else
			setImageSpeed(getSprite().getImageNumber() / (double) duration);
	}
	
	/**
	 * @return Which subimage from the animation is currently drawn [0, numberOfSubimages[
	 */
	public int getImageIndex()
	{
		return (int) this.imageIndex;
	}
	
	/**
	 * Changes which subimage from the animation is currently drawn
	 * 
	 * @param imageIndex The index of the subimage drawn [0, numberOfSubimages[
	 */
	public void setImageIndex(int imageIndex)
	{
		this.imageIndex = imageIndex;
		// Also checks the new index
		checkImageIndex();
	}
	
	/**
	 * @return The animationlistenerhandler that will inform animationlisteners 
	 * about the events in the animation
	 */
	public AnimationListenerHandler getAnimationListenerHandler()
	{
		return this.listenerhandler;
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Draws the sprite. Should be called in the DrawnObject's drawSelfBasic 
	 * method or in another similar method.
	 * 
	 * @param g2d The graphics object that does the actual drawing
	 * @param xtranslation How much the sprite is translated horizontally 
	 * before drawing
	 * @param ytranslation How much the sprite is translated vertically 
	 * before drawing
	 */
	public void drawSprite(Graphics2D g2d, int xtranslation, int ytranslation)
	{
		// Only works if alive
		if (isDead())
			return;
		
		// Draws the sprite
		drawSprite(g2d, xtranslation, ytranslation, getImageIndex());
	}
	
	/**
	 * Draws the sprite. Should be called in the DrawnObject's drawSelfBasic 
	 * method or in another similar method.
	 * 
	 * @param g2d The graphics object that does the actual drawing
	 * @param xtranslation How much the sprite is translated horizontally 
	 * before drawing
	 * @param ytranslation How much the sprite is translated vertically 
	 * before drawing
	 * @param imageindex Which subimage of the sprite is drawn (used with 
	 * drawers that aren't automatically animated)
	 */
	public void drawSprite(Graphics2D g2d, int xtranslation, int ytranslation, 
			int imageindex)
	{
		// Draws the sprite
		g2d.drawImage(getSprite().getSubImage(imageindex), xtranslation, 
				ytranslation, null);
	}
	
	// Handles the change of the image index
	private void animate(double steps)
	{
		this.imageIndex += getImageSpeed() * steps;
		checkImageIndex();
	}
	
	// Returns the imageindex to a valid value
	private void checkImageIndex()
	{
		int imageindexlast = getImageIndex();
		
		this.imageIndex = this.imageIndex % getSprite().getImageNumber();
		
		if (this.imageIndex < 0)
			this.imageIndex += getSprite().getImageNumber();
		
		// If image index changed (cycle ended / looped), informs the listeners
		if (getImageIndex() != imageindexlast)
			getAnimationListenerHandler().onAnimationEnd(this);
	}
}
