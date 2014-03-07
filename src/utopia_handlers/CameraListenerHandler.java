package utopia_handlers;

import utopia_handleds.Handled;
import utopia_listeners.CameraListener;

/**
 * This class informs all its sublisteners about changes in camera's position
 *
 * @author Mikko Hilpinen.
 *         Created 7.12.2012.
 * @deprecated TransformationListenerHandler should be used instead
 */
public class CameraListenerHandler extends LogicalHandler implements CameraListener
{	
	// ATTRIBUTES	------------------------------------------------------
	
	private int lastx, lasty, lastw, lasth, lastangle;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new listenerhandler that will inform all sublisteners about
	 * camera's changes. Listeners must be added manually later.
	 * 
	 * @param autodeath Will the handler stop functioning when it runs out of 
	 * handled listeners
	 * @param superhandler The cameralistenerhandler that informs this handler (optional)
	 */
	public CameraListenerHandler(boolean autodeath, CameraListenerHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.lastx = 0;
		this.lasty = 0;
		this.lastw = 0;
		this.lasth = 0;
		this.lastangle = 0;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void informCameraPosition(int posx, int posy, int w, int h, int angle)
	{
		// Remembers the data
		this.lastx = posx;
		this.lasty = posy;
		this.lastw = w;
		this.lasth = h;
		this.lastangle = angle;
		
		// Goes through the listeners
		handleObjects();
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return CameraListener.class;
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		// Informs all cameralisteners about the event
		CameraListener l = (CameraListener) h;
		if (l.isActive())
			l.informCameraPosition(this.lastx, this.lasty, this.lastw, 
					this.lasth, this.lastangle);
		
		return true;
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Adds a new cameralistener to the informed cameralisteners
	 * @param c The listener to be addded
	 */
	public void addListener(CameraListener c)
	{
		super.addHandled(c);
	}
}
