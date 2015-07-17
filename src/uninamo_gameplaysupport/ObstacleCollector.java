package uninamo_gameplaysupport;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import arc_bank.Bank;
import conflict_collision.CollisionChecker;
import conflict_collision.CollisionEvent;
import conflict_collision.CollisionInformation;
import conflict_collision.CollisionListener;
import exodus_world.Area;
import exodus_world.AreaListener;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import omega_util.Transformation;
import uninamo_gameplaysupport.TestEvent.TestEventType;
import uninamo_main.Utility;
import uninamo_obstacles.ObstacleType;
import vision_drawing.SimpleSingleSpriteDrawerObject;
import vision_sprite.MultiSpriteDrawer;
import vision_sprite.Sprite;
import vision_sprite.SpriteBank;

/**
 * ObstacleCollectors require a certain number of a certain object to fill up. 
 * A stage is cleared once all the obstacleCollectors are filled
 * 
 * @author Mikko Hilpinen
 * @since 11.3.2014
 */
public class ObstacleCollector extends SimpleGameObject implements CollisionListener, 
	TestListener, AreaListener, Drawable
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int neededAmount, collectsLeft;
	private MultiSpriteDrawer spriteDrawer;
	private VictoryHandler victoryHandler;
	private SimpleSingleSpriteDrawerObject numberDrawer, collectableDrawer;
	private Transformation transformation;
	private CollisionInformation collisionInfo;
	private CollisionChecker collisionChecker;
	private StateOperator isVisibleOperator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new obstacleCollector that collects obstacles of the given 
	 * type. The collector needs to collect neededAmount obstacles before it 
	 * is filled.
	 * @param handlers The handlers that will handle the collector
	 * @param position The position of the collector's origin
	 * @param victoryHandler The victoryHandler that will check if the stage has been beaten
	 * @param collectedType The type of obstacle the collector collects
	 * @param neededAmount How many obstacles are collected before the collector is filled
	 * @param designSpriteName The name of the sprite used to draw the object in design mode
	 * @param realSpriteName The name of the sprite used to draw the object in test mode
	 */
	public ObstacleCollector(HandlerRelay handlers, Vector3D position,  
			VictoryHandler victoryHandler, ObstacleType collectedType, 
			int neededAmount, String designSpriteName, String realSpriteName)
	{
		super(handlers);
		
		// Initializes attributes
		Bank<Sprite> spriteBank = SpriteBank.getSpriteBank("goals");
		Sprite[] sprites = {spriteBank.get(designSpriteName), spriteBank.get(realSpriteName)};
		// TODO: Need animation later?
		this.spriteDrawer = new MultiSpriteDrawer(sprites, this, handlers);
		
		this.neededAmount = neededAmount;
		this.victoryHandler = victoryHandler;
		this.collectsLeft = this.neededAmount;
		this.transformation = new Transformation(position);
		this.isVisibleOperator = new StateOperator(true, true);
		this.collisionInfo = new CollisionInformation(Utility.getSpriteVertices(
				this.spriteDrawer.getSprite()));
		this.collisionChecker = new CollisionChecker(this, false, false);
		Class<?>[] collided = {collectedType.getObstacleClass()};
		this.collisionChecker.limitCheckedClassesTo(collided);
		
		this.numberDrawer = new SimpleSingleSpriteDrawerObject(getDepth() - 2, 
				SpriteBank.getSprite("gameplayinterface", "numbers"), handlers);
		this.numberDrawer.getDrawer().getSpriteDrawer().setImageSpeed(0);
		this.numberDrawer.getDrawer().getSpriteDrawer().setImageIndex(this.neededAmount);
		this.numberDrawer.getDrawer().setIsVisibleOperator(getIsVisibleStateOperator());
		this.numberDrawer.setIsActiveStateOperator(getIsActiveStateOperator());
		this.numberDrawer.setIsDeadStateOperator(getIsDeadStateOperator());
		
		this.collectableDrawer = new SimpleSingleSpriteDrawerObject(getDepth() - 1, 
				collectedType.getSprite(), handlers);
		this.collectableDrawer.getDrawer().setIsVisibleOperator(getIsVisibleStateOperator());
		this.collectableDrawer.setIsActiveStateOperator(getIsActiveStateOperator());
		this.collectableDrawer.setIsDeadStateOperator(getIsDeadStateOperator());
		updateDrawerTransformations();
		
		getIsActiveStateOperator().setState(false);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public StateOperator getCanBeCollidedWithStateOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public CollisionInformation getCollisionInformation()
	{
		return this.collisionInfo;
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
		updateDrawerTransformations();
	}

	@Override
	public void onAreaStateChange(Area area, boolean newState)
	{
		// Dies at the end of the room
		if (!newState)
			getIsDeadStateOperator().setState(true);
	}

	@Override
	public void onTestEvent(TestEvent event)
	{
		// Changes image & activates
		if (event.getType() == TestEventType.START)
		{
			this.spriteDrawer.setSpriteIndex(1, true);
			this.collectsLeft = this.neededAmount;
			getIsActiveStateOperator().setState(true);
		}
		else
		{		
			this.spriteDrawer.setSpriteIndex(0, true);
			this.numberDrawer.getDrawer().getSpriteDrawer().setImageIndex(this.neededAmount);
			getIsActiveStateOperator().setState(false);
		}
	}

	@Override
	public CollisionChecker getCollisionChecker()
	{
		return this.collisionChecker;
	}

	@Override
	public StateOperator getListensForCollisionStateOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public void onCollisionEvent(CollisionEvent event)
	{
		// Collects the collided object
		// TODO: Disable the obstacle
		/*
		Obstacle o = (Obstacle) collided;
		
		o.makeUnsolid();
		o.inactivate();
		o.setInvisible();
		*/
		
		this.collectsLeft --;
		this.numberDrawer.getDrawer().getSpriteDrawer().setImageIndex(this.collectsLeft);
		
		if (this.collectsLeft == 0)
		{
			getIsActiveStateOperator().setState(false);
			this.spriteDrawer.setImageIndex(1);
			// Informs the victoryHandler of the new status
			this.victoryHandler.recheck();
		}
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Draws the sprite
		AffineTransform lastTransform = getTransformation().transform(g2d);
		this.spriteDrawer.drawSprite(g2d);
		g2d.setTransform(lastTransform);
	}

	@Override
	public int getDepth()
	{
		return DepthConstants.BACK;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}
	
	
	// OTHER METHODS	----------------
	
	private void updateDrawerTransformations()
	{
		Vector3D dimensions = this.spriteDrawer.getSprite().getDimensions().times(
				getTransformation().getScaling());
		Vector3D drawerDimensions = dimensions.dividedBy(new Vector3D(2, 1));
		Vector3D topLeft = getTransformation().getPosition().minus(
				this.spriteDrawer.getOrigin().times(getTransformation().getScaling()));
		
		this.numberDrawer.setTrasformation(getTransformation().withPosition(topLeft));
		this.collectableDrawer.setTrasformation(getTransformation().withPosition(
				topLeft.plus(new Vector3D(drawerDimensions.getFirst(), 0))));
		
		this.numberDrawer.getDrawer().scaleToSize(drawerDimensions);
		this.collectableDrawer.getDrawer().scaleToSize(drawerDimensions);
	}
}
