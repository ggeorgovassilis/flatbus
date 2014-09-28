package com.bazarooma.flatbus.demo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bazarooma.flatbus.demo.DemoBus;
import com.bazarooma.flatbus.demo.api.OrderListener;
import com.bazarooma.flatbus.demo.api.UserInteractionListener;
import com.bazarooma.flatbus.demo.domain.Item;
import com.bazarooma.flatbus.demo.domain.Product;
import com.bazarooma.flatbus.demo.domain.ValidationMessage;

/**
 * UI controller that coordinates the {@link ProductSelectionWidget}, {@link ShoppingCartWidget}, {@link SummaryWidget} and
 * a list of {@link ItemWidget}s and handles user interaction over a {@link DemoBus} instance
 * @author george georgovassilis
 *
 */
public class ShoppingCartController implements OrderListener, UserInteractionListener{

	private DemoBus bus;
	private IShoppingCartWidget shoppingcart;
	private ISummaryWidget summary;
	private Map<Product, IItemWidget> productsToWidgets = new HashMap<Product, IItemWidget>();

	public ShoppingCartController(DemoBus bus, IShoppingCartWidget widget, ISummaryWidget summary) {
		this.bus = bus;
		this.shoppingcart = widget;
		this.summary = summary;
	}

	private List<Item> getItems() {
		List<Item> items = new ArrayList<Item>();
		for (Product product:productsToWidgets.keySet()) {
			items.add(productsToWidgets.get(product).getValue());
		}
		return items;
	}

	private void updateTotals(){
		List<Item> list = getItems();
		int price = 0;
		int count = 0;
		for (Item item:list){
			count+=item.getQuantity();
			price+=item.getProduct().getUnitPrice()*item.getQuantity();
		}
		summary.updateTotals(count, price);
		summary.setCheckoutButtonEnabled(count>0);
		bus.hideAllMessages();
	}
	
	private boolean validateOrder() {
		List<ValidationMessage> messages = new ArrayList<ValidationMessage>();
		List<Item> items = getItems();
		for (Item item : items) {
			ValidationMessage message = bus.validateThatItemIsInStock(item);
			if (message != null)
				messages.add(message);
		}
		List<ValidationMessage> orderValidationMessages = bus.validate(items);
		if (orderValidationMessages != null)
			messages.addAll(orderValidationMessages);
		bus.hideAllMessages();
		for (ValidationMessage message : messages)
			bus.show(message);
		return messages.isEmpty();
	}

	@Override
	public void onAddNewItemToOrder(Product product) {
		updateTotals();
	}

	@Override
	public void onModifyItemQuantityInOrder(Product product, int quantity) {
		updateTotals();
	}

	@Override
	public void onRemoveItemFromOrder(Product product) {
		updateTotals();
	}

	@Override
	public void onAddProductToOrderButtonClicked(int productId) {
		Product product = bus.findProductById(productId);
		if (!productsToWidgets.containsKey(product)) {
			IItemWidget itemWidget = shoppingcart.addItemWidget(product);
			productsToWidgets.put(product, itemWidget);
			bus.onAddNewItemToOrder(product);
		}
		bus.onModifyItemQuantityInOrder(product, 1);
		updateTotals();
	}

	@Override
	public void onIncreaseItemQuantityButtonClicked(Item item) {
		bus.onModifyItemQuantityInOrder(item.getProduct(), 1);
		updateTotals();
	}

	@Override
	public void onDecreaseItemQuantityButtonClicked(Item item) {
		int count = item.getQuantity()-1;
		bus.onModifyItemQuantityInOrder(item.getProduct(), -1);
		if (count == 0){
			IItemWidget widget = productsToWidgets.remove(item.getProduct());
			shoppingcart.removeItemWidget(widget);
			bus.onRemoveItemFromOrder(item.getProduct());
		}
		updateTotals();
	}

	@Override
	public void onCheckoutButtonClicked() {
		boolean validationOk = validateOrder();
		if (validationOk)
			bus.checkoutOrder();
	}
}
