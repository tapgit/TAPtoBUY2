package com.gui.taptobuy.Entities;

import android.graphics.Bitmap;

public class MyHistoryProductForAuction extends MyHistoryProduct {
	private int bidsAmount;

	public MyHistoryProductForAuction(int id, int order_id, String title,
			double paidPrice, double paidShippingPrice, String imgLink,
			Bitmap img, int bidsAmount) {
		super(id, order_id, title, paidPrice, paidShippingPrice, imgLink, img);
		this.bidsAmount = bidsAmount;
	}

	public int getBidsAmount() {
		return bidsAmount;
	}

	public void setBidsAmount(int bidsAmount) {
		this.bidsAmount = bidsAmount;
	}

	
}
