package com.bazarooma.flatbus.demo.api;

import com.bazarooma.flatbus.demo.domain.Item;
import com.bazarooma.flatbus.shared.BusListener;

/**
 * Various UI specific methods that indicate that the user has clicked on something
 * @author george georgovassilis
 *
 */
public interface UserInteractionListener extends BusListener{

	void onAddProductToOrderButtonClicked(int productId);
	void onIncreaseItemQuantityButtonClicked(Item item);
	void onDecreaseItemQuantityButtonClicked(Item item);
	void onCheckoutButtonClicked();
}
