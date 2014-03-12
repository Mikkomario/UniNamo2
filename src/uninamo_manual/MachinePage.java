package uninamo_manual;

import java.awt.Color;

import uninamo_machinery.Machine;
import uninamo_machinery.MachineType;
import uninamo_main.GameSettings;
import uninamo_worlds.Area;
import utopia_gameobjects.GameObject;
import utopia_graphic.TextDrawer;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;

/**
 * MachinePage features a single machine and contains a short description as 
 * well as a test version of that machine
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class MachinePage extends DescriptionPage implements Page
{	
	// ATTRIBUTES	-----------------------------------------------------
	
	private MachineType featuredType;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new machinePage
	 * 
	 * @param x The x-coordinate of the center of the page
	 * @param y The y-coordinate of teh center of the page
	 * @param drawer The drawableHandler that will draw the page
	 * @param actorHandler The actorHandler that will inform the test machine 
	 * about step events
	 * @param area The area where the manual is located at
	 * @param featuredMachineType The MachineType shown on this page
	 */
	public MachinePage(int x, int y, DrawableHandler drawer, 
			ActorHandler actorHandler, Area area, 
			MachineType featuredMachineType)
	{
		super(x, y, drawer, area, new TextDrawer("This is a short description", 
				GameSettings.basicFont, Color.BLACK, ManualMaster.MANUALWIDTH 
				/ 2 - 50), featuredMachineType.getName());
		
		// Initializes attributes
		this.featuredType = featuredMachineType;
	}

	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected GameObject createTestObject(Area area)
	{
		Machine newMachine = this.featuredType.getTestMachine((int) getX(),
				(int) getY() - 125, area.getDrawer(), area.getActorHandler(), area);
		double scale = 100.0 / newMachine.getWidth();
		newMachine.setScale(scale, scale);
		return newMachine;
	}
}
