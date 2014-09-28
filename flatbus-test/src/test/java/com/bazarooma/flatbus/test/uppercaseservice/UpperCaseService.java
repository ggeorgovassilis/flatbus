package com.bazarooma.flatbus.test.uppercaseservice;

import com.bazarooma.flatbus.shared.BusListener;

/**
 * Service definition that converts arguments to upper case
 * @author george georgovassilis
 * 
 */

public interface UpperCaseService extends BusListener{

	void toUpperCase(String what, StringBuffer result);
}
