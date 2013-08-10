package com.bazarooma.flatbus.test.cascadingservice;

import com.bazarooma.flatbus.shared.BusListener;

public interface CascadingService extends BusListener{

	void logAndRegisterNewInstance();
	void logAndUnregisterSelf();
}
