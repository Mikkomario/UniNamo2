package uninamo_components;

import gateway_event.ButtonEvent;
import gateway_event.ButtonEventListener;
import gateway_event.ButtonEvent.ButtonEventType;
import gateway_ui.SingleSpriteButton;
import genesis_event.Drawable;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.Vector3D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import uninamo_main.GameSettings;
import uninamo_userinterface.CurrentCostDrawer;
import vision_sprite.SpriteBank;

/**
 * ComponentBox is a box from which the user can drag components away from. 
 * This is the main method of creating new components
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class ComponentBox extends SingleSpriteButton implements ButtonEventListener, Drawable
{
	// TODO: There's no real need to extend the button. Just handle the button's from a 
	// controller class
	
	// ATTRIBUTES	------------------------------------------------------
	
	private HandlerRelay handlers;
	private ConnectorRelay connectorRelay;
	private ComponentType componentType;
	private CurrentCostDrawer costDrawer;
	private EventSelector<ButtonEvent> selector;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new ComponentBox with the given component type and position
	 * 
	 * @param position The boxes position
	 * @param handlers The handlers that will handle the box
	 * @param connectorRelay The connectorRelay that will handle the created 
	 * connectors
	 * @param costDrawer The costDrawer that will be affected by the created 
	 * components
	 * @param componentType The type of component that will be dragged from the box
	 */
	public ComponentBox(Vector3D position, HandlerRelay handlers, 
			ConnectorRelay connectorRelay, CurrentCostDrawer costDrawer, 
			ComponentType componentType)
	{
		super(position, DepthConstants.BACK - 20, SpriteBank.getSprite("gameplayinterface", 
				"componentbox"), handlers);
		
		// Initializes attributes
		this.connectorRelay = connectorRelay;
		this.componentType = componentType;
		this.costDrawer = costDrawer;
		this.handlers = handlers;
		this.selector = ButtonEvent.createButtonEventSelector(ButtonEventType.PRESSED);
		
		getSpriteDrawer().setImageSpeed(0);
		getSpriteDrawer().setImageIndex(0);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getTransformation().transform(g2d);
		// Also draws the text
		g2d.setFont(GameSettings.basicFont);
		g2d.setColor(Color.WHITE);
		g2d.drawString(this.componentType.toString(), 5, 28);
		g2d.setTransform(lastTransform);
	}

	@Override
	public EventSelector<ButtonEvent> getButtonEventSelector()
	{
		return this.selector;
	}

	@Override
	public StateOperator getListensToButtonEventsOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public void onButtonEvent(ButtonEvent e)
	{
		// On left click, creates a new component
		this.componentType.getNewComponent(this.handlers, getTransformation().getPosition(), 
				this.connectorRelay, this.costDrawer, false);
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
