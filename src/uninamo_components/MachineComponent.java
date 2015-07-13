package uninamo_components;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

import java.awt.Color;
import java.awt.Graphics2D;

import uninamo_main.GameSettings;

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
	 * @param handlers The handlers that will handle the component
	 * @param position The component's position
	 * @param connectorRelay The connectorRelay that will handle the component's 
	 * connectors
	 * @param spritename The name of the sprite used to draw the component
	 * @param inputs How many input connectors the component has
	 * @param outputs How many output connectors the component has
	 * @param isForTesting Is the component created for testing purposes 
	 * (= for the manual)
	 * @param componentName The name of this component
	 */
	public MachineComponent(HandlerRelay handlers, Vector3D position, 
			ConnectorRelay connectorRelay,
			String spritename, int inputs, int outputs, boolean isForTesting, 
			String componentName)
	{
		super(handlers, position, connectorRelay, spritename, inputs, outputs, false, 
				isForTesting);
		
		// Initializes attributes
		this.name = componentName;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		super.drawSelf(g2d);
		
		if (this.name == null)
			return;
		
		// Also draws the name of the component
		g2d.setColor(Color.BLACK);
		g2d.setFont(GameSettings.basicFont);
		g2d.drawString(this.name, getTransformation().getPosition().getFirstInt(), 
				getTransformation().getPosition().getSecondInt());
	}
	
	@Override
	public String getID()
	{
		return this.name;
	}
}
