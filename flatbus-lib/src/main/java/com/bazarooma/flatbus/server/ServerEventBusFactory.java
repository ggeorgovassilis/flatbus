package com.bazarooma.flatbus.server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.bazarooma.flatbus.shared.BusListener;
import com.bazarooma.flatbus.shared.EventBus;

/**
 * Factory which generates {@link EventBus} instances for unit testing and mocking.
 * Assuming an interface MyEventBus which extends both {@link EventBus} and one or more {@link BusListener} interfaces:
 * 
 * MyEventBus bus = factory.createAndProxy(MyEventBus.class)
 * 
 * @author george georgovassilis
 *
 */
public class ServerEventBusFactory {

	@SuppressWarnings("unchecked")
	public <T extends EventBus> T createAndProxy(Class<T> busInterface){
		final ServerEventBus bus = new ServerEventBus();
		return (T)Proxy.newProxyInstance(ServerEventBus.class.getClassLoader(), new Class[]{busInterface}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Class<?> c = method.getDeclaringClass();
				if (BusListener.class.isAssignableFrom(c))
					return bus.invokeListenerMethod((BusListener)proxy, method.getDeclaringClass(), method, args);
				else
					return method.invoke(bus, args);
			}
		});
	}
}
