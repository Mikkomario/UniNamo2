package utopia_resourceHandling;

import java.util.ArrayList;

import utopia_resourcebanks.MultiMediaHolder;

/**
 * ResourceActivator activates resources from certain gamePhases at a time. 
 * Resources that aren't used in the current gamePhase are deactivated until 
 * they are needed again. The class is completely static so no instance is 
 * needed.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class ResourceActivator
{
	// ATTRIBUTES	------------------------------------------------------
	
	private static GamePhase currentPhase = null;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	private ResourceActivator()
	{
		// the Constuctor is hidden since the class is completely static
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Stops the current phase and starts a new one, activating and 
	 * deactivating resources in the process.
	 * 
	 * @param phase The new gamePhase to be started
	 */
	public static void startPhase(GamePhase phase)
	{
		// Updates the loaded resources
		for (ResourceType type : ResourceType.values())
		{
			updateResourceBanks(phase, type);
		}
		
		// Remembers the new active phase
		currentPhase = phase;
	}
	
	private static void updateResourceBanks(GamePhase newphase, 
			ResourceType resourcetype)
	{
		String[] newbanknames = 
				newphase.getConnectedResourceBankNames(resourcetype);
		
		// Skips the removal process if there was no previous phase
		if (currentPhase != null)
		{
			String[] oldbanknames = 
					currentPhase.getConnectedResourceBankNames(resourcetype);
			
			// Takes all the new banknames into a list format
			ArrayList<String> newbanknamelist = new ArrayList<String>();
			for (int i = 0; i < newbanknames.length; i++)
			{
				newbanknamelist.add(newbanknames[i]);
			}
			
			// Removes the old banks that aren't active in the new phase
			for (int i = 0; i < oldbanknames.length; i++)
			{
				String oldbankname = oldbanknames[i];
				
				if (!newbanknamelist.contains(oldbankname))
				{
					System.out.println("Deactivates: " + oldbankname);
					MultiMediaHolder.deactivateBank(resourcetype, oldbankname);
				}
			}
		}
		
		// Adds all the new banks that weren't active already
		// (Checking done in the addSpritebank method)
		for (int i = 0; i < newbanknames.length; i++)
		{
			System.out.println("Activates: " + newbanknames[i]);
			MultiMediaHolder.activateBank(resourcetype, newbanknames[i], true);
		}
	}
}
