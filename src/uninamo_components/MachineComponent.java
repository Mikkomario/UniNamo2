package uninamo_components;

import java.awt.Color;
import java.awt.Graphics2D;

import uninamo_gameplaysupport.TestHandler;
import uninamo_main.GameSettings;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_worlds.Room;

/**
 * MachineComponents are components that are tied into a single machine. 
 * MachineComponents can't be moved like normal components but they 
 * draw their name beside them.
 * 
 * @author Mikko Hilpinen
 * @since 14.3.2014
 */
public abstract class MachineComponent extends Component
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private String name;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new machineComponent with the given stats
	 * 
	 * @param x The x-coordinate of the component
	 * @param y The y-coordinate of the component
	 * @param drawer The drawableHandler that will draw the component
	 * @param actorhandler The actorHandler that will animate the component 
	 * (optional)
	 * @param mousehandler The mouseHandler that will inform the component about 
	 * mouse events
	 * @param room The room where the component will reside at
	 * @param testHandler The testHandler that will inform the component about 
	 * test events
	 * @param connectorRelay The connectorRelay that will handle the component's 
	 * connectors
	 * @param spritename The name of the sprite used to draw the component
	 * @param inputs How many input connectors the component has
	 * @param outputs How many output connectors the component has
	 * @param isForTesting Is the component created for testing purposes 
	 * (= for the manual)
	 * @param componentName The name of this component
	 */
	public MachineComponent(int x, int y, DrawableHandler drawer,
			ActorHandler actorhandler, MouseListenerHandler mousehandler,
			Room room, TestHandler testHandler, ConnectorRelay connectorRelay,
			String spritename, int inputs, int outputs, boolean isForTesting, 
			String componentName)
	{
		super(x, y, drawer, actorhandler, mousehandler, room, testHandler,
				connectorRelay, spritename, inputs, outputs, false,
				isForTesting);
		
		// Initializes attributes
		this.name = componentName;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		super.drawSelfBasic(g2d);
		
		// Also draws the name of the component
		g2d.setColor(Color.BLACK);
		g2d.setFont(GameSettings.basicFont);
		g2d.drawString(this.name, 0, 0);
	}
	
	@Override
	public String getID()
	{
		return this.name;
	}
}
