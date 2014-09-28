package com.bazarooma.flatbus.demo.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.bazarooma.flatbus.demo.api.ValidationListener;
import com.bazarooma.flatbus.demo.domain.Item;
import com.bazarooma.flatbus.demo.domain.ValidationMessage;

/**
 * Performs various plausibility checks on an order and returns any objections as {@link ValidationMessage}s
 * @author george georgovassilis
 *
 */
public class FraudPreventionServiceImpl implements ValidationListener{

	@Override
	public List<ValidationMessage> validate(List<Item> items) {
		List<ValidationMessage> messages = new ArrayList<ValidationMessage>();
		int count = 0;
		int price = 0;
		for (Item item:items){
			count+=item.getQuantity();
			price+=item.getQuantity()*item.getProduct().getUnitPrice();
			if (item.getQuantity()>10)
				messages.add(new ValidationMessage(item.getProduct(), "Don't order more than 10 times the same item"));
		}
		if (count>10)
			messages.add(new ValidationMessage(null, "Don't order more than 10 items"));
		if (price>1000)
			messages.add(new ValidationMessage(null, "Don't order more than 1000 €"));
		return messages;
	}

	
}
