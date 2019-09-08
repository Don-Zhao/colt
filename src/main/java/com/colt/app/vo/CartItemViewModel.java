package com.colt.app.vo;

import java.math.BigDecimal;

public class CartItemViewModel {
	
	private int id;
	
	private ItemViewModel itemViewModel;
	
	private BigDecimal price;
	
	private int count;
	
	private int userId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ItemViewModel getItemViewModel() {
		return itemViewModel;
	}

	public void setItemViewModel(ItemViewModel itemViewModel) {
		this.itemViewModel = itemViewModel;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
