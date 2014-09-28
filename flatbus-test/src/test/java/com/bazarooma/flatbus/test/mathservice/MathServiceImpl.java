package com.bazarooma.flatbus.test.mathservice;

import java.util.Arrays;

/**
 * 
 * Implementation of the math service
 * @author george georgovassilis
 * 
 */

public class MathServiceImpl implements MathService{

	@Override
	public int add(int a, int b) {
		return a + b;
	}

	@Override
	public int[] sort(int[] numbers) {
		int[] copy = new int[numbers.length];
		for (int i=0;i<numbers.length;i++)
			copy[i] = numbers[i];
		Arrays.sort(copy);
		return copy;
	}

}
