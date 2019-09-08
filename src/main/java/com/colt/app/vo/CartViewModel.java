package com.colt.app.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartViewModel {
	
	private List<CartItemViewModel> items = new ArrayList<>();
	
	private BigDecimal balance;

	public List<CartItemViewModel> getItems() {
		return items;
	}

	public void setItems(List<CartItemViewModel> items) {
		this.items = items;
	}
	
	public void addItem(CartItemViewModel cartItemViewModel) {
		items.add(cartItemViewModel);
	}
	
	public void cancelItem(CartItemViewModel cartItemViewModel) {
		for (CartItemViewModel item : items) {
			if (item.getId() == cartItemViewModel.getId()) {
				items.remove(item);
				break;
			}
		}
	}
	
	public void clearCart() {
		items.clear();
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public BigDecimal getBalance() {
		this.balance = new BigDecimal(0);
		for (CartItemViewModel item : items) {
			BigDecimal countPrice = item.getPrice().multiply(new BigDecimal(item.getCount()));
			this.balance = this.balance.add(countPrice);
		}
		
		return this.balance;
	}
}
