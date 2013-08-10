package com.bazarooma.flatbus.test;


import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bazarooma.flatbus.test.addressservice.Address;
import com.bazarooma.flatbus.test.addressservice.PostalCodeLookupService;
import com.bazarooma.flatbus.test.addressservice.PostalCodeLookupServiceImpl;
import com.bazarooma.flatbus.test.cascadingservice.CascadingService;
import com.bazarooma.flatbus.test.cascadingservice.CascadingServiceImpl;
import com.bazarooma.flatbus.test.mathservice.BrokenMathServiceImpl;
import com.bazarooma.flatbus.test.mathservice.MathService;
import com.bazarooma.flatbus.test.mathservice.MathServiceImpl;
import com.bazarooma.flatbus.test.multipleimplementationsservice.MultipleImplementationsServiceImpl;
import com.bazarooma.flatbus.test.treesplitservice.NumberGameService;
import com.bazarooma.flatbus.test.treesplitservice.NumberGameServiceImpl;
import com.bazarooma.flatbus.test.uppercaseservice.UpperCaseService;
import com.bazarooma.flatbus.test.uppercaseservice.UpperCaseServiceImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class Tests extends GWTTestCase{

	protected OurEventBus eventBus;
	protected MathService mathService;
	protected MathService brokenMathService;
	protected UpperCaseService upperCaseService;
	protected PostalCodeLookupService postalCodesInCountryA;
	protected PostalCodeLookupService postalCodesInCountryB;
	protected NumberGameService numberGameService;
	
	@Override
	public String getModuleName() {
		return "com.bazarooma.flatbus.Tests";
	}

	@Before
	@Override
	public void gwtSetUp(){
		Log.log = new Log();
		eventBus = GWT.create(OurEventBus.class);
		mathService = new MathServiceImpl();
		brokenMathService = new BrokenMathServiceImpl();
		upperCaseService = new UpperCaseServiceImpl();
		postalCodesInCountryA = new PostalCodeLookupServiceImpl(eventBus, "1234,City A", "5678,City B");
		postalCodesInCountryB = new PostalCodeLookupServiceImpl(eventBus, "1234,Ciudad A", "7777,Ciudad B");
		numberGameService = new NumberGameServiceImpl(eventBus);

		eventBus.register(upperCaseService);
		eventBus.register(mathService);
		eventBus.register(brokenMathService);
		eventBus.register(postalCodesInCountryA);
		eventBus.register(postalCodesInCountryB);
		eventBus.register(numberGameService);
	}
	
	@Test
	public void testSingleServiceWithVoidReturn() {
		StringBuffer upperChar = new StringBuffer();
		eventBus.toUpperCase("small stuff", upperChar);
		assertEquals("Expected value for string from UpperCaseService ", "SMALL STUFF", upperChar.toString());
	}

	@Test
	public void testMultipleServicesWithReturn() {
		List<Integer> results = eventBus.all(eventBus.add(1, 2));
		assertEquals("Expected number of results from MathService ", 2, results.size());
		assertEquals("Expected value for results[0] from MathService ", 3, results.get(0).intValue());
		assertEquals("Expected value for results[1] from MathService ", 6, results.get(1).intValue());
	}

	@Test
	public void testMultipleServicesWithCancel() {
		//normally there would be 2 results since the postal code 1234 is served by two clients.
		//but clients are supposed to cancel the event once served, so there should be only a single result
		List<Address> results = eventBus.all(eventBus.lookup("1234"));
		assertEquals("Expected number of results from PostalCodeLookupService", 1, results.size());
		assertEquals("Expected value for address ", "City A", results.get(0).getCity());
	}

	@Test
	public void testNullsAndAll() {
		eventBus.unregister(postalCodesInCountryA);
		eventBus.unregister(postalCodesInCountryB);
		eventBus.register(new PostalCodeLookupService() {
			@Override
			public Address lookup(String postalCode) {
				return null;
			}
		});
		eventBus.register(new PostalCodeLookupService() {
			@Override
			public Address lookup(String postalCode) {
				return "AAAA".equals(postalCode)?new Address(postalCode, "City A"):null;
			}
		});
		eventBus.register(new PostalCodeLookupService() {
			@Override
			public Address lookup(String postalCode) {
				return "AAAA".equals(postalCode)?new Address(postalCode, "City AAAA"):null;
			}
		});

		List<Address> results = eventBus.all(eventBus.lookup("AAAA"));
		assertEquals("Expected number of results from PostalCodeLookupService", 3, results.size());
		assertEquals("Expected value for address 1", null, results.get(0));
		assertEquals("Expected value for address 2", "City A", results.get(1).getCity());
		assertEquals("Expected value for address 3", "City AAAA", results.get(2).getCity());

		results = eventBus.allWithValues(eventBus.lookup("AAAA"));
		assertEquals("Expected number of results from PostalCodeLookupService", 2, results.size());
		assertEquals("Expected value for address 1", "City A", results.get(0).getCity());
		assertEquals("Expected value for address 2", "City AAAA", results.get(1).getCity());

		Address adr = eventBus.firstValue(eventBus.lookup("AAAA"));
		assertNotNull(adr);

		adr = eventBus.lookup("AAAA");
		assertNull(adr);

	}

	@Test
	public void testRecursiveServiceInvocations() {
		int result = eventBus.play(11);
		assertEquals("Expected result for number game ",3,result);
	}

	@Test
	public void testArray() {
		int[] input = {2,7,4,1,3,8,5,6};
		int[] expectedResult = {1,2,3,4,5,6,7,8};
		int[] result = eventBus.sort(input);
		assertEquals(input.length, result.length);
		for (int i=0;i<expectedResult.length;i++)
			assertEquals(expectedResult[i], result[i]);
	}

	@Test
	public void testCascadingServices() {
		CascadingService service = new CascadingServiceImpl(eventBus);
		eventBus.register(service);
		
		eventBus.logAndRegisterNewInstance();
		assertEquals(1, Log.log.getMessages().size());
		assertEquals(CascadingServiceImpl.class.getName()+".logAndRegisterNewInstance 0", Log.log.getMessages().get(0));

		eventBus.logAndRegisterNewInstance();
		assertEquals(3, Log.log.getMessages().size());
		assertEquals(CascadingServiceImpl.class.getName()+".logAndRegisterNewInstance 0", Log.log.getMessages().get(1));
		assertEquals(CascadingServiceImpl.class.getName()+".logAndRegisterNewInstance 1", Log.log.getMessages().get(2));

		eventBus.logAndUnregisterSelf();
		assertEquals(7, Log.log.getMessages().size());
		assertEquals(CascadingServiceImpl.class.getName()+".logAndUnregisterSelf 0", Log.log.getMessages().get(3));
		assertEquals(CascadingServiceImpl.class.getName()+".logAndUnregisterSelf 1", Log.log.getMessages().get(4));
		assertEquals(CascadingServiceImpl.class.getName()+".logAndUnregisterSelf 2", Log.log.getMessages().get(5));
		assertEquals(CascadingServiceImpl.class.getName()+".logAndUnregisterSelf 3", Log.log.getMessages().get(6));

		eventBus.logAndUnregisterSelf();
		assertEquals(7, Log.log.getMessages().size());
	}
	
	/**
	 * Test that all methods of a service which implements multiple listener interfaces are invoked correctly
	 */
	public void testMultipleImplementationsService(){
		MultipleImplementationsServiceImpl service = new MultipleImplementationsServiceImpl();
		eventBus.register(service);
		eventBus.toUpperCase("some string", new StringBuffer());
		eventBus.add(1, 2);

		assertEquals(2, Log.log.getMessages().size());
		assertEquals(MultipleImplementationsServiceImpl.class.getName()+".toUpperCase", Log.log.getMessages().get(0));
		assertEquals(MultipleImplementationsServiceImpl.class.getName()+".add", Log.log.getMessages().get(1));
	}
}
