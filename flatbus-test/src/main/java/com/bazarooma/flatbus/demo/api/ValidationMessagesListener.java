package com.bazarooma.flatbus.demo.api;

import com.bazarooma.flatbus.demo.domain.ValidationMessage;
import com.bazarooma.flatbus.shared.BusListener;

/**
 * Listener for showing validation messages
 * @author george georgovassilis
 *
 */
public interface ValidationMessagesListener extends BusListener{

	void hideAllMessages();
	void show(ValidationMessage message);

}
