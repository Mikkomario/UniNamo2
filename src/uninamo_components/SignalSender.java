package uninamo_components;

/**
 * SignalSenders send signals to signalReceivers. SignalSenders should be able 
 * to inform objects of their signal type and send events when their signal changes.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public interface SignalSender
{
	/**
	 * @return The current (or last known) status of the signal that is being 
	 * sended
	 */
	public boolean getSignalStatus();
}
