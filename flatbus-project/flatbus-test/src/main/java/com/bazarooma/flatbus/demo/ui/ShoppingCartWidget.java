package com.bazarooma.flatbus.demo.ui;

import com.bazarooma.flatbus.demo.DemoBus;
import com.bazarooma.flatbus.demo.domain.Product;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Displays a list of items in an order 
 * @author george georgovassilis
 *
 */
public class ShoppingCartWidget extends Composite implements IShoppingCartWidget{

	private DemoBus bus;
	private FlowPanel orders = new FlowPanel();
	private SummaryWidget summary;
	private ShoppingCartController presenter;
	private Label label = new Label("Your shopping cart");
	
	@Override
	protected void onLoad() {
		super.onLoad();
		presenter = new ShoppingCartController(bus, this, summary);
		bus.register(presenter);
	}

	public ShoppingCartWidget(final DemoBus bus) {
		this.bus = bus;
		FlowPanel container = new FlowPanel();
		initWidget(container);
		addStyleName("ShoppingCartWidget");
		summary = new SummaryWidget(bus);
		container.add(label);
		label.addStyleName("title");
		container.add(orders);
		container.add(summary);
	}
	
	@Override
	public int getItemWidgetCount(){
		return orders.getWidgetCount();
	}
	
	@Override
	public IItemWidget getItemWidget(int row){
		return (IItemWidget)orders.getWidget(row);
	}

	
	@Override
	public ItemWidget addItemWidget(Product product){
		ItemWidget w = new ItemWidget(bus, product, 0);
		orders.add(w);
		return w;
	}
	
	@Override
	public void removeItemWidget(IItemWidget w){
		orders.remove(w);
	}

	
}
