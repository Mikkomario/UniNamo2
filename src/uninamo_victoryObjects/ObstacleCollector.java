package uninamo_victoryObjects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.Testable;
import uninamo_obstacles.Obstacle;
import utopia_gameobjects.DrawnObject;
import utopia_graphic.MultiSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_handleds.Collidable;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_listeners.CollisionListener;
import utopia_listeners.TransformationListener;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * ObstacleCollectors require a certain number of a certain object to fill up. 
 * A stage is cleared once all the obstacleCollectors are filled
 * 
 * @author Mikko Hilpinen
 * @since 11.3.2014
 */
public class ObstacleCollector extends DrawnObject implements CollisionListener, 
	Testable, TransformationListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Class<?> collectedClass;
	private int neededAmount;
	private MultiSpriteDrawer spriteDrawer;
	private Point2D.Double[] relativeColPoints, absoluteColPoints;
	private boolean colPointsNeedUpdating, active;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new obstacleCollector that collects obstacles of the given 
	 * type. The collector needs to collect neededAmount obstacles before it 
	 * is filled.
	 * 
	 * @param x The x-coordinate of the collector
	 * @param y The y-coordinate of the collector
	 * @param drawer The drawableHandler that will draw the collector
	 * @param collisionHandler The collisionHandler that will inform the object 
	 * about collision events
	 * @param testHandler The testHandler that will inform the object about test 
	 * events
	 * @param collectedClass The class the collector collects (must extend Obstacle class)
	 * @param neededAmount How many obstacles are collected before the collector is filled
	 * @param designSpriteName The name of the sprite used to draw the object in design mode
	 * @param realSpriteName The name of the sprite used to draw the object in test mode
	 */
	public ObstacleCollector(int x, int y, DrawableHandler drawer, 
			CollisionHandler collisionHandler, TestHandler testHandler, 
			Class<?> collectedClass, int neededAmount, String designSpriteName, 
			String realSpriteName)
	{
		super(x, y, DepthConstants.BACK, drawer);
		
		// Initializes attributes
		this.collectedClass = collectedClass;
		this.neededAmount = neededAmount;
		
		Sprite[] sprites = new Sprite[2];
		sprites[0] = MultiMediaHolder.getSpriteBank("goals").getSprite(designSpriteName);
		sprites[1] = MultiMediaHolder.getSpriteBank("goals").getSprite(realSpriteName);
		// TODO: Need animation later?
		this.spriteDrawer = new MultiSpriteDrawer(sprites, null, this);
		
		this.relativeColPoints = new Point2D.Double[1];
		this.relativeColPoints[0] = new Point2D.Double(getOriginX(), getOriginY());
		this.colPointsNeedUpdating = true;
		
		// Adds the object to the handler(s)
		if (collisionHandler != null)
			collisionHandler.addCollisionListener(this);
		if (testHandler != null)
			testHandler.addTestable(this);
		
		// Checks if the class is acceptable
		if (!Obstacle.class.isAssignableFrom(this.collectedClass))
		{
			System.err.println("ObstacleCollector doesn't support class " + 
					this.collectedClass.getName());
			kill();
		}
		
		forceTransformationUpdate();
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
	public Double[] getCollisionPoints()
	{
		// Updates the collision points if needed
		if (this.relativeColPoints == null)
			return new Point2D.Double[0];
		
		if (this.colPointsNeedUpdating)
			this.absoluteColPoints = 
					transformMultipleRelativePoints(this.relativeColPoints);
		
		return this.absoluteColPoints;
	}

	@Override
	public void onCollision(ArrayList<Double> colpoints, Collidable collided,
			double steps)
	{
		if (this.collectedClass.isInstance(collided))
		{
			// Collects the collided object
			Obstacle o = (Obstacle) collided;
			
			o.kill();
			this.neededAmount --;
			
			if (this.neededAmount == 0)
			{
				inactivate();
				this.spriteDrawer.setImageIndex(1);
			}
		}
	}

	@Override
	public int getOriginX()
	{
		if (this.spriteDrawer == null)
			return 0;
		else
			return this.spriteDrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		if (this.spriteDrawer == null)
			return 0;
		else
			return this.spriteDrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		if (this.spriteDrawer == null)
			return;
		
		this.spriteDrawer.drawSprite(g2d, 0, 0);
	}

	@Override
	public void startTesting()
	{
		// Changes image & activates
		this.spriteDrawer.setSpriteIndex(1, true);
		activate();
	}

	@Override
	public void endTesting()
	{
		this.spriteDrawer.setSpriteIndex(0, true);
		inactivate();
	}

	@Override
	public void onTransformationEvent(TransformationEvent e)
	{
		// Updates the collision points
		this.colPointsNeedUpdating = true;
	}
}
