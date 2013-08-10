package com.bazarooma.flatbus.demo.api;

import com.bazarooma.flatbus.shared.BusListener;

/**
 * Listener for order summary
 * @author george georgovassilis
 *
 */
public interface OrderSummaryListener extends BusListener{

	void updateTotals(int itemCount, int price);
}
