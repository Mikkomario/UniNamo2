package uninamo_manual;

import gateway_event.ButtonEvent;
import gateway_event.ButtonEventListener;
import gateway_event.ButtonEvent.ButtonEventType;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import uninamo_userinterface.ScalingSpriteButton;
import vision_sprite.SingleSpriteDrawer;
import vision_sprite.SpriteBank;

/**
 * By clicking a bookMark, the user can go to a certain section on the manual
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class BookMark extends ScalingSpriteButton implements ButtonEventListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private int sectionIndex;
	private int relativeX;
	private ManualMaster manual;
	private EventSelector<ButtonEvent> selector;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new bookMark that will take the user to the given section
	 * @param relativeToCenterX How much to the right the bookMark is from the center of the 
	 * manual (!)
	 * @param sectionIndex To which sectionthe user will be taken when this bookmark is 
	 * pressed
	 * @param master The manualMaster that handles the page changing
	 * @param handlers The handlers that will handle the bookmark
	 */
	public BookMark(int relativeToCenterX, int sectionIndex, ManualMaster master, 
			HandlerRelay handlers)
	{
		super(Vector3D.zeroVector(), handlers, new SingleSpriteDrawer(SpriteBank.getSprite(
				ManualMaster.SPRITEBANKNAME, "bookmark"), null, handlers));
		/*
		super(GameSettings.screenWidth / 2 + x, 
				GameSettings.screenHeight / 2 - ManualMaster.MANUALHEIGHT / 2, 
				DepthConstants.BOTTOM, 
				OpenSpriteBank.getSpriteBank("manual").getSprite("bookmark"), 
				area);
		*/
		
		// Initializes attributes
		this.sectionIndex = sectionIndex;
		this.relativeX = relativeToCenterX;
		this.manual = master;
		this.selector = ButtonEvent.createButtonEventSelector(ButtonEventType.RELEASED);
		
		resetPosition();
		getDrawer().setDepth(DepthConstants.BOTTOM);
		getSpriteDrawer().setImageSpeed(0);
		getSpriteDrawer().setImageIndex(sectionIndex);
		
		getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

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
		this.manual.openSection(this.sectionIndex);
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * This method should be called when a new page is opened
	 * 
	 * @param newPageIndex The index of the new section
	 */
	void onSectionChange(int newSectionIndex)
	{
		// Updates position
		int sign = 1;
		if (newSectionIndex >= this.sectionIndex)
			sign = -1;
		this.relativeX = Math.abs(this.relativeX) * sign;
		
		resetPosition();
		
		// If the page is the same as the pageIndex, changes depth, otherwise 
		// rechanges if (if necessary)
		/* TODO: There's no good way for doing this in the current version
		if (newPageIndex == this.pageIndex)
			setDepth(DepthConstants.NORMAL);
		else if (getDepth() == DepthConstants.NORMAL)
			setDepth(DepthConstants.BOTTOM);
		*/
	}
	
	private void resetPosition()
	{
		setTrasformation(getTransformation().withPosition(this.manual.getPosition().plus(
				new Vector3D(ManualMaster.MANUALDIMENSIONS.getFirst() / 2 + this.relativeX, 
				32))));
	}
}
