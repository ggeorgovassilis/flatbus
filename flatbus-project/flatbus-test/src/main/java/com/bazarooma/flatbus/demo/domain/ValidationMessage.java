package com.bazarooma.flatbus.demo.domain;

/**
 * A validation message indicates some kind of error condition regarding either a specific item ({@link #aboutProduct} is not null)
 * or the order in general ({@link #aboutProduct} is null) 
 * @author george georgovassilis
 *
 */
public class ValidationMessage {

	private final String message;
	private final Product aboutProduct;
	
	public ValidationMessage(Product product, String message){
		this.aboutProduct = product;
		this.message = message;
	}

	public Product getAboutProduct() {
		return aboutProduct;
	}

	public String getMessage() {
		return message;
	}
}
