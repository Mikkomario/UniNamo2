package uninamo_gameplay;

/**
 * SignalReceivers react to changes in a binary signal and should be informed 
 * about these changes.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public interface SignalReceiver
{
	/**
	 * This method should be called when the signal the receiver is interested 
	 * of changes.
	 * @param newSignalStatus The new status of the signal
	 * @param source The signalRelay that informed the receiver about the 
	 * change. Null if the signal didn't come through a relay
	 */
	public void onSignalChange(boolean newSignalStatus, SignalRelay source);
}
