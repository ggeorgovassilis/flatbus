package com.bazarooma.flatbus.test.mathservice;

import com.bazarooma.flatbus.shared.BusListener;


public interface MathService extends BusListener{

	int add(int a, int b);
	int[] sort(int[] numbers);
}
