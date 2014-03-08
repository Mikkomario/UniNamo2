package utopia_resourceHandling;

import java.util.HashMap;

import utopia_resourcebanks.BankObject;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * A single gamePhase represents a separated phase in the game. Multiple 
 * gamePhases aren't usually active at the same time. Each phase is tied to 
 * a certain set of resources that will be activated when the phase starts.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class GamePhase implements BankObject
{
	// ATTRIBUTES	------------------------------------------------------
	
	private String name;
	private HashMap<ResourceType, String[]> connectedBankNames;
	private boolean dead;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty gamePhase with the given name. Connected resource 
	 * banks have to be separately added afterwards.
	 * 
	 * @param name The name of the phase
	 */
	public GamePhase(String name)
	{
		// Initializes attributes
		this.dead = false;
		this.name = name;
		this.connectedBankNames = new HashMap<ResourceType, String[]>();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	@Override
	public void kill()
	{
		this.dead = true;
	}

	@Override
	public boolean isDead()
	{
		return this.dead;
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * Returns a table containing the names of all the resourceBanks of the 
	 * given type used during this GamePhase
	 * 
	 * @param type The type of resourceBanks the caller is interested of
	 * @return A table containing the names of the resourceBanks of the 
	 * given type used during this phase. If the resourceType hasn't been 
	 * introduced to the phase, a table with a size of 0 is returned.
	 */
	public String[] getConnectedResourceBankNames(ResourceType type)
	{
		String[] names = this.connectedBankNames.get(type);
		
		if (names == null)
		{
			System.err.println("ResourceType " + type + "in GamePhase " + this 
					+ " hasn't been introduced.");
			return new String[0];
		}
		
		// Clones the table just in case the caller would try to edit the 
		// contents
		return names.clone();
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * @return The name of the phase
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Connects a resourceBank to the gamePhase so that it will be recognized 
	 * as being used in the phase.
	 * 
	 * @param bankType The type of resource the bank holds
	 * @param bankNames A table containing the names of the connected 
	 * resourceBanks of the given type. These should be same names that 
	 * are used by the resourceBanks' holder and / or MultiMediaHolder.
	 * @see MultiMediaHolder
	 */
	public void connectResourceBankNames(ResourceType bankType, String[] bankNames)
	{
		this.connectedBankNames.put(bankType, bankNames);
	}
}
