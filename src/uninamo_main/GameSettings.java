package uninamo_main;

import java.awt.Color;
import java.awt.Font;

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
	/**
	 * How long a single "turn" in the game should take (in steps)
	 */
	public static final int turnDuration = 100;
	/**
	 * How fast should objects usually move horizontally to move a single 
	 * "unit" in a turn
	 */
	public static final double normalTurnSpeed = 1.72;
	/**
	 * The basic font used in the game
	 */
	public final static Font basicFont = new Font("Orc A Std", Font.PLAIN, 20);
	/**
	 * The basic white colour used in the basic texts of the game
	 */
	public final static Color whiteTextColor = new Color(235, 232, 168);
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	private GameSettings()
	{
		// Constructor is never used.
	}
}
