package com.bazarooma.flatbus.test.mathservice;

/**
 * 
 * A faulty implementation of the math service
 * @author george georgovassilis
 * 
 */

public class BrokenMathServiceImpl implements MathService{

	@Override
	public int add(int a, int b) {
		return 6;
	}

	@Override
	public int[] sort(int[] numbers) {
		return null;
	}

}
