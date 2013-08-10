package com.bazarooma.flatbus.demo.ui;

import com.bazarooma.flatbus.demo.api.OrderListener;
import com.bazarooma.flatbus.demo.api.ValidationMessagesListener;
import com.bazarooma.flatbus.demo.domain.Item;
import com.bazarooma.flatbus.demo.domain.Product;
import com.bazarooma.flatbus.demo.domain.ValidationMessage;
import com.google.gwt.user.client.ui.IsWidget;

public interface IItemWidget extends IsWidget, OrderListener, ValidationMessagesListener{

	Item getValue();

	void setQuantity(int quantity);

	void onModifyItemQuantityInOrder(Product product, int modificationInQuantity);

	void onRemoveItemFromOrder(Product product);

	void onAddNewItemToOrder(Product product);

	void hideAllMessages();

	void show(ValidationMessage message);

}