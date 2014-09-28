package com.bazarooma.flatbus.test.mathservice;

import com.bazarooma.flatbus.shared.BusListener;

/**
 * 
 * Definition of a math service
 * @author george georgovassilis
 * 
 */

public interface MathService extends BusListener{

	int add(int a, int b);
	int[] sort(int[] numbers);
}
