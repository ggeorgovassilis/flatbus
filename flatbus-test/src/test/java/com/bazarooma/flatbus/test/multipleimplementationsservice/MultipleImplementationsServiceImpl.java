package com.bazarooma.flatbus.test.multipleimplementationsservice;

import com.bazarooma.flatbus.test.Log;
import com.bazarooma.flatbus.test.mathservice.MathService;
import com.bazarooma.flatbus.test.uppercaseservice.UpperCaseService;

/**
 * 
 * A class that implements multiple service interfaces and thus consumes multiple events
 * @author george georgovassilis
 * 
 */

public class MultipleImplementationsServiceImpl implements MathService, UpperCaseService{

	@Override
	public void toUpperCase(String what, StringBuffer result) {
		Log.log.log(getClass().getName()+".toUpperCase");
	}

	@Override
	public int add(int a, int b) {
		Log.log.log(getClass().getName()+".add");
		return 0;
	}

	@Override
	public int[] sort(int[] numbers) {
		Log.log.log(getClass().getName()+".sort");
		return null;
	}

}
