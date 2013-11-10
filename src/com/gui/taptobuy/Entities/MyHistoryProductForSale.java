package com.gui.taptobuy.Entities;

import android.graphics.Bitmap;

public class MyHistoryProductForSale extends MyHistoryProduct{
	private int quantity;

	public MyHistoryProductForSale(int id, String title, double paidPrice,
			double paidShippingPrice, String imgLink, Bitmap img, int quantity) {
		super(id, title, paidPrice, paidShippingPrice, imgLink, img);
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

}
