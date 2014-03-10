package utopia_handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import utopia_handleds.Handled;

/**
 * Handlers specialize in handling certain types of objects. Each handler can 
 * inform its subobjects and can be handled itself.
 *
 * @author Mikko Hilpinen.
 *         Created 8.12.2012.
 */
public abstract class Handler implements Handled
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private LinkedList<Handled> handleds;
	private ArrayList<Handled> handledstoberemoved, handledstobeadded;
	private boolean autodeath;
	private boolean killed;
	private boolean started; // Have any objects been added to the handler yet
	private boolean disabled; // Has the handler been temporarily disabled
	
	private HashMap<HandlingOperation, ReentrantLock> locks;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new handler that is still empty. Handled objects must be added 
	 * manually later. If autodeath is set on, the handled will be destroyed as 
	 * soon as it becomes empty.
	 *
	 * @param autodeath Will the handler die automatically when it becomes empty
	 * @param superhandler The handler that will handle the object (optional)
	 */
	public Handler(boolean autodeath, Handler superhandler)
	{
		// Initializes attributes
		this.autodeath = autodeath;
		this.killed = false;
		this.handleds = new LinkedList<Handled>();
		this.handledstobeadded = new ArrayList<Handled>();
		this.handledstoberemoved = new ArrayList<Handled>();
		this.started = false;
		this.disabled = false;
		this.locks = new HashMap<HandlingOperation, ReentrantLock>();
		this.locks.put(HandlingOperation.HANDLE, new ReentrantLock());
		this.locks.put(HandlingOperation.ADD, new ReentrantLock());
		this.locks.put(HandlingOperation.REMOVE, new ReentrantLock());
		
		// Tries to add itself to the superhandler
		if (superhandler != null)
			superhandler.addHandled(this);
	}
	
	
	// ABSTRACT METHODS	---------------------------------------------------
	
	/**
	 * @return The class supported by the handler
	 */
	protected abstract Class<?> getSupportedClass();
	
	/**
	 * Many handlers are supposed to do something to the handled objects. 
	 * That something should be done in this method. The method is called as 
	 * a part of the handleObjects method.
	 *
	 * @param h The handler that may need handling
	 * @return Should object handling be continued (true) or skipped for the 
	 * remaining handleds (false)
	 */
	protected abstract boolean handleObject(Handled h);
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public boolean isDead()
	{
		// The handler is dead if it was killed
		if (this.killed)
			return true;
		
		// or if autodeath is on and it's empty (but has had an object in it 
		// previously)
		if (this.autodeath)
		{	
			updateStatus();
			//System.out.println("Started: " + this.started + ", empty: " + 
			//this.handleds.isEmpty());
			if (this.started && this.handleds.isEmpty())
				return true;
		}
		
		return false;
	}

	@Override
	public void kill()
	{
		// Tries to permanently inactivate all subhandleds and kill the handler
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			iterator.next().kill();
		}
		
		// Also erases the memory
		killWithoutKillingHandleds();
	}
	
	/**
	 * Kills the handler but spares the handleds in the handler. This should 
	 * be used instead of kill -method if, for example, the handleds are still 
	 * used in another handler.
	 */
	public void killWithoutKillingHandleds()
	{
		// Safely clears the handleds
		clearOperationList(HandlingOperation.HANDLE);
		// Safely clears the added handleds
		clearOperationList(HandlingOperation.ADD);
		// And finally clears the removed handleds
		clearOperationList(HandlingOperation.REMOVE);
		
		this.killed = true;
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Goes through all the handleds and calls the operator's handleObject() 
	 * -method for the objects
	 * @param operator The operation done for each handled. Null if the default 
	 * handleObject(Handled) should be used
	 * 
	 * @see #handleObject(Handled)
	 * @see HandlingOperator
	 */
	// TODO: Add boolean parameter performUpdate is needed
	protected void handleObjects(HandlingOperator operator)
	{	
		updateStatus();
		
		// Disabled handlers don't handle objects until reactivated
		if (this.disabled)
			return;
		
		// Goes through all the handleds
		boolean handlingskipped = false;
		this.locks.get(HandlingOperation.HANDLE).lock();

		try
		{
			Iterator<Handled> iterator = this.handleds.iterator();
			
			while (iterator.hasNext())
			{
				if (this.killed)
					break;
				
				Handled h = iterator.next();
				
				if (!h.isDead() /*&& !this.handledstoberemoved.containsKey(h)*/)
				{	
					// Doesn't handle objects after handleobjects has returned 
					// false. Continues through the cycle though to remove dead 
					// handleds
					if (!handlingskipped)
					{
						if (operator == null)
						{
							if (!handleObject(h))
								handlingskipped = true;
							
						}
						else if (!operator.handleObject(h))
							handlingskipped = true;
					}
				}
				else
					removeHandled(h);
			}
		}
		finally { this.locks.get(HandlingOperation.HANDLE).unlock(); }
		
		updateStatus();
	}
	
	/**
	 * Goes through all the handleds and calls handleObject -method for those 
	 * objects
	 * 
	 * @see #handleObject(Handled)
	 * @see #handleObjects(HandlingOperator)
	 */
	protected void handleObjects()
	{
		handleObjects(null);
	}
	
	/**
	 * @return The iterator of the handled list
	 * @see #handleObjects()
	 * @warning This method is not very safe and should not be used if 
	 * handleObjects() can be used instead
	 */
	protected Iterator<Handled> getIterator()
	{
		return this.handleds.iterator();
	}
	
	/**
	 * Adds a new object to the handled objects
	 *
	 * @param h The object to be handled
	 */
	protected void addHandled(Handled h)
	{	
		//System.out.println(this + " tries to add a handled");
		
		// Handled must be of the supported class
		if (!getSupportedClass().isInstance(h))
		{
			System.err.println(getClass().getName() + 
					" does not support given object's class");
			return;
		}
		
		// Performs necessary checks
		if (h != this && !this.handleds.contains(h) && 
				!this.handledstobeadded.contains(h))
		{
			// Adds the handled to the queue
			addToOperationList(HandlingOperation.ADD, h);
			this.started = true;
			//System.out.println(this + " adds a handled to queue (now " + 
			//			this.handledstobeadded.size() + ")");
		}
	}
	
	/**
	 * Removes a handled from the group of handled objects
	 *
	 * @param h The handled object to be removed
	 */
	public void removeHandled(Handled h)
	{
		if (h != null && !this.handledstoberemoved.contains(h) && 
				this.handleds.contains(h))
		{
			addToOperationList(HandlingOperation.REMOVE, h);
		}
	}
	
	/**
	 * Removes all the handleds from the handler
	 */
	public void removeAllHandleds()
	{
		this.locks.get(HandlingOperation.HANDLE).lock();
		try
		{
			Iterator<Handled> iter = getIterator();
			
			while (iter.hasNext())
			{
				removeHandled(iter.next());
			}
		}
		finally { this.locks.get(HandlingOperation.HANDLE).unlock(); }
		
		// Also cancels the adding of new handleds
		clearOperationList(HandlingOperation.ADD);
	}
	
	/**
	 * Temporarily disables the handler. This can be used to block certain 
	 * functions for a while. The disable should be ended with endDisable().
	 * 
	 * @see #endDisable()
	 */
	public void disable()
	{
		this.disabled = true;
	}
	
	/**
	 * Ends a temporary disable put on the handler, making it function normally 
	 * again
	 */
	public void endDisable()
	{
		this.disabled = false;
	}
	
	/**
	 * @return How many objects is the handler currently taking care of
	 */
	protected int getHandledNumber()
	{
		return this.handleds.size();
	}
	
	/**
	 * Prints the amount of handleds the handler currently contains. This 
	 * should be used for testing purposes only.
	 */
	public void printHandledNumber()
	{
		System.out.println(getHandledNumber());
	}
	
	/**
	 * @return The first handled in the list of handleds
	 */
	protected Handled getFirstHandled()
	{
		return this.handleds.getFirst();
	}
	
	/**
	 * Returns a certain handled form the list
	 *
	 * @param index The index from which the handled is taken
	 * @return The handled from the given index or null if no such index exists
	 * @warning Normally it is adviced to use the iterator to go through the 
	 * handleds but if the caller modifies the list during the iteration, this 
	 * method should be used instead
	 * @see #getIterator()
	 * @see #handleObjects(Handled)
	 */
	protected Handled getHandled(int index)
	{
		if (index < 0 || index >= getHandledNumber())
			return null;
		return this.handleds.get(index);
	}
	
	/**
	 * Updates the handler list by adding new members and removing old ones. 
	 * This method should not be called during an iteration but is useful before 
	 * testsing the handler status.<br>
	 * Status is automatically updated each time the handleds in the handler 
	 * are handled.
	 * 
	 * @see #handleObjects()
	 */
	protected void updateStatus()
	{
		// Adds the new handleds (if possible)
		addNewHandleds();
		// Removes the removed handleds (if possible)
		clearRemovedHandleds();
	}
	
	/**
	 * Sorts the list of handleds using the given comparator
	 *
	 * @param c The comparator used to sort the handleds
	 */
	protected void sortHandleds(Comparator<Handled> c)
	{
		Collections.sort(this.handleds, c);
	}
	
	// This should be called at the end of the iteration
	private void clearRemovedHandleds()
	{
		if (this.handledstoberemoved.isEmpty())
			return;
		
		this.locks.get(HandlingOperation.REMOVE).lock();
		try
		{
			// Removes all removed handleds from handleds or added or 
			// inserted handleds
			for (Handled h : this.handledstoberemoved)
			{
				if (this.handleds.contains(h))
					removeFromOperationList(HandlingOperation.HANDLE, h);
			}
			
			// Empties the removing list
			// TODO: One might want to change these into clearoperationList(...)
			this.handledstoberemoved.clear();
		}
		finally { this.locks.get(HandlingOperation.REMOVE).unlock(); }
	}
	
	private void addNewHandleds()
	{
		// If the handler has no handleds to be added, does nothing
		if (this.handledstobeadded.isEmpty())
			return;
		
		this.locks.get(HandlingOperation.ADD).lock();
		try
		{
			// Adds all handleds from the addlist to the handleds
			for (Handled h : this.handledstobeadded)
			{
				addToOperationList(HandlingOperation.HANDLE, h);
			}
			
			// Clears the addlist
			this.handledstobeadded.clear();
		}
		finally { this.locks.get(HandlingOperation.ADD).unlock(); }
	}
	
	// Thread-safely clears a data structure used with the given operation type
	// TODO: Try to figure out a way to make this without copy-paste, though 
	// it might be difficult since there are no function pointers in java
	private void clearOperationList(HandlingOperation o)
	{
		// Checks the argument
		if (o == null)
			return;
		
		// Locks the correct lock
		this.locks.get(o).lock();
		
		try
		{
			switch (o)
			{
				// I really wish I had function pointers in use now...
				case HANDLE: this.handleds.clear(); break;
				case ADD: this.handledstobeadded.clear(); break;
				case REMOVE: this.handledstoberemoved.clear(); break;
			}
		}
		finally
		{
			this.locks.get(o).unlock();
		}
	}
	
	// Thread safely adds an handled to an operation list
	private void addToOperationList(HandlingOperation o, Handled h)
	{
		// Checks the argument
		if (o == null || h == null)
			return;
		
		// Locks the correct lock
		this.locks.get(o).lock();
		
		try
		{
			switch (o)
			{
				case HANDLE: this.handleds.add(h); break;
				case ADD: this.handledstobeadded.add(h); break;
				case REMOVE: this.handledstoberemoved.add(h); break;
			}
		}
		finally
		{
			this.locks.get(o).unlock();
		}
	}
	
	// Thread safely removes an handled from an operation list
	private void removeFromOperationList(HandlingOperation o, Handled h)
	{
		// Checks the argument
		if (o == null || h == null)
			return;
		
		// Locks the correct lock
		this.locks.get(o).lock();
		
		try
		{
			switch (o)
			{
				case HANDLE: this.handleds.remove(h); break;
				case ADD: this.handledstobeadded.remove(h); break;
				case REMOVE: this.handledstoberemoved.remove(h); break;
			}
		}
		finally
		{
			this.locks.get(o).unlock();
		}
	}
	
	
	// ENUMERATIONS	------------------------------------------------------
	
	private enum HandlingOperation
	{
		HANDLE, ADD, REMOVE;
	}
	
	
	// SUBCLASSES	-------------------------------------------------------
	
	/**
	 * HandlingOperator is a function object that does a specific operation 
	 * for a single handled. The subclasses of this class will define the 
	 * nature of the operation.<br>
	 * HandlingOperators are used in handleObjects() -method and are usually 
	 * used with multiple handleds in succession.
	 *
	 * @author Mikko Hilpinen.
	 *         Created 19.10.2013.
	 */
	protected abstract class HandlingOperator
	{
		// ABSTRACT METHODS	---------------------------------------------
		
		/**
		 * In this method the operator affects the handled in some way.
		 *
		 * @param h The handled that needs to be done something with
		 * @return Should the operation be done for the remaining handleds as well
		 */
		protected abstract boolean handleObject(Handled h);
	}
}
