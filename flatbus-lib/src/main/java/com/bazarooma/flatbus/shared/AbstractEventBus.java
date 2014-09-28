package com.bazarooma.flatbus.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bazarooma.flatbus.generator.BusGenerator;
import com.google.gwt.event.shared.UmbrellaException;

/**
 * Base implementation of the {@link EventBus} which takes care of registering
 * and unregistering listeners, maintaining a queue of listeners and delegating
 * method invocations to them. The {@link BusGenerator} generates bus
 * implementations that extend this class and implement interfaces that extend
 * {@link BusListener} and implement the
 * {@link #getInterfaceNamesForListener(BusListener)} method.
 * 
 * @author george georgovassilis
 * 
 */
public abstract class AbstractEventBus implements EventBus {

	/**
	 * Base command for generated method invocations on listeners. A
	 * parameterized executable removes the need for class casting in the
	 * generated method invocations and encapsulates processing state.
	 * 
	 * @param <Listener>
	 * @param <ReturnType>
	 */
	public abstract class Executable<Listener extends BusListener, ReturnType> {

		public List<ReturnType> results;

		public abstract ReturnType execute(Listener listener);

		public void addItemToResults(ReturnType item) {
			if (results == null)
				results = new ArrayList<ReturnType>();
			results.add(item);
		}
	}

	/**
	 * Map of interface names (interfaces extending {@link BusListener}) to
	 * listeners
	 */
	protected Map<String, List<BusListener>> listeners = new HashMap<String, List<BusListener>>();

	/**
	 * List of returned values of all listener of the current invocations
	 */
	protected List<?> lastInvocationResults;

	/**
	 * Indicates whether an invocation is still active or has been cancelled
	 */
	protected boolean callActive = false;

	/**
	 * The generated bus must implement this method which returns the interface
	 * name (an extension of {@link BusListener}) for the provided listener
	 * implementation
	 * 
	 * @param listener
	 * @return
	 */
	protected abstract List<String> getInterfaceNamesForListener(BusListener listener);

	/**
	 * Finds all listeners that implement the interface denoted by
	 * "interfaceName" and runs "executable" on them.
	 * 
	 * @param interfaceName
	 * @param executable
	 * @param returnsResults
	 *            The caller needs to know whether the method invoked on the
	 *            listener declares a return type or not and denote this here
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void invokeListeners(String interfaceName, Executable executable, boolean returnsResults) {
		List<BusListener> specificListeners = getListenersFor(interfaceName, listeners);
		if (returnsResults && specificListeners == null)
			throw new IllegalArgumentException("Cannot return results when no listeners are registered for "
					+ interfaceName);
		if (specificListeners == null)
			return;
		// create unmodifiable copy so that additions/removals of listeners
		// during an invocations don't mingle with the order of listeners
		specificListeners = new ArrayList<BusListener>(specificListeners);
		boolean oldCallActive = callActive;
		callActive = true;
		Set<Throwable> exceptions = null;
		for (BusListener listener : specificListeners) {
			if (!callActive)
				break;
			try {
				Object result = executable.execute(listener);
				executable.addItemToResults(result);
			} catch (Throwable t) {
				if (exceptions == null)
					exceptions = new HashSet<Throwable>();
				exceptions.add(t);
			}
		}
		callActive = oldCallActive;
		lastInvocationResults = executable.results;
		if (exceptions != null)
			throw new UmbrellaException(exceptions);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static <T extends BusListener> List<T> getListenersFor(String className,
			Map<String, List<BusListener>> listeners) {
		List<BusListener> list = listeners.get(className);
		if (list == null)
			return null;
		return (List<T>) (new ArrayList(list));
	}

	@Override
	public void unregister(BusListener listener) {
		List<String> interfaceNames = getInterfaceNamesForListener(listener);
		for (String interfaceName : interfaceNames) {
			List<BusListener> list = listeners.get(interfaceName);
			if (list != null)
				list.remove(listener);
		}
	}

	@Override
	public void register(BusListener listener) {
		List<String> interfaceNames = getInterfaceNamesForListener(listener);
		for (String interfaceName : interfaceNames) {
			List<BusListener> list = listeners.get(interfaceName);
			if (list == null) {
				list = new ArrayList<BusListener>();
				listeners.put(interfaceName, list);
			}
			list.add(listener);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> all(T object) {
		List list = lastInvocationResults;
		lastInvocationResults = null;
		return list;
	}

	@Override
	public <T> List<T> allWithValues(T object) {
		List<T> list = all(object);
		for (Iterator<T> ite = list.iterator(); ite.hasNext();) {
			T value = ite.next();
			if (value == null)
				ite.remove();
		}
		return list;
	}

	@Override
	public <T> T firstValue(T object) {
		List<T> list = all(object);
		if (list != null)
			for (T item : list)
				if (item != null)
					return item;
		return null;
	}

	@Override
	public void cancelCurrentEvent() {
		callActive = false;
	}

}
