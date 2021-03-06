package uninamo_main;

import genesis_graphic.GamePanel;
import genesis_graphic.GameWindow;

import java.awt.BorderLayout;

import omega_graphic.OpenSpriteBankHolder;
import arc_bank.MultiMediaHolder;
import arc_bank.OpenGamePhaseBankHolder;
import arc_resource.MetaResource;
import tests.FpsApsTest;
import uninamo_worlds.AreaChanger;

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
		// TODO: Make using gameWindow less cryptic and easier
		
		// Initializes resources
		/*
		MultiMediaHolder.initializeResourceDatabase(ResourceType.SPRITE, 
				"configure/spriteload.txt");
		MultiMediaHolder.initializeResourceDatabase(ResourceType.GAMEPHASE, 
				"configure/gamephaseload.txt");
		*/
		MultiMediaHolder.initializeResourceDatabase(
				new OpenSpriteBankHolder("configure/spriteload.txt"));
		MultiMediaHolder.initializeResourceDatabase(
				new OpenGamePhaseBankHolder("configure/gamephaseload.txt"));
		
		// Creates the main window
		GamePanel gamepanel = new GamePanel(GameSettings.screenWidth, 
				GameSettings.screenHeight);
		GameWindow window = new GameWindow(GameSettings.screenWidth, 
				GameSettings.screenHeight, "UniNamo2", !GameSettings.fullScreen, 
				120, 10, false);
		window.addGamePanel(gamepanel, BorderLayout.CENTER);
		if (GameSettings.fullScreen)
			window.setFullScreen(true);
		
		// Starts the gameplay area(s)
		MultiMediaHolder.activateBank(MetaResource.GAMEPHASE, "default", true);
		
		// TODO: Add key handler when needed
		AreaChanger areachanger = new AreaChanger(window, gamepanel);
		areachanger.getArea("design").start();
		areachanger.getArea("coding").start();
		areachanger.getArea("mission").start();
		
		// TODO: This causes occasional errors
		areachanger.getArea("coding").getMouseHandler().inactivate();
		areachanger.getArea("coding").getDrawer().setInvisible();
		
		// Also activates FPS test
		new FpsApsTest(window.getStepHandler(), gamepanel.getDrawer());
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
