package com.bazarooma.flatbus.test.uppercaseservice;

import com.bazarooma.flatbus.shared.BusListener;


public interface UpperCaseService extends BusListener{

	void toUpperCase(String what, StringBuffer result);
}
