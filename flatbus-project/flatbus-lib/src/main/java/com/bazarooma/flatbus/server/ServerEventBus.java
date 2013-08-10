package com.bazarooma.flatbus.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.bazarooma.flatbus.shared.AbstractEventBus;
import com.bazarooma.flatbus.shared.BusListener;

/**
 * Implementation to be used in unit tests. Also see {@link ServerEventBusFactory}
 * @author george georgovassilis
 *
 */
public class ServerEventBus extends AbstractEventBus{

	@Override
	protected List<String> getInterfaceNamesForListener(BusListener listener) {
		List<String> interfaceNames = new ArrayList<String>();
		Class<?> listenerClass = listener.getClass();
		Class<?>[] interfaces = listenerClass.getInterfaces();
		for (Class<?> c:interfaces){
			if (BusListener.class.isAssignableFrom(c))
				interfaceNames.add(c.getCanonicalName());
		}
		return interfaceNames;
	}
	
	/**
	 * Invoke a listener method on the event bus. This will delegate to {@link #invokeListeners(String, Executable, boolean)}
	 * @param listener
	 * @param listenerInterface
	 * @param method
	 * @param args
	 * @return
	 */
	public Object invokeListenerMethod(final BusListener listener, Class<?> listenerInterface, final Method method, final Object[] args){
		Class<?> returnType = method.getReturnType();
		boolean returnsValue = !(void.class.equals(returnType) || Void.class.equals(returnType));
		invokeListeners(listenerInterface.getName(), new Executable<BusListener, Object>() {
			@Override
			public Object execute(BusListener listener) {
				try {
					return method.invoke(listener, args);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}, returnsValue);
		return lastInvocationResults.get(0);
	}
}
