package com.bazarooma.flatbus.demo.ui;


import com.bazarooma.flatbus.demo.DemoBus;
import com.bazarooma.flatbus.demo.domain.Item;
import com.bazarooma.flatbus.demo.domain.Product;
import com.bazarooma.flatbus.demo.domain.ValidationMessage;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Shows a single line in the shopping cart
 * @author george georgovassilis
 *
 */
public class ItemWidget extends Composite implements IItemWidget{

	private static Label label(String text, String style){
		Label label = new Label(text);
		label.addStyleName(style);
		return label;
	}
	
	private DemoBus bus;
	private Button increaseCountButton = new Button("+");
	private Button decreaseCountButton = new Button("-");
	private Label priceLabel = label("", "priceLabel");
	private Label productLabel = label("", "productLabel");
	private Product product;
	private int quantity;
	private Label validationLabel = label("", "validationLabel");
	
	/* (non-Javadoc)
	 * @see com.bazarooma.flatbus.demo.ui.IItemWidget#getValue()
	 */
	@Override
	public Item getValue(){
		return new Item(product, quantity);
	}
	
	/* (non-Javadoc)
	 * @see com.bazarooma.flatbus.demo.ui.IItemWidget#setQuantity(int)
	 */
	@Override
	public void setQuantity(int quantity){
		this.quantity = quantity;
		update();
	}
	
	public ItemWidget(final DemoBus bus, final Product product, int quantity){
		this.bus = bus;
		this.product = product;
		this.quantity = quantity;
		FlowPanel panel = new FlowPanel();
		FlowPanel handles = new FlowPanel();
		handles.addStyleName("handles");
		initWidget(panel);
		addStyleName("ItemWidget");
		panel.add(productLabel);
		handles.add(priceLabel);
		handles.add(increaseCountButton);
		handles.add(decreaseCountButton);
		panel.add(handles);
		panel.add(validationLabel);
		update();
		
		increaseCountButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				bus.onIncreaseItemQuantityButtonClicked(getValue());
			}
		});
		decreaseCountButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				bus.onDecreaseItemQuantityButtonClicked(getValue());
			}
		});
	}

	private void update(){
		productLabel.setText(quantity+" x "+product.getName()+" ("+product.getUnitPrice()+" €)");
		priceLabel.setText((product.getUnitPrice() * quantity)+" €");
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		bus.register(this);
	}
	
	@Override
	protected void onUnload() {
		super.onUnload();
		bus.unregister(this);
	}

	@Override
	public void onModifyItemQuantityInOrder(Product product, int modificationInQuantity) {
		if (product == this.product){
			setQuantity(this.quantity+modificationInQuantity);
		}
	}

	@Override
	public void onRemoveItemFromOrder(Product product) {
		if (product == this.product)
			removeFromParent();
	}

	@Override
	public void onAddNewItemToOrder(Product product) {
	}

	@Override
	public void hideAllMessages() {
		validationLabel.setText("");
	}

	@Override
	public void show(ValidationMessage message) {
		if (message.getAboutProduct() == product)
			validationLabel.setText(message.getMessage());
	}
}
