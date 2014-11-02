package tests;

import genesis_graphic.DepthConstants;
import genesis_graphic.Drawable;
import genesis_graphic.DrawableHandler;
import genesis_logic.Actor;
import genesis_logic.ActorHandler;

import java.awt.Graphics2D;

/**
 * FpsAps tester draws the current FPS (frames per second) and APS 
 * (actions per second) to the screen
 *
 * @author Mikko Hilpinen.
 *         Created 15.6.2013.
 */
public class FpsApsTest implements Actor, Drawable
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean active;
	private boolean visible;
	private boolean alive;
	private long lastmillis;
	private int fps;
	private int aps;
	private int frames;
	private int actions;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new test with the required information
	 * 
	 * @param actorhandler The handler that handles created actors
	 * @param drawer The drawer that draws created drawables
	 */
	public FpsApsTest(ActorHandler actorhandler, DrawableHandler drawer)
	{
		// Initializes the attributes
		this.active = true;
		this.visible = true;
		this.alive = true;
		this.lastmillis = System.currentTimeMillis();
		this.fps = 0;
		this.aps = 0;
		this.frames = 0;
		this.actions = 0;
		
		// Adds the object to the handlers
		actorhandler.addActor(this);
		drawer.addDrawable(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

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
		return !this.alive;
	}

	@Override
	public void kill()
	{
		this.alive = false;
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Calculates the fps
		this.frames ++;
		
		// Draws the current fps and aps
		g2d.drawString("FPS: " + this.fps, 50, 100);
		g2d.drawString("APS: " + this.aps, 50, 130);
	}

	@Override
	public boolean isVisible()
	{
		return this.visible;
	}

	@Override
	public void setVisible()
	{
		this.visible = true;
	}

	@Override
	public void setInvisible()
	{
		this.visible = false;
	}

	@Override
	public void act(double steps)
	{
		// Calculates the aps and fps
		this.actions ++;
		
		if (System.currentTimeMillis() - this.lastmillis > 1000)
		{
			this.aps = this.actions;
			this.fps = this.frames;
			this.actions = 0;
			this.frames = 0;
			this.lastmillis = System.currentTimeMillis();
		}
	}
	
	@Override
	public int getDepth()
	{
		// The test is always drawn on top
		return DepthConstants.TOP;
	}

	@Override
	public boolean setDepth(int depth)
	{
		// The depth of the test can't be changed
		return false;
	}
}
