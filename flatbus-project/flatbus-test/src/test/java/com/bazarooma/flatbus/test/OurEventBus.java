package com.bazarooma.flatbus.test;

import com.bazarooma.flatbus.shared.EventBus;
import com.bazarooma.flatbus.test.addressservice.PostalCodeLookupService;
import com.bazarooma.flatbus.test.cascadingservice.CascadingService;
import com.bazarooma.flatbus.test.mathservice.MathService;
import com.bazarooma.flatbus.test.treesplitservice.NumberGameService;
import com.bazarooma.flatbus.test.uppercaseservice.UpperCaseService;


public interface OurEventBus extends EventBus, MathService, UpperCaseService, PostalCodeLookupService, NumberGameService, CascadingService{

}
