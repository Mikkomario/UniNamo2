package utopia_handlers;

import utopia_handleds.Handled;
import utopia_listeners.SoundListener;
import utopia_sound.Sound;

/**
 * Soundlistenerhandler informs multiple listeners about sound events of sounds 
 * it listens to
 *
 * @author Mikko Hilpinen.
 *         Created 19.8.2013.
 */
public class SoundListenerHandler extends LogicalHandler implements SoundListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	// TODO: Update to handlingoperators?
	private SoundEvent lastevent;
	private Sound lastsound;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new soundlistenerhandler and adds it to the given handler (if 
	 * possible)
	 *
	 * @param autodeath Will the handler automatically die when it runs out 
	 * of handleds
	 * @param superhandler The soundhandler that will handle the handler
	 */
	public SoundListenerHandler(boolean autodeath, SoundListenerHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.lastevent = SoundEvent.START;
		this.lastsound = null;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected Class<?> getSupportedClass()
	{
		return SoundListener.class;
	}

	@Override
	public void onSoundStart(Sound source)
	{
		informListeners(SoundEvent.START, source);
	}

	@Override
	public void onSoundEnd(Sound source)
	{
		informListeners(SoundEvent.END, source);
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		// Informs all the listeners about a new event
		SoundListener s = (SoundListener) h;
		
		if (!s.isActive())
			return true;
		
		if (this.lastevent == SoundEvent.START)
			s.onSoundStart(this.lastsound);
		else if (this.lastevent == SoundEvent.END)
			s.onSoundEnd(this.lastsound);
		
		return true;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Adds a new listener to the informed listeners
	 *
	 * @param s The listener to be informed
	 */
	public void addListener(SoundListener s)
	{
		addHandled(s);
	}
	
	private void informListeners(SoundEvent event, Sound source)
	{
		// Updates status
		this.lastevent = event;
		this.lastsound = source;
		// Informs listeners
		handleObjects();
		// Forgets the sound
		this.lastsound = null;
	}
	
	
	// ENUMERATIONS	-------------------------------------------------------
	
	private enum SoundEvent
	{
		START, END;
	}
}
