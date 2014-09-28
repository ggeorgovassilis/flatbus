package com.bazarooma.flatbus.demo.ui;

import java.util.List;

import com.bazarooma.flatbus.demo.DemoBus;
import com.bazarooma.flatbus.demo.domain.Product;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Shows a list of all available products in the warehouse and allows adding items to the
 * shopping cart
 * @author george georgovassilis
 *
 */
public class ProductSelectionWidget extends Composite{

	private DemoBus bus;
	private ListBox products = new ListBox();
	private Button addOrderButton = new Button("Add to order");
	
	private void updateProductList(){
		products.clear();
		List<Product> catalogue = bus.getAllProducts();
		for (Product p:catalogue)
			products.addItem(p.getName() + " "+p.getUnitPrice()+"€", ""+p.getId());
	}
	
	public ProductSelectionWidget(final DemoBus bus){
		this.bus = bus;
		FlowPanel panel = new FlowPanel();
		initWidget(panel);
		panel.add(products);
		panel.add(addOrderButton);
		addStyleName("ProductSelectionWidget");
		updateProductList();
		addOrderButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int index = products.getSelectedIndex();
				if (index<0)
					return;
				int id = Integer.parseInt(products.getValue(index));
				bus.onAddProductToOrderButtonClicked(id);
			}
		});
	}
}
