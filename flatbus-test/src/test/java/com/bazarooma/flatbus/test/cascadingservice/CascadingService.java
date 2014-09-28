package com.bazarooma.flatbus.test.cascadingservice;

import com.bazarooma.flatbus.shared.BusListener;

/**
 * 
 * Interface definitions for a logging service that registers and unregisters itself during event processing.
 * Uses to test that the bus doesn't get confused by listeners joining and leaving the bus while events are being processed
 * @author george georgovassilis
 * 
 */

public interface CascadingService extends BusListener{

	void logAndRegisterNewInstance();
	void logAndUnregisterSelf();
}
