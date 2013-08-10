package com.bazarooma.flatbus.test.uppercaseservice;

public class UpperCaseServiceImpl implements UpperCaseService{

	@Override
	public void toUpperCase(String what, StringBuffer result) {
		result.append(what.toUpperCase());
	}

}
