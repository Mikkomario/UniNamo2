package uninamo_worlds;

import exodus_world.Area;
import exodus_world.AreaBank;
import exodus_world.AreaListener;
import gateway_ui.AbstractButton;
import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.InvisibleWall;
import uninamo_gameplaysupport.TotalCostAnalyzer;
import uninamo_gameplaysupport.VictoryHandler;
import uninamo_machinery.MachineCounter;
import uninamo_main.GameSettings;
import uninamo_main.UninamoHandlerType;
import uninamo_userinterface.DesignInterface;
import uninamo_userinterface.ScalingSpriteButton;

/**
 * DesignObjectCreator creates the objects needed in the design area. It also 
 * handles the background of the said area.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class DesignObjectCreator extends SimpleGameObject implements AreaListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private TotalCostAnalyzer costAnalyzer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new objectCreator. The creator will create the objects when 
	 * the area starts.
	 * @param handlers The handlers used for handling the object
	 * @param costAnalyzer The costAnalyzer that will keep track of the machine 
	 * costs
	 */
	public DesignObjectCreator(HandlerRelay handlers, TotalCostAnalyzer costAnalyzer)
	{
		super(handlers);
		
		// Initializes attributes
		this.costAnalyzer = costAnalyzer;
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onAreaStateChange(Area area, boolean newState)
	{
		if (newState)
		{
			HandlerRelay handlers = area.getHandlers();
			HandlerRelay codingHandlers = AreaBank.getArea("gameplay", "coding").getHandlers();
			
			// Creates the interface
			AbstractButton finishButton = ScalingSpriteButton.createButton(
					GameSettings.resolution.dividedBy(2), handlers, "finish");
			finishButton.getListensToMouseEventsOperator().setState(false);
			finishButton.getIsVisibleStateOperator().setState(false);
			// Victory handler can only be added at this point
			VictoryHandler victoryHandler = new VictoryHandler(finishButton);
			handlers.addHandler(victoryHandler, true);
			new DesignInterface(handlers, codingHandlers, finishButton);
			
			// Creates invisible walls
			new InvisibleWall(new Vector3D(-32, 0), new Vector3D(32, 
					GameSettings.resolution.getSecond()), handlers);
			new InvisibleWall(new Vector3D(0, -32), new Vector3D(
					GameSettings.resolution.getFirst(), 32), handlers);
			new InvisibleWall(new Vector3D(GameSettings.resolution.getFirst(), 0), new Vector3D(32, 
					GameSettings.resolution.getSecond()), handlers);
			new InvisibleWall(new Vector3D(0, GameSettings.resolution.getSecond()), 
					new Vector3D(GameSettings.resolution.getFirst(), 32), handlers);
			
			// Creates other objects
			MachineCounter counter = new MachineCounter(this.costAnalyzer);
			new DesignInitializer(handlers, codingHandlers, counter, (ConnectorRelay) 
					handlers.getHandler(UninamoHandlerType.CONNECTOR), victoryHandler);
			new MissionInitializer(handlers);
		}
	}
}
