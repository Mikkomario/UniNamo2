package uninamo_worlds;

import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.InvisibleWall;
import uninamo_machinery.ConveyorBelt;
import uninamo_main.GameSettings;
import uninamo_obstacles.Box;
import uninamo_userinterface.CodeTransitionButton;

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
	private ConnectorRelay connectorRelay;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new objectCreator. The creator will create the objects when 
	 * the area starts.
	 * 
	 * @param areaChanger The areaChanger that handles different areas
	 * @param connectorRelay The connectorRelay that is used in the coding area.
	 */
	public DesignObjectCreator(AreaChanger areaChanger, 
			ConnectorRelay connectorRelay)
	{
		super(areaChanger.getArea("design"), "paper", "gameplaybackgrounds");
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.connectorRelay = connectorRelay;
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected void createObjects(Area area)
	{
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
		new CodeTransitionButton(area.getDrawer(), area.getMouseHandler(), 
				area, CodeTransitionButton.TOCODE, this.areaChanger);
		new ConveyorBelt(400, GameSettings.screenHeight - 200, 
				area.getDrawer(), area.getActorHandler(), 
				area.getCollisionHandler(), this.areaChanger.getArea("coding"), 
				this.connectorRelay);
		new Box(400, 200, area.getDrawer(), 
				area.getCollisionHandler().getCollidableHandler(), 
				area.getCollisionHandler(), area.getActorHandler(), area).startTest();;
	}
}
