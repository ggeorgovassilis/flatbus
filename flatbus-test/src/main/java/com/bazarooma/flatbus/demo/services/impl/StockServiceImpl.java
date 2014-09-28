package com.bazarooma.flatbus.demo.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bazarooma.flatbus.demo.api.StockService;
import com.bazarooma.flatbus.demo.domain.Item;
import com.bazarooma.flatbus.demo.domain.Product;
import com.bazarooma.flatbus.demo.domain.ValidationMessage;
import com.google.gwt.user.client.Window;

/**
 * Implements a warehouse inventory
 * @author george georgovassilis
 *
 */
public class StockServiceImpl implements StockService{

	private List<Product> catalogue = new ArrayList<Product>();
	private Map<Product, Integer> stock = new HashMap<Product, Integer>();
	
	public StockServiceImpl(){
		catalogue.add(new Product(1, "Running shoes", 20));
		catalogue.add(new Product(2, "Ice making machine", 120));
		catalogue.add(new Product(3, "Mini LCD TV", 89));
		catalogue.add(new Product(4, "Commemorative postcards", 1));
		
		stock.put(catalogue.get(0), 10);
		stock.put(catalogue.get(1), 3);
		stock.put(catalogue.get(2), 5);
		stock.put(catalogue.get(3), 0);
	}
	
	@Override
	public ValidationMessage validateThatItemIsInStock(Item item) {
		Product p = item.getProduct();
		if (item.getQuantity()<1)
			return new ValidationMessage(p, "You must at least order a single item");
		int count = stock.get(p);
		if (count<item.getQuantity())
			return new ValidationMessage(p, "Not enough in stock");
		return null;
	}

	@Override
	public List<Product> getAllProducts() {
		return new ArrayList<Product>(catalogue);
	}

	@Override
	public Product findProductById(int id) {
		for (Product p:catalogue)
			if (p.getId() == id)
				return p;
		return null;
	}

	@Override
	public void checkoutOrder() {
		Window.alert("Thank you for your order");
	}

}
