package utopia_helpAndEnums;

/**
 * This static class offers constants used in defining object's depth
 *
 * @author Mikko Hilpinen.
 *         Created 1.7.2013.
 */
public class DepthConstants
{
	// CONSTANTS	------------------------------------------------------
	
	/**
	 * The very bottom layer of depth.<br>
	 * Background should be drawn here
	 */
	public static final int BOTTOM = 1000;
	/**
	 * The basic layer of depth.<br>
	 * Normal objects that have no real preference should be drawn here.
	 */
	public static final int NORMAL = 0;
	/**
	 * The second-lowest layer of depth.<br>
	 * Objects that should remain on the background but still aren't 
	 * really background should be drawn here.
	 */
	public static final int BACK = 500;
	/**
	 * The highest normal layer of depth.<br>
	 * Objects that should remain on top but not above the hud should be 
	 * drawn here.
	 */
	public static final int FOREGROUND = -500;
	/**
	 * The second-highest layer of depth.<br>
	 * Hud elements and other objects that need to stay above all the normal 
	 * objects shoudl be drawn here.
	 */
	public static final int HUD = -1000;
	/**
	 * The highest layer of depth.<br>
	 * Objects that should remain even above the hud level should be drawn here.
	 */
	public static final int TOP = -1500;
}
