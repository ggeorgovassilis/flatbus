package com.bazarooma.flatbus.demo.ui;


import com.bazarooma.flatbus.demo.DemoBus;
import com.bazarooma.flatbus.demo.domain.ValidationMessage;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Shows item count and totals for an order
 * @author george georgovassilis
 *
 */
public class SummaryWidget extends Composite implements ISummaryWidget{

	private final static String EMPTY = "Your shopping cart is empty";
	private final static String TOTALS = "Totals: ";
	private Label label = new Label(EMPTY);
	private Label itemCount = new Label();
	private Label priceLabel = new Label();
	private Label messagesLabel = new Label();
	private Button checkoutButton = new Button("Check out");

	public SummaryWidget(final DemoBus bus){
		FlowPanel panel = new FlowPanel();
		initWidget(panel);
		addStyleName("SummaryWidget");
		panel.add(label);
		panel.add(itemCount);
		panel.add(priceLabel);
		priceLabel.addStyleName("priceLabel");
		panel.add(messagesLabel);
		messagesLabel.addStyleName("validationLabel");
		panel.add(checkoutButton);
		checkoutButton.addStyleName("checkoutButton");
		checkoutButton.setEnabled(false);
		bus.register(this);
		checkoutButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				bus.onCheckoutButtonClicked();
			}
		});
	}
	
	@Override
	public void hideAllMessages() {
		messagesLabel.setText("");
	}

	@Override
	public void show(ValidationMessage message) {
		if (message.getAboutProduct() == null)
			messagesLabel.setText(message.getMessage());
	}

	@Override
	public void updateTotals(int itemCount, int price) {
		this.itemCount.setText(itemCount==0?"":""+itemCount);
		this.priceLabel.setText(itemCount==0?"":price+"€");

		label.setText(itemCount==0?EMPTY:TOTALS);
	}
	
	@Override
	public void setCheckoutButtonEnabled(boolean enabled){
		checkoutButton.setEnabled(enabled);
	}

}
