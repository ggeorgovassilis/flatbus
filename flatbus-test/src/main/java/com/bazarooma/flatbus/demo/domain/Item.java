package com.bazarooma.flatbus.demo.domain;

/**
 * An order consists of one or more items. Items consists of a product and a quantity.
 * @author george georgovassilis
 *
 */
public class Item {

	private final Product product;
	private int quantity;

	public Item(Product product, int quantity){
		this.product = product;
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}
}
