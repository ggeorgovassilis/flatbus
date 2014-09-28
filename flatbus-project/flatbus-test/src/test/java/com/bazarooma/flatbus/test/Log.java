package com.bazarooma.flatbus.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A log memory. Quite handy when evaluating log traces in tests in order to determine that the correct services were called in the right order.
 * @author george georgovassilis
 * 
 */

public class Log {

	private List<String> messages = new ArrayList<String>();
	public static Log log = new Log();
	
	public void log(String message) {
		messages.add(message);
	}

	public List<String> getMessages() {
		return messages;
	}
	
}
