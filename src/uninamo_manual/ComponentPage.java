package uninamo_manual;

import java.awt.Graphics2D;

import uninamo_components.ComponentType;
import uninamo_gameplaysupport.TurnHandler;
import utopia_gameobjects.GameObject;
import utopia_worlds.Area;

/**
 * ComponentPage features a short description of a component as well as a 
 * testable component.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class ComponentPage extends DescriptionPage implements Page
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private ComponentType featuredComponentType;
	private TurnHandler turnHandler;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new componentPage to the given position featuring the 
	 * given component type
	 * 
	 * @param x The x-coordinate of the center of the page
	 * @param y The y-coordinate of the center of the page
	 * @param area The area where the test component will be put into
	 * @param turnHandler The turnHandler that will inform the test component 
	 * about turn events
	 * @param featuredType The ComponentType that will be demonstrated on this 
	 * page
	 * @param componentData The infoHolder that holds the data regarding the 
	 * components
	 */
	public ComponentPage(int x, int y, Area area, TurnHandler turnHandler, 
			ComponentType featuredType, ComponentInfoHolder componentData)
	{
		super(x, y, area, componentData.getComponentData(featuredType), 
				featuredType.getName());
		
		// Initializes attributes
		this.featuredComponentType = featuredType;
		this.turnHandler = turnHandler;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the normal stuff
		super.drawSelfBasic(g2d);
		
		// And a note
		g2d.drawString("Click the cables to test", 32, 128);
	}

	@Override
	protected GameObject createTestObject(Area area)
	{
		return this.featuredComponentType.getNewComponent((int) getX(), 
				(int) getY() - 50, area.getDrawer(), area.getActorHandler(), 
				area.getMouseHandler(), area, null, null, null, null, 
				this.turnHandler, true);
	}
}
