package uninamo_main;

import java.awt.BorderLayout;

import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_video.GamePanel;
import utopia_video.GameWindow;

/**
 * This is the main class of UniNamo2. The class initializes the necessary 
 * systems and starts the game. The class also features the main method.
 * 
 * @author Mikko Hilpinen
 * @since 7.3.2014
 */
public class Main
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new main and starts the game. Opens the game in a new window.
	 */
	public Main()
	{
		// Initializes resources
		// TODO: Initialize resources
		
		// Initializes handlers
		ActorHandler mainactorhandler = new ActorHandler(false, null);
		DrawableHandler maindrawer = new DrawableHandler(false, true, 
				DepthConstants.NORMAL, null);
		//KeyListenerHandler mainkeyhandler = new KeyListenerHandler(false, null);
		MouseListenerHandler mainmousehandler = new MouseListenerHandler(false, null, null);
		
		// Creates the main window
		GamePanel gamepanel = new GamePanel(GameSettings.screenWidth, 
				GameSettings.screenHeight);
		GameWindow window = new GameWindow(GameSettings.screenWidth, 
				GameSettings.screenHeight, "UniNamo2", !GameSettings.fullScreen, 
				120, 10, false);
		window.addGamePanel(gamepanel, BorderLayout.CENTER);
		if (GameSettings.fullScreen)
			window.setFullScreen(true);
		
		// Adds the handlers to the window
		window.addActor(mainactorhandler);
		gamepanel.getDrawer().addDrawable(maindrawer);
		//window.addKeyListener(mainkeyhandler);
		window.addActor(mainmousehandler);
		window.addMouseListener(mainmousehandler);
	}
	
	
	// MAIN METHOD	-----------------------------------------------------
	
	/**
	 * Starts the game
	 * 
	 * @param args Not used by the software
	 */
	public static void main(String[] args)
	{
		new Main();
	}
}
