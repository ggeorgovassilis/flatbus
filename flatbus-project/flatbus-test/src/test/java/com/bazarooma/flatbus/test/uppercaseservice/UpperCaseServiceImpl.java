package com.bazarooma.flatbus.test.uppercaseservice;

/**
 * 
 * Implementation of the upepr case service
 * @author george georgovassilis
 * 
 */

public class UpperCaseServiceImpl implements UpperCaseService{

	@Override
	public void toUpperCase(String what, StringBuffer result) {
		result.append(what.toUpperCase());
	}

}
