package utopia_resourcebanks;

import java.util.ArrayList;
import java.util.HashMap;

import utopia_fileio.FileReader;

/**
 * OpenBankHolder is an abstract class which creates and stores various OpenBanks
 * and gives access to them to it's subclasses.
 * 
 * @author Unto Solala & Mikko Hilpinen
 * 			Created 29.8.2013
 *
 */
public abstract class OpenBankHolder extends FileReader
{
	// ATTRIBUTES -----------------------------------------------------

	private HashMap<String, OpenBank> banks;
	private String lastbankname, filename;
	private ArrayList<String> lastcommands;
	private boolean initialized;

	
	// CONSTRUCTOR -----------------------------------------------------

	/**
	 * Creates and initializes new OpenBankHolder. The content is loaded using
	 * the given file.
	 * 
	 * @param filename A file that shows information about what banks to create
	 * (data/ automatically included). The file should be written
	 * as follows:
	 * <p>
	 * &bankname<br>
	 * ...<br>
	 * ...<br>
	 * &anotherbankname<br>
	 * ...<br>
	 * * this is a comment
	 * @param autoinitialize Should the holder be initialized right at the 
	 * constructor. This should be true unless the subclass needs to collect 
	 * some information before initialization, in which case initialization 
	 * must be called manually
	 */
	public OpenBankHolder(String filename, boolean autoinitialize)
	{
		// Initializes attributes
		this.initialized = false;
		this.banks = new HashMap<String, OpenBank>();
		this.lastbankname = null;
		this.lastcommands = new ArrayList<String>();
		this.filename = filename;
		
		if (autoinitialize)
			initialize();
	}
	
	
	// ABSTRACT METHODS -------------------------------------------------
	
	/**
	 * Creates and returns a new OpenBank.
	 * 
	 * @param commands	Commands given to the OpenBank.
	 * @return	Returns the new OpenBank.
	 */
	protected abstract OpenBank createBank(ArrayList<String> commands);

	
	// IMPLEMENTED METHODS ----------------------------------------------

	@SuppressWarnings("unchecked")
	@Override
	protected void onLine(String line)
	{
		// If the line starts with '&' ends the last bank and starts a new bank
		if (line.startsWith("&")) {
			if (this.lastbankname != null) {
				// System.out.println("Puts " + this.lastcommands.size() +
				// " objects to the bank " + this.lastbankname);
				this.banks.put(this.lastbankname, this.createBank(
						(ArrayList<String>) this.lastcommands.clone()));
			}
			this.lastbankname = line.substring(1);
			this.lastcommands.clear();
			return;
		}
		// Otherwise, tries to add a new command to the lastcommands
		this.lastcommands.add(line);
	}

	
	// OTHER METHODS ---------------------------------------------------

	/**
	 * Provides an OpenBank with the given name from the databanks.
	 * 
	 * @param bankname	The name of the Bank
	 * @return The Bank with the given name or null if no Bank was found
	 * @warning It's usually easier to simply use another getter provided by 
	 * a subclass.
	 */
	public OpenBank getBank(String bankname)
	{
		if (this.banks.containsKey(bankname))
			return this.banks.get(bankname);
		else if (this.initialized)
		{
			System.err.println("The OpenBankHolder doesn't hold a bank "
					+ "named " + bankname);
			return null;
		}
		else
		{
			System.err.println("The OpenBankHolder hasn't been initialized yet");
			return null;
		}
	}

	/**
	 * Uninitializes all the banks held by this object
	 */
	public void uninitializeBanks() {
		// Goes through all the banks and uninitializes them
		for (OpenBank bank : this.banks.values()) {
			bank.uninitialize();
		}
	}
	
	/**
	 * Initializes the BankHolder if it hasn't been initialized already
	 */
	@SuppressWarnings("unchecked")
	protected void initialize()
	{
		if (this.initialized)
			return;
		
		this.initialized = true;
		
		// Reads the file
		readFile(this.filename, "*");
		// Adds the last Bank and releases the memory
		if (this.lastcommands.size() > 0)
		{
			// System.out.println("Puts " + this.lastcommands.size() +
			// " objects to the bank " + this.lastbankname);
			this.banks.put(this.lastbankname, this.createBank(
					(ArrayList<String>) this.lastcommands.clone()));
		}
			
		this.lastcommands.clear();
		this.lastbankname = null;
	}
}
