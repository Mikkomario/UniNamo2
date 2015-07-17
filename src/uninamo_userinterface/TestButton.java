package uninamo_userinterface;

import uninamo_gameplaysupport.TestEvent;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TestEvent.TestEventType;
import vision_sprite.SingleSpriteDrawer;
import vision_sprite.SpriteBank;
import gateway_event.ButtonEvent;
import gateway_event.ButtonEventListener;
import gateway_event.ButtonEvent.ButtonEventType;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.Vector3D;

/**
 * The test button initiates or ends testing mode when pressed
 * @author Mikko Hilpinen
 * @since 14.7.2015
 */
public class TestButton extends ScalingSpriteButton implements
		ButtonEventListener
{
	// ATTRIBUTES	-------------------------------------------------------
	
		private boolean testing;
		private TestHandler testHandler;
		private EventSelector<ButtonEvent> selector;
		
		
		// CONSTRUCTOR	-------------------------------------------------------
		
		/**
		 * Creates a new demoButton to the given position
		 * @param handlers The handlers that will handle the button
		 * @param position The button's new position
		 * @param testHandler The testHandler that will be informed about test 
		 * (= demo) events
		 * @param spriteName The name of the sprite used for the button
		 */
		public TestButton(HandlerRelay handlers, Vector3D position, TestHandler testHandler, 
				String spriteName)
		{
			super(position, handlers, new SingleSpriteDrawer(SpriteBank.getSprite(
					"gameplayinterface", spriteName), null, handlers));
			
			// Initializes attributes
			this.testHandler = testHandler;
			this.testing = false;
			this.selector = ButtonEvent.createButtonEventSelector(ButtonEventType.RELEASED);
			
			getIsVisibleStateOperator().setState(false);
			getListenerHandler().add(this);
		}

		
		// IMPLEMENTED METHODS	---------------------------------------------

		@Override
		public EventSelector<ButtonEvent> getButtonEventSelector()
		{
			return this.selector;
		}

		@Override
		public StateOperator getListensToButtonEventsOperator()
		{
			return getIsActiveStateOperator();
		}

		@Override
		public void onButtonEvent(ButtonEvent e)
		{
			this.testing = !this.testing;
			getSpriteDrawer().setImageIndex(
					this.testing ? 1 : 0);
			this.testHandler.onTestEvent(new TestEvent(
					this.testing ? TestEventType.START : TestEventType.END));
		}
		
		/**
		 * Creates a new testButton with the default sprite
		 * @param handlers The handlers that will handle the button
		 * @param position The button's new position
		 * @param testHandler The testHandler
		 * @return The button that was created
		 */
		public static TestButton createButton(HandlerRelay handlers, Vector3D position, 
				TestHandler testHandler)
		{
			return new TestButton(handlers, position, testHandler, "testing");
		}
}
