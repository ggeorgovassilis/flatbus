package com.bazarooma.flatbus.demo;

import com.bazarooma.flatbus.demo.api.StockService;
import com.bazarooma.flatbus.demo.services.impl.FraudPreventionServiceImpl;
import com.bazarooma.flatbus.demo.services.impl.StockServiceImpl;
import com.bazarooma.flatbus.demo.ui.ProductSelectionWidget;
import com.bazarooma.flatbus.demo.ui.ShoppingCartController;
import com.bazarooma.flatbus.demo.ui.ShoppingCartWidget;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Demo application of a simple shopping cart. The UI consists of a {@link ShoppingCartWidget} and a {@link ProductSelectionWidget}
 * UI and business logic is handled by the {@link ShoppingCartController} and data is handled by the {@link StockService}.
 * They are all wired together via the {@link DemoBus}
 * @author george georgovassilis
 */
public class DemoApplication implements EntryPoint{

	private ProductSelectionWidget productSelectionWidget;
	private ShoppingCartWidget shoppingCartWidget;
	private DemoBus bus;
	
	private void initUI(){
		FlowPanel container = new FlowPanel();
		RootPanel.get().add(container);
		productSelectionWidget = new ProductSelectionWidget(bus);
		container.add(productSelectionWidget);
		shoppingCartWidget = new ShoppingCartWidget(bus);
		container.add(shoppingCartWidget);
	}
	
	private void initServices(){
		bus = GWT.create(DemoBus.class);
		bus.register(new StockServiceImpl());
		bus.register(new FraudPreventionServiceImpl());
	}
	
	@Override
	public void onModuleLoad() {
		initServices();
		initUI();
	}

}
