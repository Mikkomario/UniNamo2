package uninamo_gameplay;

/**
 * SignalRelays send signals to signalReceivers. SignalRelays should be able 
 * to inform objects of their signal type and send events when their signal changes.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public interface SignalRelay
{
	/**
	 * @return The current (or last known) status of the signal that is being 
	 * relayed
	 */
	public boolean getSignalStatus();
}
