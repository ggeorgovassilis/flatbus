package com.bazarooma.flatbus.demo.api;

import java.util.List;

import com.bazarooma.flatbus.demo.domain.Item;
import com.bazarooma.flatbus.demo.domain.ValidationMessage;
import com.bazarooma.flatbus.shared.BusListener;

/**
 * Listener for validating an order
 * @author george georgovassilis
 *
 */
public interface ValidationListener extends BusListener{

	List<ValidationMessage> validate(List<Item> items);

}
