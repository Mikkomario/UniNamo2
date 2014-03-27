package uninamo_gameplaysupport;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import uninamo_obstacles.Obstacle;
import uninamo_obstacles.ObstacleType;
import utopia_gameobjects.DrawnObject;
import utopia_graphic.MultiSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_handleds.Collidable;
import utopia_helpAndEnums.DepthConstants;
import utopia_listeners.CollisionListener;
import utopia_listeners.RoomListener;
import utopia_listeners.TransformationListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;

/**
 * ObstacleCollectors require a certain number of a certain object to fill up. 
 * A stage is cleared once all the obstacleCollectors are filled
 * 
 * @author Mikko Hilpinen
 * @since 11.3.2014
 */
public class ObstacleCollector extends DrawnObject implements CollisionListener, 
	TestListener, TransformationListener, RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private ObstacleType collectedType;
	private int neededAmount, collectsLeft;
	private MultiSpriteDrawer spriteDrawer;
	private Point2D.Double[] relativeColPoints, absoluteColPoints;
	private boolean colPointsNeedUpdating, active;
	private VictoryHandler victoryHandler;
	private SpriteDrawerObject numberDrawer;
	private SpriteDrawerObject collectableDrawer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new obstacleCollector that collects obstacles of the given 
	 * type. The collector needs to collect neededAmount obstacles before it 
	 * is filled.
	 * 
	 * @param area The area where the object will reside at
	 * @param x The x-coordinate of the collector
	 * @param y The y-coordinate of the collector
	 * @param testHandler The testHandler that will inform the object about test 
	 * events
	 * @param victoryHandler The victoryHandler that will check if the stage has been beaten
	 * @param collectedType The type of obstacle the collector collects
	 * @param neededAmount How many obstacles are collected before the collector is filled
	 * @param designSpriteName The name of the sprite used to draw the object in design mode
	 * @param realSpriteName The name of the sprite used to draw the object in test mode
	 */
	public ObstacleCollector(Area area, int x, int y, TestHandler testHandler, 
			VictoryHandler victoryHandler, ObstacleType collectedType, 
			int neededAmount, String designSpriteName, String realSpriteName)
	{
		super(x, y, DepthConstants.BACK, area);
		
		// Initializes attributes
		this.collectedType = collectedType;
		this.neededAmount = neededAmount;
		this.victoryHandler = victoryHandler;
		this.collectsLeft = this.neededAmount;
		
		Sprite[] sprites = new Sprite[2];
		sprites[0] = MultiMediaHolder.getSpriteBank("goals").getSprite(designSpriteName);
		sprites[1] = MultiMediaHolder.getSpriteBank("goals").getSprite(realSpriteName);
		// TODO: Need animation later?
		this.spriteDrawer = new MultiSpriteDrawer(sprites, null, this);
		
		this.relativeColPoints = new Point2D.Double[1];
		this.relativeColPoints[0] = new Point2D.Double(getOriginX(), getOriginY());
		this.colPointsNeedUpdating = true;
		
		this.numberDrawer = new SpriteDrawerObject(area, DepthConstants.BACK - 1, 
				this, MultiMediaHolder.getSpriteBank(
				"gameplayinterface").getSprite("numbers"));
		this.numberDrawer.getSpriteDrawer().setImageSpeed(0);
		
		int width = this.spriteDrawer.getSprite().getWidth();
		int height = this.spriteDrawer.getSprite().getHeight();
		this.numberDrawer.setSize(width / 2 - 10, height - 10);
		this.numberDrawer.getSpriteDrawer().setImageIndex(this.neededAmount);
		this.numberDrawer.addPosition(- width / 4, 0);
		
		this.collectableDrawer = new SpriteDrawerObject(area, 
				DepthConstants.BACK - 1, this, this.collectedType.getSprite());
		this.collectableDrawer.setSize(width / 2 - 10, height - 10);
		this.collectableDrawer.addPosition(width / 4, 0);
		
		// Adds the object to the handler(s)
		if (area.getCollisionHandler() != null)
			area.getCollisionHandler().addCollisionListener(this);
		if (testHandler != null)
			testHandler.addTestable(this);
		
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
		if (this.collectedType.getObstacleClass().isInstance(collided))
		{
			// Collects the collided object
			Obstacle o = (Obstacle) collided;
			
			o.makeUnsolid();
			o.inactivate();
			o.setInvisible();
			
			this.collectsLeft --;
			this.numberDrawer.getSpriteDrawer().setImageIndex(this.collectsLeft);
			
			if (this.collectsLeft == 0)
			{
				inactivate();
				this.spriteDrawer.setImageIndex(1);
				// Informs the victoryHandler of the new status
				this.victoryHandler.recheck();
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
		
		drawRelativePoints(g2d, this.relativeColPoints);
	}

	@Override
	public void onTestStart()
	{
		// Changes image & activates
		this.spriteDrawer.setSpriteIndex(1, true);
		this.collectsLeft = this.neededAmount;
		activate();
	}

	@Override
	public void onTestEnd()
	{
		this.spriteDrawer.setSpriteIndex(0, true);
		this.numberDrawer.getSpriteDrawer().setImageIndex(this.neededAmount);
		inactivate();
	}

	@Override
	public void onTransformationEvent(TransformationEvent e)
	{
		// Updates the collision points
		this.colPointsNeedUpdating = true;
		
		int width = this.spriteDrawer.getSprite().getWidth();
		
		this.numberDrawer.setPosition(getX() - width / 4, getY());
		this.collectableDrawer.setPosition(getX() + width / 4, getY());
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies at the end of the room
		kill();
	}
}
