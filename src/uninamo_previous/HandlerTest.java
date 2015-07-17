package uninamo_previous;

import genesis_logic.Handled;
import genesis_logic.Handler;

/**
 * Handlertest tests the functionalities of the basic handler and handled classes.
 *
 * @author Gandalf.
 *         Created 8.10.2013.
 */
public class HandlerTest
{
	// MAIN METHOD	-----------------------------------------------------
	
	/**
	 * Runs the test
	 *
	 * @param args Not needed
	 */
	public static void main(String[] args)
	{
		// Tests basics
		basictest();
		// Tests autodeath
		autoDeathTest();
		// Tests handler handling and death sorting
		handlingTest();
	}
	
	private static void basictest()
	{
		System.out.println("\nStarts basic test");
		
		HandlerTest t = new HandlerTest();
		// Creates new handler
		TestHandler h1 = t.new TestHandler(false, null);
		// Adds handleds to the handler
		TestHandled b1 = t.new TestHandled(h1, "Basic 1");
		t.new TestHandled(h1, "Basic 2");
		t.new TestHandled(h1, "Basic 3");
		// Tests
		System.out.println("Should call Basic 1, 2 and 3");
		h1.test();
		// Removes a handled
		h1.removeHandled(b1);
		// Tests
		System.out.println("Should call only Basic 2 and 3");
		h1.test();
		// Removes all handleds
		h1.removeAllHandleds();
		// Tests
		System.out.println("No calls should happen here");
		h1.test();
		// Adds handleds and then kills the handler
		h1.addTestHandled(b1);
		h1.kill();
		// Tests
		System.out.println("Handler should be dead and empty");
		h1.test();
		
		System.out.println("Basic test complete");
	}
	
	private static void autoDeathTest()
	{
		System.out.println("\nStarts the autodeath test");
		HandlerTest t = new HandlerTest();
		
		// Creates a new autodeath handler
		TestHandler h1 = t.new TestHandler(true, null);
		// Tests
		System.out.println("The handler should be alive and empty (no calls)");
		h1.test();
		// Adds handleds
		t.new TestHandled(h1, "Auto 1");
		t.new TestHandled(h1, "Auto 2");
		t.new TestHandled(h1, "Auto 3");
		/* Tests
		System.out.println("Three handleds should be called");
		h1.test();*/
		// Removes all handleds
		h1.removeAllHandleds();
		// Tests
		System.out.println("Handler should have autodied and be empty");
		h1.test();
		
		System.out.println("AutoDeathTest complete");
	}
	
	private static void handlingTest()
	{
		HandlerTest t = new HandlerTest();
		
		System.out.println("\nStarts the handler handling test");
		// Creates four handlers that are inside each other
		TestHandler h1 = t.new TestHandler(false, null);
		TestHandler h2 = t.new TestHandler(false, h1);
		TestHandler h3 = t.new TestHandler(false, h1);
		TestHandler h4 = t.new TestHandler(false, h2);
		// Adds handleds
		t.new TestHandled(h1, "h1 Handled");
		t.new TestHandled(h2, "h2 Handled");
		t.new TestHandled(h3, "h3 Handled");
		t.new TestHandled(h4, "h4 Handled");
		// Tests
		System.out.println("Each 4 handleds should be called inside their " +
				"own handlers");
		h1.test();
		// Kills the lower handler
		h2.kill();
		// Tests
		System.out.println("Only handler 1 and 3 should remain");
		h1.test();
		
		System.out.println("Handler handling test complete");
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	private interface Testable
	{
		public void test();
	}
	
	private class TestHandled implements Handled, Testable
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private boolean dead;
		private String name;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public TestHandled(TestHandler h, String name)
		{
			// Initializes attributes
			this.dead = false;
			this.name = name;
			
			// Adds the handled to the handler
			if (h != null)
				h.addTestHandled(this);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------
		
		@Override
		public boolean isDead()
		{
			return this.dead;
		}

		@Override
		public void kill()
		{
			this.dead = true;
		}
		
		
		// OTHER METHODS	----------------------------------------------
		
		@Override
		public void test()
		{
			System.out.println(this.name + " was called");
		}
	}
	
	private class TestHandler extends Handler implements Testable
	{
		// CONSTRUCTOR	--------------------------------------------------
		
		public TestHandler(boolean autodeath, TestHandler superhandler)
		{
			super(autodeath, superhandler);
		}
		

		// IMPLEMENTED METHODS	------------------------------------------
		
		@Override
		protected Class<?> getSupportedClass()
		{
			return Testable.class;
		}

		@Override
		protected boolean handleObject(Handled h)
		{
			((Testable) h).test();
			
			return true;
		}
		
		
		// OTHER METHODS	----------------------------------------------
		
		public void addTestHandled(TestHandled h)
		{
			addHandled(h);
		}
		
		@Override
		public void test()
		{
			if (!isDead())
			{
				System.out.println("Test was called for TestHandler containing " 
						+ getHandledNumber() + " objects");
				handleObjects();
			}
			else
				System.out.println("TestHandler is dead and has " + 
						getHandledNumber() + " handleds");
		}
	}
}
