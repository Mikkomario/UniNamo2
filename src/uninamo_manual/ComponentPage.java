package uninamo_manual;

import java.awt.Color;
import java.awt.Graphics2D;

import uninamo_components.Component;
import uninamo_components.ComponentType;
import uninamo_gameplaysupport.TurnHandler;
import uninamo_main.GameSettings;
import utopia_gameobjects.DrawnObject;
import utopia_graphic.TextDrawer;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_worlds.Room;

/**
 * ComponentPage features a short description of a component as well as a 
 * testable component.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class ComponentPage extends DrawnObject implements Page
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Component testComponent;
	private ComponentType featuredComponentType;
	private TextDrawer textDrawer;
	private DrawableHandler drawer;
	private ActorHandler actorHandler;
	private TurnHandler turnHandler;
	private MouseListenerHandler mouseHandler;
	private Room room;
	
	
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
	 * @param room The room where the test component will be put into
	 * @param turnHandler The turnHandler that will inform the test component 
	 * about turn events
	 * @param featuredType The ComponentType that will be demonstrated on this 
	 * page
	 * @param componentData The infoHolder that holds the data regarding the 
	 * components
	 */
	public ComponentPage(int x, int y, DrawableHandler drawer, 
			ActorHandler actorHandler, MouseListenerHandler mouseHandler, 
			Room room, TurnHandler turnHandler, 
			ComponentType featuredType, ComponentInfoHolder componentData)
	{
		super(x, y, DepthConstants.NORMAL, drawer);
		
		// Initializes attributes
		this.testComponent = null;
		this.featuredComponentType = featuredType;
		this.drawer = drawer;
		this.actorHandler = actorHandler;
		this.mouseHandler = mouseHandler;
		this.turnHandler = turnHandler;
		this.room = room;
		this.textDrawer = new TextDrawer(
				componentData.getComponentData(featuredType), 
				GameSettings.basicFont, Color.BLACK, 
				ManualMaster.MANUALWIDTH / 2 - 50);
		
		// Is invisible until opened
		setInvisible();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void open()
	{
		this.testComponent = this.featuredComponentType.getNewComponent(
				(int) getX(), (int) getY() - 128, this.drawer, this.actorHandler, 
				this.mouseHandler, this.room, null, null, this.turnHandler, true);
		setVisible();
	}

	@Override
	public void close()
	{
		this.testComponent.kill();
		setInvisible();
	}

	@Override
	public int getOriginX()
	{
		return ManualMaster.MANUALWIDTH / 4;
	}

	@Override
	public int getOriginY()
	{
		return ManualMaster.MANUALHEIGHT / 2;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the text
		if (this.textDrawer != null)
			this.textDrawer.drawText(g2d, 32, 200);
		
		// Draws the headline
		g2d.drawString(this.featuredComponentType.getName(), 32, 50);
	}

	@Override
	public void kill()
	{
		// Also kills the test component
		this.testComponent.kill();
		this.textDrawer = null;
		
		super.kill();
	}
}
