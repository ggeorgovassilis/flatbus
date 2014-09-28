package com.bazarooma.flatbus.test.addressservice;

import com.bazarooma.flatbus.shared.BusListener;

/**
 * 
 * Interface for the postal code lookup service
 * @author george georgovassilis
 * 
 */

public interface PostalCodeLookupService extends BusListener{

	Address lookup(String postalCode);
}
