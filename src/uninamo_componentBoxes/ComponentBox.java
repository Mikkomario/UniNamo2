package uninamo_componentBoxes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TurnHandler;
import uninamo_main.GameSettings;
import uninamo_worlds.Area;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * ComponentBox is a box from which the user can drag components away from. 
 * This is the main method of creating new components
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public abstract class ComponentBox extends AbstractButton
{
	// ATTRIBUTES	------------------------------------------------------
	
	private Area area;
	private TestHandler testHandler;
	private ConnectorRelay connectorRelay;
	private TurnHandler turnHandler;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new ComponentBox with the given component type and position
	 * 
	 * @param x The x-coordinate of the box
	 * @param y The y-coordinate of the box
	 * about mouse events
	 * @param area The area where the box is located
	 * @param testHandler The testHandler that will inform objects about test 
	 * events
	 * @param connectorRelay The connectorRelay that will handle the created 
	 * connectors 
	 * @param turnHandler The turnHandler that will inform the objects about 
	 * turn events 
	 */
	public ComponentBox(int x, int y, Area area, TestHandler testHandler, 
			ConnectorRelay connectorRelay, TurnHandler turnHandler)
	{
		super(x, y, DepthConstants.BACK - 20, MultiMediaHolder.getSpriteBank(
				"gameplayinterface").getSprite("componentbox"), area.getDrawer(), 
				area.getMouseHandler(), area);
		
		// Initializes attributes
		this.area = area;
		this.testHandler = testHandler;
		this.connectorRelay = connectorRelay;
		this.turnHandler = turnHandler;
		
		getSpriteDrawer().setImageSpeed(0);
		getSpriteDrawer().setImageIndex(0);
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * @return The text shown in the front of the box
	 */
	protected abstract String getText();
	
	/**
	 * Creates a new component using the given data
	 * 
	 * @param x The new x-coordinate of the component
	 * @param y The new y-coordinate of the component
	 * @param drawer The drawer that will draw the component
	 * @param actorHandler The actorHandler that will animate the component
	 * @param mouseHandler The mouseHandler that will inform the object about 
	 * mouse events
	 * @param room The room where the component will reside at
	 * @param testHandler The testHandler that will inform the object about 
	 * test events
	 * @param connectorRelay The connectorRelay that holds the connectors
	 * @param turnHandler The turnHandler that will inform the component about 
	 * turn events
	 */
	protected abstract void createComponent(int x, int y, DrawableHandler drawer, 
			ActorHandler actorHandler, MouseListenerHandler mouseHandler, 
			Room room, TestHandler testHandler, ConnectorRelay connectorRelay, 
			TurnHandler turnHandler);
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D.Double mousePosition,
			double eventStepTime)
	{
		// On left click, creates a new component
		if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
			createComponent((int) mousePosition.getX(), 
					(int) mousePosition.getY(), this.area.getDrawer(), 
					this.area.getActorHandler(), this.area.getMouseHandler(), 
					this.area, this.testHandler, this.connectorRelay, 
					this.turnHandler);
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D.Double mousePosition, double eventStepTime)
	{
		// On enter, scales a bit and opens the box
		if (eventType == MousePositionEventType.ENTER)
		{
			//setScale(GameSettings.interfaceScaleFactor, 
			//		GameSettings.interfaceScaleFactor);
			getSpriteDrawer().setImageIndex(1);
		}
		// On exit, rescales and closes the box
		else if (eventType == MousePositionEventType.EXIT)
		{
			//setScale(1, 1);
			getSpriteDrawer().setImageIndex(0);
		}
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		super.drawSelfBasic(g2d);
		
		// Also draws the text
		g2d.setFont(GameSettings.basicFont);
		g2d.setColor(Color.WHITE);
		g2d.drawString(getText(), 5, 28);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/*
	private void createNewComponent()
	{
		try
		{
			if (TurnBased.class.isAssignableFrom(this.componentClass))
			{
				this.componentClass.getConstructor(Integer.class, Integer.class, 
						DrawableHandler.class, ActorHandler.class, 
						MouseListenerHandler.class, Room.class, 
						TestHandler.class, ConnectorRelay.class, 
						TurnHandler.class).newInstance((int) getX(), (int) getY(), this.area.getDrawer(), 
								this.area.getActorHandler(), this.area.getMouseHandler(), 
								this.area, this.testHandler, this.connectorRelay, 
								this.turnHandler);

			}
			else
			{
				this.componentClass.getConstructor(Integer.class, Integer.class, 
						DrawableHandler.class, 
						ActorHandler.class, MouseListenerHandler.class, Room.class, 
						TestHandler.class, ConnectorRelay.class).newInstance(
								(int) getX(), (int) getY(), this.area.getDrawer(), this.area.getActorHandler(), 
								this.area.getMouseHandler(), this.area, this.testHandler, 
								this.connectorRelay);
			}
		}
		catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e)
		{
			System.err.println("ComponentBox failed to create an instance "
					+ "of a component");
			e.printStackTrace();
		}
	}
	*/
}
