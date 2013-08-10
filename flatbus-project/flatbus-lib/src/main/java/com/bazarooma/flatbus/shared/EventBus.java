package com.bazarooma.flatbus.shared;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;

/**
 * Basic interface for the event bus. This interface is extended by the
 * interface in the application and implemented by the BusGenerator when
 * {@link GWT#create(Class)} is called.
 * 
 * A note on error handling: exceptions occurring in listeners will not lead to
 * cancellation of the invocation chain. Subsequent listeners in the queue will
 * still be invoked. Any exceptions thrown by the listeners are collected by an
 * {@link UmbrellaException} and thrown at the end of the invocation.
 * 
 * @author george georgovassilis
 * 
 */
public interface EventBus {

	/**
	 * Add a listener to the event bus. Method invocations on the event bus that
	 * belong to interfaces implemented by the listener will be forwarded to the
	 * listener. Multiple registrations of the same listener will register it
	 * only once.
	 * 
	 * @param listener
	 */
	void register(BusListener listener);

	/**
	 * Unregister a listener so it will stop receiving invocations.
	 * Unregistering a listener that is not currently registered has no effect.
	 * 
	 * @param listener
	 */
	void unregister(BusListener listener);

	/**
	 * Invoke method on all registered listeners and collect return values in a
	 * list. Assuming an interface OurEventBus extends {@link EventBus} which
	 * declares a method getQuoteOfTheDay():String and multiple listeners of
	 * that type being registered, the following call will return the results of
	 * all method invocations of getQuoteOfTheDay on all registered listeners:
	 * 
	 * List<String> quotes = eventBus.all(eventBus.getQuoteOfTheDay());
	 * 
	 * @param T
	 * @return List of return values.
	 */
	<T> List<T> all(T object);

	/**
	 * Same as {@link #all(Object)}, but filters out nulls
	 * @param T
	 * @return List of return values.
	 */
	<T> List<T> allWithValues(T object);

	/**
	 * Like {@link #allWithValues(Object)} but returns only the first non-null value
	 * @param object
	 * @return
	 */
	<T> T firstValue(T object);

	/**
	 * Cancel the current invocation. No further listeners in the current
	 * invocation will be invoked
	 */
	void cancelCurrentEvent();
}
