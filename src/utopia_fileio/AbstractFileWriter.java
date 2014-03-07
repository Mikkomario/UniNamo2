package utopia_fileio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * FileWriter writes a text file according to its subclasses commands.
 *
 * @author Mikko Hilpinen.
 *         Created 4.9.2013.
 */
public abstract class AbstractFileWriter
{
	// ATTRIBUTES	-----------------------------------------------------
	
	/**
	 * The writeLine method should return this value when it wants to stop 
	 * the writing
	 * @see #writeLine(int)
	 */
	protected final static String END_OF_STREAM = "endofstream1";
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * Returns a new line that will be written on the created file. This 
	 * method will be called until END_OF_STREAM will be returned
	 *
	 * @param lineindex What index the line has (starting from 0 and increasing 
	 * by one for each line)
	 * @return The string that will be written to that line or END_OF_STREAM if 
	 * no more lines will be written
	 * @see #END_OF_STREAM
	 */
	protected abstract String writeLine(int lineindex);
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Creates a file, overwriting a previous file with the same name if there 
	 * is one, writes data into the file by calling the writeLine method and 
	 * finally closes the file.
	 *
	 * @param filename The name of the new file created (data/ 
	 * already included)
	 */
	public void saveIntoFile(String filename)
	{
		// Generates a new filename
		String savename = "data/" + filename;
		
		// Creates the new Savefile or overwrites the old one
		File savefile = new File(savename);
		try
		{
			// Overwrites the old file
			if (!savefile.createNewFile())
			{
				savefile.delete();
				savefile.createNewFile();
			}
		}
		catch (IOException ie)
		{
			System.err.println("Error in creating / overwriting the file " + 
					savename);
		}
		
		// Writes the data into the file
		BufferedWriter bufferedWriter = null;
		
        try
        {
        	bufferedWriter = new BufferedWriter(new FileWriter(savefile));
            
            // Writes the data
        	int i = 0;
            while(true)
            {
            	String newline = writeLine(i);
            	
            	// Checks if the writing should be ended
            	if (newline.equals(END_OF_STREAM))
            		break;
            	
            	// Writes the data into the file
            	bufferedWriter.write(newline);
            	bufferedWriter.newLine();
            	i++;
            }
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Couldn't find the file " + savename);
        }
        catch (IOException ex)
        {
        	System.err.println("Failed to save the file " + savename);
        }
        finally
        {
            // Closes the writer
            try
            {
                if (bufferedWriter != null)
                {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            }
            catch (IOException ex)
            {
                System.err.println("Failed to close the file writer");
            }
        }
	}
}
