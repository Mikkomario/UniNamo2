package uninamo_obstacles;

import java.awt.Graphics2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import utopia_gameobjects.BouncingBasicPhysicDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handleds.Collidable;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * obstacles are the main 'objects' in the game in a sense that the user is 
 * trying to affect them. Obstacles interact with some of the machinery but 
 * are more often the passive part in the interaction.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class Obstacle extends BouncingBasicPhysicDrawnObject
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 *  Creates a new obstacle to the given position
	 *  
	 * @param x The x-coordinate of the obstacle (pixels)
	 * @param y The y-coordinate of the obstacle (pixels)
	 * @param isSolid Can the obstacle be collided with
	 * @param collisiontype What is the shape of the obstacle
	 * @param drawer The drawableHandler that will draw the obstacle
	 * @param collidablehandler The collidableHandler that will handle the 
	 * obstacle's collision checking
	 * @param collisionhandler The collisionHandler that will inform the 
	 * object about collision events (optional)
	 * @param actorhandler The actorHandler that will inform the object about 
	 * step events
	 * @param spritename The name of the sprite used to draw the obstacle
	 */
	public Obstacle(int x, int y, boolean isSolid,
			CollisionType collisiontype, DrawableHandler drawer,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler, 
			String spritename)
	{
		super(x, y, DepthConstants.NORMAL, isSolid, collisiontype, drawer, 
				collidablehandler, collisionhandler, actorhandler);
		
		// Initializes attributes
		this.spritedrawer = new SingleSpriteDrawer(
				MultiMediaHolder.getSpriteBank("obstacles").getSprite(
				spritename), actorhandler, this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onCollision(ArrayList<Double> colpoints, Collidable collided, 
			double steps)
	{
		// TODO Add wall collision
	}

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// Doesn't limit colliding classes
		return null;
	}

	@Override
	public int getWidth()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getWidth();
	}

	@Override
	public int getHeight()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getHeight();
	}

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the sprite
		if (this.spritedrawer == null)
			return;
		
		this.spritedrawer.drawSprite(g2d, 0, 0);
	}

	@Override
	public void act(double steps)
	{
		super.act(steps);
		
		// Adds gravity
		addMotion(270, 0.75 * steps);
		
		// Bounces from walls / floor / ceiling
		//if (getX() - getOriginX() < 0)
		// TODO: Create wall bounce,
		// TODO: Fix the collision system
	}
}
