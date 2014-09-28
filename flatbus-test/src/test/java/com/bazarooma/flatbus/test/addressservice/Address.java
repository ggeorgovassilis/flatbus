package com.bazarooma.flatbus.test.addressservice;

/**
 * 
 * Models a simple address entity
 * @author george georgovassilis
 * 
 */

public class Address {

	private String postalCode;
	private String city;
	
	public Address(){
	}
	
	public Address(String postalCode, String city){
		this.postalCode = postalCode;
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
