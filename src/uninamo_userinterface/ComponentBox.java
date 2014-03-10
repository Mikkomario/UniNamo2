package uninamo_userinterface;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import uninamo_main.GameSettings;
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
public class ComponentBox extends AbstractButton
{
	// ATTRIBUTES	------------------------------------------------------
	
	private String text;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new ComponentBox with the given component type and position
	 * 
	 * @param x The x-coordinate of the box
	 * @param y The y-coordinate of the box
	 * @param drawer The drawableHandler that will draw the box
	 * @param mousehandler The mouseListenerHandler that will inform the box 
	 * about mouse events
	 * @param room The room in which the box resides
	 */
	public ComponentBox(int x, int y, DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room)
	{
		super(x, y, DepthConstants.BACK - 20, MultiMediaHolder.getSpriteBank(
				"gameplayinterface").getSprite("componentbox"), drawer, 
				mousehandler, room);
		
		this.text = "asdasd";
		
		getSpriteDrawer().setImageSpeed(0);
		getSpriteDrawer().setImageIndex(0);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{
		// TODO: Create a new component
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
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
		g2d.drawString(this.text, 5, 28);
	}
}
