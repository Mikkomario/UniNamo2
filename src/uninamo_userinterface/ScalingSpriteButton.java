package uninamo_userinterface;

import uninamo_main.GameSettings;
import vision_sprite.SingleSpriteDrawer;
import vision_sprite.SpriteBank;
import gateway_ui.AbstractSpriteButton;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.Vector3D;

/**
 * This button grows larger on hoverover
 * @author Mikko Hilpinen
 * @since 14.7.2015
 */
public class ScalingSpriteButton extends AbstractSpriteButton<SingleSpriteDrawer>
{
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new button to the given location
	 * @param position The button's new position
	 * @param handlers The handlers that will handle the button
	 * @param drawer The drawer used for drawing the button
	 * @see #createButton(Vector3D, HandlerRelay, String)
	 */
	public ScalingSpriteButton(Vector3D position, HandlerRelay handlers, 
			SingleSpriteDrawer drawer)
	{
		super(position, handlers, drawer, DepthConstants.FOREGROUND);
	}
	
	
	// IMPLEMENTED METHODS	----------------

	@Override
	protected void changeVisualStyle(ButtonStatus status)
	{
		switch (status)
		{
			case DEFAULT: setScale(1); break;
			case HOVEROVER: setScale(GameSettings.interfaceScaleFactor); break;
			case PRESSED: setScale(GameSettings.interfaceScaleFactor * 
					GameSettings.interfaceScaleFactor); break;
		}
	}

	
	// OTHER METHODS	--------------------
	
	/**
	 * Creates a new button with the given stats
	 * @param position The button's position
	 * @param handlers The handlers that will handle the button
	 * @param spriteName The name of the button's sprite
	 * @return A new button
	 */
	public static ScalingSpriteButton createButton(Vector3D position, HandlerRelay handlers, 
			String spriteName)
	{
		SingleSpriteDrawer drawer = new SingleSpriteDrawer(SpriteBank.getSprite(
				"gameplayinterface", spriteName), null, handlers);
		ScalingSpriteButton button = new ScalingSpriteButton(position, handlers, drawer);
		drawer.setMaster(button);
		
		return button;
	}
	
	private void setScale(double scale)
	{
		setTrasformation(getTransformation().withScaling(new Vector3D(scale, scale)));
	}
}
