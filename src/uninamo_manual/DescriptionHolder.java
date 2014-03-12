package uninamo_manual;

import java.util.HashMap;

import utopia_fileio.FileReader;

/**
 * DescriptionHolder reads and holds descriptions of different things
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class DescriptionHolder extends FileReader
{
	// ATTRIBUTES	------------------------------------------------------

	private HashMap<String, String> data;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates and fills a new descriptionHolder. The data is read from the 
	 * provided file.
	 * 
	 * @param dataLocation The name of the file the data is read from (data/ 
	 * automatically included)
	 */
	public DescriptionHolder(String dataLocation)
	{
		// Initializes attributes
		this.data = new HashMap<String, String>();
		
		// Reads the data
		readFile(dataLocation, "*");
	}
	
	
	// IMPLEMENTED METHODS

	@Override
	protected void onLine(String line)
	{
		// The format of a line should be ComponentType#Description
		String[] arguments = line.split("#");
		
		if (arguments.length != 2)
		{
			System.err.println("The line: " + line + " doesn't hold a correct "
					+ "amount of arguments. DescriptionHolder requires 2 "
					+ "arguments per line to work");
			return;
		}
		
		this.data.put(arguments[0], arguments[1]);
	}

	/**
	 * Finds and returns a description for the given string from the 
	 * collected data.
	 * 
	 * @param keyWord The keyword that tells which data is accessed
	 * @return The data describing the given word
	 */
	protected String getData(String keyWord)
	{
		String results = this.data.get(keyWord);
		
		if (results == null)
			return "No data available";
		else
			return results;
	}
}
