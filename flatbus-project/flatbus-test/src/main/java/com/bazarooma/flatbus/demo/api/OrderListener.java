package com.bazarooma.flatbus.demo.api;

import com.bazarooma.flatbus.demo.domain.Product;
import com.bazarooma.flatbus.shared.BusListener;

/**
 * Listener for events that modify the order
 * @author george georgovassilis
 *
 */
public interface OrderListener extends BusListener{

	void onAddNewItemToOrder(Product product);
	void onModifyItemQuantityInOrder(Product product, int modificationInQuantity);
	void onRemoveItemFromOrder(Product product);
}
