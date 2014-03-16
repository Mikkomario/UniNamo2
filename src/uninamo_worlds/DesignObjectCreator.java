package uninamo_worlds;

import uninamo_gameplaysupport.InvisibleWall;
import uninamo_gameplaysupport.TestHandler;
import uninamo_main.GameSettings;
import uninamo_userinterface.CodeTransitionButton;
import uninamo_userinterface.DemoButton;
import uninamo_userinterface.TestingButton;

/**
 * DesignObjectCreator creates the objects needed in the design area. It also 
 * handles the background of the said area.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class DesignObjectCreator extends AreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private AreaChanger areaChanger;
	//private ConnectorRelay connectorRelay;
	private TestHandler testHandler;
	//private TurnTimer timer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new objectCreator. The creator will create the objects when 
	 * the area starts.
	 * 
	 * @param areaChanger The areaChanger that handles different areas
	 * @param testHandler The testHandler that will inform the objects about 
	 * test events
	 */
	public DesignObjectCreator(AreaChanger areaChanger, 
			TestHandler testHandler/*, 
			TurnTimer turnTimer*/)
	{
		super(areaChanger.getArea("design"), "paper", "gameplaybackgrounds");
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		//this.connectorRelay = connectorRelay;
		this.testHandler = testHandler;
		//this.timer = turnTimer;
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected void createObjects(Area area)
	{
		// Creates the victoryHandler
		//VictoryHandler victoryHandler = new VictoryHandler(null);
		
		// Creates the invisible walls
		new InvisibleWall(0, 1, 0, 
				area.getCollisionHandler().getCollidableHandler(), area);
		new InvisibleWall(0, -1, GameSettings.screenWidth, 
				area.getCollisionHandler().getCollidableHandler(), area);
		/*
		new InvisibleWall(1, 0, 0, 
				area.getCollisionHandler().getCollidableHandler(), area);
		*/
		new InvisibleWall(-1, 0, GameSettings.screenHeight - 80, 
				area.getCollisionHandler().getCollidableHandler(), area);
		
		// Creates objects
		DemoButton demoButton = new DemoButton(600, 80, area.getDrawer(), 
				area.getMouseHandler(), area, this.testHandler);
		TestingButton testButton = new TestingButton(GameSettings.screenWidth - 110, 
				GameSettings.screenHeight - 45, area.getDrawer(), 
				area.getMouseHandler(), area, this.testHandler);
		new CodeTransitionButton(area.getDrawer(), area.getMouseHandler(), 
				area, CodeTransitionButton.TOCODE, this.areaChanger, 
				this.testHandler, testButton, demoButton);
		/*
		
		new ConveyorBelt(400, GameSettings.screenHeight - 200, 
				area.getDrawer(), area.getActorHandler(), 
				area.getCollisionHandler().getCollidableHandler(), 
				area.getCollisionHandler(), this.areaChanger.getArea("coding"), 
				area, this.testHandler, this.connectorRelay, false);
		
		new ConveyorBelt(580, GameSettings.screenHeight - 200, 
				area.getDrawer(), area.getActorHandler(), 
				area.getCollisionHandler().getCollidableHandler(),
				area.getCollisionHandler(), this.areaChanger.getArea("coding"), 
				area, this.testHandler, this.connectorRelay, false);
		*/
		/*
		new Box(430, 200, area.getDrawer(), 
				area.getCollisionHandler().getCollidableHandler(), 
				area.getCollisionHandler(), area.getActorHandler(), area, 
				this.testHandler);
		
		*//*
		new Box(600, 200, area.getDrawer(), 
				area.getCollisionHandler().getCollidableHandler(), 
				area.getCollisionHandler(), area.getActorHandler(), area, 
				this.testHandler);
		*/
		/*
		for (int i = 0; i < 5; i++)
		{
			new Box(400, 200 - 200 * i, area.getDrawer(), 
					area.getCollisionHandler().getCollidableHandler(), 
					area.getCollisionHandler(), area.getActorHandler(), area, 
					this.testHandler);
		}
		*/
		/*
		new ObstacleCollector(800, GameSettings.screenHeight - 200, area.getDrawer(), 
				area.getCollisionHandler(), this.testHandler, victoryHandler, 
				ObstacleType.BOX, 2, "boxdesign", "boxreal");
		*/
	}
}
