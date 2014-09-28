package com.bazarooma.flatbus.demo;

import com.bazarooma.flatbus.demo.api.StockService;
import com.bazarooma.flatbus.demo.api.OrderListener;
import com.bazarooma.flatbus.demo.api.OrderSummaryListener;
import com.bazarooma.flatbus.demo.api.UserInteractionListener;
import com.bazarooma.flatbus.demo.api.ValidationListener;
import com.bazarooma.flatbus.demo.api.ValidationMessagesListener;
import com.bazarooma.flatbus.shared.EventBus;

/**
 * Marker interface for the demo application. This interface extends all listener interfaces which
 * are also implemented by various components such as controllers, services and widgets
 * @author george georgovassilis
 *
 */
public interface DemoBus extends EventBus, StockService, OrderListener, ValidationListener, OrderSummaryListener, ValidationMessagesListener, UserInteractionListener{

}
