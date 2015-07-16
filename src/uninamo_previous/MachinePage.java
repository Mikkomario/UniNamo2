package uninamo_previous;

import omega_world.Area;
import omega_world.GameObject;
import uninamo_machinery.Machine;
import uninamo_machinery.MachineType;

/**
 * MachinePage features a single machine and contains a short description as 
 * well as a test version of that machine
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 * @deprecated Replaced with a method in page
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
	 * @param area The area where the manual is located at
	 * @param featuredMachineType The MachineType shown on this page
	 * @param infoHolder MachineInfoHolder that contains the descriptions of machines
	 */
	public MachinePage(int x, int y, Area area, 
			MachineType featuredMachineType, MachineInfoHolder infoHolder)
	{
		super(x, y, area, 
				infoHolder.getMachineDescription(featuredMachineType), 
				featuredMachineType.getName());
		
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
