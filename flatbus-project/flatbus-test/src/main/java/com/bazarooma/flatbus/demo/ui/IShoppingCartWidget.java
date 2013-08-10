package com.bazarooma.flatbus.demo.ui;

import com.bazarooma.flatbus.demo.domain.Product;

/**
 * Interface for the {@link ShoppingCartWidget}
 * @author george georgovassilis
 *
 */
public interface IShoppingCartWidget {

	int getItemWidgetCount();

	IItemWidget getItemWidget(int row);

	IItemWidget addItemWidget(Product product);

	void removeItemWidget(IItemWidget w);

}