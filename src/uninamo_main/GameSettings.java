package uninamo_main;

/**
 * This is a collection of different settings used in the game.
 * 
 * @author Mikko Hilpinen
 * @since 7.3.2014
 */
public class GameSettings
{
	// ATTRIBUTES	-----------------------------------------------------
	
	/**
	 * The width of the game window and view (pixels)
	 */
	public static int screenWidth = 1280;
	/**
	 * The height of the game window and view (pixels)
	 */
	public static int screenHeight = 720;
	/**
	 * Will the game start in fullscreen mode
	 */
	public static boolean fullScreen = false;
	
	/**
	 * How much interface elements are scaled when mouse hovers over them
	 */
	public static final double interfaceScaleFactor = 1.2;
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	private GameSettings()
	{
		// Constructor is never used.
	}
}
