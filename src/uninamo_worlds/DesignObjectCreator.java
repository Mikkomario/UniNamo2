package uninamo_worlds;

import uninamo_components.NormalComponentRelay;
import uninamo_gameplaysupport.InvisibleWall;
import uninamo_gameplaysupport.TestHandler;
import uninamo_main.GameSettings;
import uninamo_userinterface.DemoButton;
import uninamo_userinterface.TestingButton;
import uninamo_userinterface.ToCodeButton;

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
	private TestHandler testHandler;
	private NormalComponentRelay componentRelay;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new objectCreator. The creator will create the objects when 
	 * the area starts.
	 * 
	 * @param areaChanger The areaChanger that handles different areas
	 * @param testHandler The testHandler that will inform the objects about 
	 * test events
	 * @param componentRelay The componentRelay that holds the information 
	 * about created components
	 */
	public DesignObjectCreator(AreaChanger areaChanger, 
			TestHandler testHandler, NormalComponentRelay componentRelay)
	{
		super(areaChanger.getArea("design"), "paper", "gameplaybackgrounds");
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.componentRelay = componentRelay;
		this.testHandler = testHandler;
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
		new ToCodeButton(this.areaChanger, this.testHandler, testButton, 
				demoButton, this.componentRelay);
	}
}
