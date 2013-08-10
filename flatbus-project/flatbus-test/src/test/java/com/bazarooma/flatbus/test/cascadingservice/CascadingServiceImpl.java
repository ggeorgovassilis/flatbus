package com.bazarooma.flatbus.test.cascadingservice;

import com.bazarooma.flatbus.shared.EventBus;
import com.bazarooma.flatbus.test.Log;

public class CascadingServiceImpl implements CascadingService{

	private static int counter = 0;
	private EventBus bus;
	private int instanceId = counter++;
	
	public CascadingServiceImpl(EventBus bus){
		this.bus = bus;
	}
	
	
	@Override
	public void logAndRegisterNewInstance() {
		Log.log.log(getClass().getName()+".logAndRegisterNewInstance "+instanceId);
		bus.register(new CascadingServiceImpl(bus));
	}

	@Override
	public void logAndUnregisterSelf() {
		Log.log.log(getClass().getName()+".logAndUnregisterSelf "+instanceId);
		bus.unregister(this);
	}

}
