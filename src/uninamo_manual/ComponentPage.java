package uninamo_manual;

import java.awt.Color;
import java.awt.Graphics2D;

import uninamo_components.ComponentType;
import uninamo_gameplaysupport.TurnHandler;
import uninamo_main.GameSettings;
import uninamo_worlds.Area;
import utopia_gameobjects.GameObject;
import utopia_graphic.TextDrawer;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;

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
	 * @param drawer The drawableHandler that will draw the page
	 * @param actorHandler The actorHandler that will animate the shown component
	 * @param mouseHandler The mouseListenerHandler that will inform the 
	 * test component about mouse events
	 * @param area The area where the test component will be put into
	 * @param turnHandler The turnHandler that will inform the test component 
	 * about turn events
	 * @param featuredType The ComponentType that will be demonstrated on this 
	 * page
	 * @param componentData The infoHolder that holds the data regarding the 
	 * components
	 */
	public ComponentPage(int x, int y, DrawableHandler drawer, 
			ActorHandler actorHandler, MouseListenerHandler mouseHandler, 
			Area area, TurnHandler turnHandler, 
			ComponentType featuredType, ComponentInfoHolder componentData)
	{
		super(x, y, drawer, area, new TextDrawer(
				componentData.getComponentData(featuredType), 
				GameSettings.basicFont, Color.BLACK, 
				ManualMaster.MANUALWIDTH / 2 - 50), featuredType.getName());
		
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
				area.getMouseHandler(), area, null, null, this.turnHandler, 
				true);
	}
}
