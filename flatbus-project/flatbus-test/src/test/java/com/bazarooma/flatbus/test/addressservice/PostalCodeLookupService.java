package com.bazarooma.flatbus.test.addressservice;

import com.bazarooma.flatbus.shared.BusListener;

public interface PostalCodeLookupService extends BusListener{

	Address lookup(String postalCode);
}
