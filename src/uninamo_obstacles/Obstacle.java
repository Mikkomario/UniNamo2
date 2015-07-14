package uninamo_obstacles;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import arc_bank.Bank;
import exodus_world.Area;
import exodus_world.AreaListener;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import omega_util.Transformable;
import omega_util.Transformation;
import uninamo_gameplaysupport.TestEvent;
import uninamo_gameplaysupport.TestListener;
import vision_sprite.MultiSpriteDrawer;
import vision_sprite.Sprite;
import vision_sprite.SpriteBank;

/**
 * obstacles are the main 'objects' in the game in a sense that the user is 
 * trying to affect them. Obstacles interact with some of the machinery but 
 * are more often the passive part in the interaction.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public abstract class Obstacle extends SimpleGameObject implements 
	AreaListener, TestListener, Drawable, Transformable
{
	// TODO: Any collision systems, as well as any physics have been (temporarily) removed
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private MultiSpriteDrawer spritedrawer;
	private boolean started;
	private Transformation startState, transformation;
	private StateOperator isVisibleOperator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 *  Creates a new obstacle to the given position. Remember to set up 
	 *  the collision points after creating the object.
	 * @param handlers The handlers that will handle the obstacle
	 * @param position The obstacle's position
	 * @param designSpriteName The name of the sprite used to draw the object 
	 * in the design mode
	 * @param realSpriteName The name of the sprite used to draw the object 
	 * during the test mode 
	 */
	public Obstacle(HandlerRelay handlers, Vector3D position, 
			String designSpriteName, String realSpriteName)
	{
		super(handlers);
		
		// Initializes attributes
		Bank<Sprite> spriteBank = SpriteBank.getSpriteBank("obstacles");
		Sprite[] sprites = {spriteBank.get(designSpriteName), spriteBank.get(realSpriteName)};
		this.spritedrawer = new MultiSpriteDrawer(sprites, this, handlers);
		this.started = false;
		this.transformation = new Transformation(position);
		this.isVisibleOperator = new StateOperator(true, true);
		this.startState = getTransformation();
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * Resets the status of the object back to the original. The position 
	 * of the object need not be reseted since it is done automatically.
	 */
	protected abstract void resetStatus();
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void onTestEvent(TestEvent event)
	{
		this.started = event.testRunning();
		
		// Changes the object's graphics and starts it
		if (this.started)
			getSpriteDrawer().setSpriteIndex(1, false);
		else
		{
			// Returns back to the original position and sprite
			getSpriteDrawer().setSpriteIndex(0, false);
			setTrasformation(this.startState);
			getIsActiveStateOperator().setState(true);
			getIsVisibleStateOperator().setState(true);
			resetStatus();
		}
	}

	@Override
	public void onAreaStateChange(Area area)
	{
		if (!area.getIsActiveStateOperator().getState())
			getIsDeadStateOperator().setState(true);
	}

	@Override
	public Transformation getTransformation()
	{
		return this.transformation;
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.transformation = t;
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Draws the sprite
		AffineTransform lastTransform = getTransformation().transform(g2d);
		if (this.spritedrawer == null)
			return;
		this.spritedrawer.drawSprite(g2d);
		g2d.setTransform(lastTransform);
	}

	@Override
	public int getDepth()
	{
		return DepthConstants.NORMAL;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The spriteDrawer used to draw the object
	 */
	protected MultiSpriteDrawer getSpriteDrawer()
	{
		return this.spritedrawer;
	}
}
