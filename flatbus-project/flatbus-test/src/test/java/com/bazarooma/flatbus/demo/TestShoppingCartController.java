package com.bazarooma.flatbus.demo;

import org.junit.Before;
import org.junit.Test;

import com.bazarooma.flatbus.demo.api.StockService;
import com.bazarooma.flatbus.demo.api.ValidationListener;
import com.bazarooma.flatbus.demo.domain.Item;
import com.bazarooma.flatbus.demo.domain.Product;
import com.bazarooma.flatbus.demo.ui.IItemWidget;
import com.bazarooma.flatbus.demo.ui.IShoppingCartWidget;
import com.bazarooma.flatbus.demo.ui.ISummaryWidget;
import com.bazarooma.flatbus.demo.ui.ShoppingCartController;
import com.bazarooma.flatbus.server.ServerEventBus;
import com.bazarooma.flatbus.server.ServerEventBusFactory;

import static org.mockito.Mockito.*;

/**
 * Tests for the {@link ServerEventBus}: checks that event bus communicates
 * invocations between the {@link ShoppingCartController} and views or services
 * correctly.
 * 
 * @author george georgovassilis
 * 
 */
public class TestShoppingCartController {

	DemoBus bus;
	ShoppingCartController controller;
	ISummaryWidget summaryView;
	IShoppingCartWidget shoppingCartView;
	ValidationListener validationListener;
	StockService stockService;

	@Before
	public void setup() {
		bus = new ServerEventBusFactory().createAndProxy(DemoBus.class);
		summaryView = mock(ISummaryWidget.class);
		shoppingCartView = mock(IShoppingCartWidget.class);
		stockService = mock(StockService.class);
		validationListener = mock(ValidationListener.class);

		controller = new ShoppingCartController(bus, shoppingCartView, summaryView);

		bus.register(stockService);
		bus.register(controller);
		bus.register(validationListener);
	}

	@Test
	public void onAddProductToOrderButtonClicked() {
		// Setup test conditions
		final int productId = 1;
		final Product product = new Product(1, "Time machine", 99999);
		final IItemWidget itemWidget = mock(IItemWidget.class);
		final Item item = new Item(product, 1);

		when(stockService.findProductById(productId)).thenReturn(product);
		when(shoppingCartView.addItemWidget(product)).thenReturn(itemWidget);
		when(itemWidget.getValue()).thenReturn(item);
		bus.register(itemWidget);

		// Perform the method to test
		controller.onAddProductToOrderButtonClicked(1);

		// Verify invocation results
		verify(summaryView, atLeastOnce()).updateTotals(1, product.getUnitPrice());
		verify(summaryView, atLeastOnce()).setCheckoutButtonEnabled(true);

	}
}
