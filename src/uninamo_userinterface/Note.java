package uninamo_userinterface;

import gateway_event.ButtonEvent;
import gateway_event.ButtonEvent.ButtonEventType;
import gateway_event.ButtonEventListener;
import gateway_ui.TextDrawer;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.Vector3D;

import java.awt.Color;

import uninamo_main.GameSettings;
import vision_sprite.SingleSpriteDrawer;
import vision_sprite.Sprite;

/**
 * Notes present information about the current mission and can be clicked to get rid of
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class Note extends ScalingSpriteButton implements ButtonEventListener
{
	// ATTRIBUTES	----------------------
	
	private EventSelector<ButtonEvent> selector;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new note to the given position with the given layout
	 * @param handlers The handlers that will handle the note
	 * @param position The note's position
	 * @param margins The margins inside the note
	 * @param sprite The sprite used as the background
	 * @param content The contents of the note. # marks a paragraph change
	 */
	public Note(HandlerRelay handlers, Vector3D position, Vector3D margins, 
			Sprite sprite, String content)
	{
		super(position, handlers, new SingleSpriteDrawer(sprite, null, handlers));
		
		this.selector = ButtonEvent.createButtonEventSelector(ButtonEventType.RELEASED);
		
		getDrawer().setDepth(DepthConstants.FOREGROUND);
		getSpriteDrawer().setImageSpeed(0);
		getSpriteDrawer().setImageIndex(0);
		
		TextDrawer text = new TextDrawer(position, content, "#", GameSettings.basicFont, 
				Color.BLACK, getDimensions(), margins, getDepth() - 1, handlers);
		text.anchorToObject(this);
		text.setIsDeadStateOperator(getIsDeadStateOperator());
		text.setIsVisibleStateOperator(getIsVisibleStateOperator());
		
		getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void changeVisualStyle(ButtonStatus style)
	{
		super.changeVisualStyle(style);
		
		if (style == ButtonStatus.HOVEROVER)
			getSpriteDrawer().setImageIndex(1);
		else if (style == ButtonStatus.DEFAULT)
			getSpriteDrawer().setImageIndex(0);
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
		getIsDeadStateOperator().setState(true);
	}
}
