package com.bazarooma.flatbus.unittests;

import org.junit.Before;
import org.junit.Test;

import com.bazarooma.flatbus.server.ServerEventBus;
import com.bazarooma.flatbus.server.ServerEventBusFactory;
import com.bazarooma.flatbus.test.OurEventBus;
import com.bazarooma.flatbus.test.mathservice.MathService;

import static org.mockito.Mockito.*;
import static junit.framework.Assert.*;

/**
 * Tests for the {@link ServerEventBus}
 * @author george georgovassilis
 *
 */
public class TestEventBusWithMocking {

	OurEventBus bus;
	MathService service;
	
	@Before
	public void setup(){
		ServerEventBusFactory factory = new ServerEventBusFactory();
		bus = factory.createAndProxy(OurEventBus.class);
		service = mock(MathService.class);
		bus.register(service);
	}
	
	@Test
	public void test(){
		when(service.add(1, 2)).thenReturn(3);
		int result = bus.add(1, 2);
		assertEquals(3, result);
		verify(service).add(1, 2);
	}
}
