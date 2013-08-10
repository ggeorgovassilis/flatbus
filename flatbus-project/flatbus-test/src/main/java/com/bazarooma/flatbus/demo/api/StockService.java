package com.bazarooma.flatbus.demo.api;

import java.util.List;

import com.bazarooma.flatbus.demo.domain.Item;
import com.bazarooma.flatbus.demo.domain.Product;
import com.bazarooma.flatbus.demo.domain.ValidationMessage;
import com.bazarooma.flatbus.shared.BusListener;

/**
 * A kind of warehouse component for checking availability etc
 * @author george georgovassilis
 *
 */
public interface StockService extends BusListener{

	List<Product> getAllProducts();
	Product findProductById(int id);
	ValidationMessage validateThatItemIsInStock(Item item);
	void checkoutOrder();
}
