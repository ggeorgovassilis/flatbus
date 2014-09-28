package com.bazarooma.flatbus.demo.ui;

import com.bazarooma.flatbus.demo.api.OrderSummaryListener;
import com.bazarooma.flatbus.demo.api.ValidationMessagesListener;
import com.bazarooma.flatbus.demo.domain.ValidationMessage;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Interface for the {@link SummaryWidget}
 * @author george georgovassilis
 *
 */
public interface ISummaryWidget extends IsWidget, OrderSummaryListener, ValidationMessagesListener{

	void hideAllMessages();

	void show(ValidationMessage message);

	void updateTotals(int itemCount, int price);

	void setCheckoutButtonEnabled(boolean enabled);

}