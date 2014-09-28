package com.bazarooma.flatbus.test.addressservice;

import java.util.HashMap;
import java.util.Map;

import com.bazarooma.flatbus.shared.EventBus;

/**
 * 
 * Synchronous implementation of the postal code lookup service
 * @author george georgovassilis
 * 
 */

public class PostalCodeLookupServiceImpl implements PostalCodeLookupService {

	private Map<String, Address> codesToPlaces = new HashMap<String, Address>();
	private EventBus eventBus;
	
	public PostalCodeLookupServiceImpl(EventBus bus, String... codes) {
		for (String pair : codes) {
			String[] parts = pair.split(",");
			String postalCode = parts[0];
			String place = parts[1];
			codesToPlaces.put(postalCode, new Address(postalCode, place));
		}
		this.eventBus = bus;
	}

	@Override
	public Address lookup(String postalCode) {
		Address address = codesToPlaces.get(postalCode);
		if (address!=null)
			eventBus.cancelCurrentEvent();
		return address;
	}

}
