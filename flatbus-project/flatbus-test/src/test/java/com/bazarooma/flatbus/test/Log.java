package com.bazarooma.flatbus.test;

import java.util.ArrayList;
import java.util.List;

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
