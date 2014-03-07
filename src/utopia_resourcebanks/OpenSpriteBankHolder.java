package utopia_resourcebanks;

import java.util.ArrayList;


/**
 * This class holds numerous OpenSpriteBanks and provides them to the objects 
 * that need them. The holder loads the banks using a specific file
 *
 * @author Mikko Hilpinen & Unto Solala.
 *         Created 26.8.2013.
 */
public class OpenSpriteBankHolder extends OpenBankHolder
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates and initializes new OpenSpriteBankHolder. The content is loaded 
	 * using the given file.
	 *
	 * @param filename A file that shows information about what banks to create 
	 * (data/ automatically included). 
	 * The file should be written as follows:<p>
	 * 
	 * &bankname<br>
	 * spritename#filename(data/ automatically included)#number of images#
	 * xorigin#yorigin<br>
	 * anotherspritename#...<br>
	 * ...<br>
	 * &anotherbankname<br>
	 * ...<br>
	 * * this is a comment
	 */
	public OpenSpriteBankHolder(String filename)
	{
		super(filename, true);
	}
	
	
	// OTHER METHODS	--------------------------------------------------

	/**
	 * Looks for the OpenBank matching the given bankName and if it is found,
	 * casts it into OpenSpriteBank and returns it. If not found, returns null.
	 * 
	 * @param bankName	The OpenBank which is needed.
	 * @return Returns the needed OpenBank, if it is found and casts it into
	 * OpenSpriteBank. If not found, return null.
	 */
	public OpenSpriteBank getOpenSpriteBank(String bankName)
	{
		OpenBank maybeOpenSpriteBank = getBank(bankName);
		
		if(maybeOpenSpriteBank instanceof OpenSpriteBank)
			return (OpenSpriteBank) maybeOpenSpriteBank;
		else
			return null;
	}
	
	@Override
	protected OpenBank createBank(ArrayList<String> commands)
	{
		return new OpenSpriteBank(commands);
	}
}
