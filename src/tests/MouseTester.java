package tests;

import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_event.MouseEvent;
import genesis_event.MouseListener;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;

/**
 * This object prints mouse data
 * @author Huoltokäyttis
 * @since 17.7.2015
 */
public class MouseTester extends SimpleGameObject implements MouseListener
{
	// ATTRIBUTES	---------------
	
	private EventSelector<MouseEvent> selector;
	private StateOperator listensOperator;
	
	
	// CONSTRUCTOR	------------------
	
	/**
	 * Creates a new tester
	 * @param handlers The handlers that will handle the object
	 */
	public MouseTester(HandlerRelay handlers)
	{
		super(handlers);
		
		this.selector = MouseEvent.createMouseMoveSelector();
		this.listensOperator = new StateOperator(true, false);
	}
	
	
	// IMPLEMENTED METHODS	----------

	@Override
	public StateOperator getListensToMouseEventsOperator()
	{
		return this.listensOperator;
	}

	@Override
	public EventSelector<MouseEvent> getMouseEventSelector()
	{
		return this.selector;
	}

	@Override
	public boolean isInAreaOfInterest(Vector3D position)
	{
		return false;
	}

	@Override
	public void onMouseEvent(MouseEvent event)
	{
		System.out.println(event.getPosition());
	}
}
