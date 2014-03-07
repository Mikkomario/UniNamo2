package utopia_resourcebanks;

/**
 * OpenBanks contain a number of elements and offer those elements for other 
 * objects. Openbanks in particular are banks that are initialized in-game using 
 * configuration files. OpenBanks are usually held in OpenBankHolders.
 *
 * @author Unto Solala
 * 			Created 29.8.2013
 * @see OpenBankHolder
 */
public interface OpenBank
{
	/**
	 * Uninitializes the contents of the bank. The bank will be reinitialized 
	 * when something is tried to retrieve from it
	 * 
	 * @warning calling this method while the objects in the bank are in use 
	 * may crash the program depending on the circumstances. It would be safe 
	 * to uninitialize the bank only when the content is not in use.
	 */
	public void uninitialize();
	
	/**
	 * Initializes all the resources in the bank so they can be used immediately
	 */
	public void initializeBank();
}
