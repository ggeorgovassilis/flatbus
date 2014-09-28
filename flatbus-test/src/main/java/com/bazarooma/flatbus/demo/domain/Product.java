package com.bazarooma.flatbus.demo.domain;

/**
 * A product consists of an id, name and price per unit. Product instances in
 * this application are singletons and can be used as identifiers.
 * 
 * @author george georgovassilis
 * 
 */
public class Product {

	private final int id;
	private final String name;
	private final int unitPrice;

	public Product(int id, String name, int unitPrice) {
		this.id = id;
		this.name = name;
		this.unitPrice = unitPrice;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getUnitPrice() {
		return unitPrice;
	}
}
