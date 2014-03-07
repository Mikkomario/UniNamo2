package utopia_listeners;

import utopia_gameobjects.DrawnObject;
import utopia_handleds.LogicalHandled;
import utopia_handlers.TransformationListenerHandler;

/**
 * TransformationListeners listen to changes in some object's transformations 
 * like scaling, translating and rotating and react to those events somehow.<br>
 * Remember to add the object into a TransformationListenerHandler
 * 
 * @author Mikko Hilpinen. 
 * Created 9.1.2014
 * @see TransformationListenerHandler
 * @see DrawnObject
 */
public interface TransformationListener extends LogicalHandled
{
	/**
	 * Here the listener reacts to the transformation event(s) it is interested 
	 * of. Only active listeners are informed though.
	 * 
	 * @param e The transformationEvent that just occured
	 */
	public void onTransformationEvent(TransformationEvent e);
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	/**
	 * TransformationEvent holds all the necessary information about the 
	 * transformation that just happened
	 * 
	 * @author Mikko Hilpinen
	 * Created at 9.1.2014
	 */
	public class TransformationEvent
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private TransformationType type;
		private TransformationAxis axis;
		private double newamount, relativechange;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		/**
		 * @param type The type of the occured transformation
		 * @param axis Which axis affected the transformation.
		 * @param newamount What is the current status of the transformation 
		 * (for example the current angle of the object)
		 * @param relativechange How much was the object's status changed 
		 * (for example how much the object was rotated during the last 
		 * transformation)
		 */
		public TransformationEvent(TransformationType type, 
				TransformationAxis axis, double newamount, double relativechange)
		{
			// Initializes attributes
			this.type = type;
			this.axis = axis;
			this.newamount = newamount;
			this.relativechange = relativechange;
		}
		
		
		// GETTTERS & SETTERS	-----------------------------------------
		
		/**
		 * @return The type of the transformation
		 */
		public TransformationType getType()
		{
			return this.type;
		}
		
		/**
		 * @return The axis which affected the transformation
		 */
		public TransformationAxis getAxis()
		{
			return this.axis;
		}
		
		/**
		 * @return The new status of the transformation (for example the 
		 * object's current x-coordinate)
		 */
		public double getNewStatus()
		{
			return this.newamount;
		}
		
		/**
		 * @return How much the status of the object was changed during the 
		 * transformation (for example how much the object was moved on the 
		 * y-axis)
		 */
		public double getTransformationAmount()
		{
			return this.relativechange;
		}
	}
	
	
	// ENUMERATIONS	-----------------------------------------------------
	
	/**
	 * TransformationType tells which kind of transformationEvent just happened
	 * 
	 * @author Mikko Hilpinen
	 * Created at 9.1.2914
	 * @see "http://docs.oracle.com/javase/7/docs/api/java/awt/geom/AffineTransform.html"
	 */
	public enum TransformationType
	{
		/**
		 * On translation the object is moved on a certain axis
		 */
		TRANSLATION, 
		/**
		 * On rotation the object is rotated around a certain axis. In 2D 
		 * programs this axis is always the z-axis.
		 */
		ROTATION, 
		/**
		 * On scaling the object is scaled either horizontally or vertically.
		 */
		SCAlING, 
		/**
		 * On shearing the object is sheared
		 */
		SHEARING;
	}
	
	/**
	 * TransformationAxis tells which axis the transformation was tied to. 
	 * For example if the axis is Y and the transformationType is TRANSLATION 
	 * then it means that the object moved vertically.
	 * 
	 * @author Mikko Hilpinen
	 * Created at 9.1.2014
	 */
	public enum TransformationAxis
	{
		/**
		 * X-axis is the horizontal axis on the screen
		 */
		X, 
		/**
		 * Y-axis is the vertical axis on the screen
		 */
		Y, 
		/**
		 * Z-axis tells how "far" the object is from the viewer. Usually not 
		 * needed in a 2D program.
		 */
		Z;
	}
}
