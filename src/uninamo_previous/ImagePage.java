package uninamo_previous;

import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import omega_util.Transformation;
import uninamo_manual.ManualMaster;
import vision_drawing.SimpleSingleSpriteDrawerObject;
import vision_sprite.Sprite;

/**
 * This page contains a single image that is scaled to fill the page area.
 * 
 * @author Mikko Hilpinen
 * @deprecated Use the new implementations instead
 */
public class ImagePage extends SimpleGameObject implements Page, StateOperatorListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SimpleSingleSpriteDrawerObject drawer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new page containing the given image
	 * @param handlers The handlers that will handle the page
	 * @param position The position of the top left corner of the page
	 * @param image The image shown on the page
	 */
	public ImagePage(HandlerRelay handlers, Vector3D position, Sprite image)
	{
		super(handlers);
		
		// Initializes attributes
		this.drawer = new SimpleSingleSpriteDrawerObject(DepthConstants.BACK, image, handlers);
		this.drawer.getDrawer().getSpriteDrawer().setOrigin(Vector3D.zeroVector());
		this.drawer.setTrasformation(new Transformation(position));
		this.drawer.getDrawer().scaleToSize(ManualMaster.MANUALDIMENSIONS.dividedBy(
				new Vector3D(2, 1)));
		
		// Hides the image
		setOpenState(false);
		
		getIsDeadStateOperator().getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void setOpenState(boolean newState)
	{
		this.drawer.getDrawer().getIsVisibleStateOperator().setState(newState);
	}

	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		if (source.equals(getIsDeadStateOperator()))
			this.drawer.getIsDeadStateOperator().setState(newState);
	}
}
