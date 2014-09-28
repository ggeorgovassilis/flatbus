package com.bazarooma.flatbus.shared;

/**
 * Marker interface for listeners on the {@link EventBus}. Components that want to be proxied by the {@link EventBus} need
 * to implement an interface which extends {@link BusListener} and then register with {@link EventBus#register(BusListener)}
 * @author george georgovassilis
 *
 */
public interface BusListener {

}
