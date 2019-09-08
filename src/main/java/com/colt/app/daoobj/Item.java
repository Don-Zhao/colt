package com.colt.app.daoobj;

public class Item extends ItemInfo {
	private int stock;
	
	private int sales;

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}
}
