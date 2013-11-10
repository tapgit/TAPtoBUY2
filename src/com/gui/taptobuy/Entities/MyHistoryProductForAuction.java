package com.gui.taptobuy.Entities;

import android.graphics.Bitmap;

public class MyHistoryProductForAuction extends MyHistoryProduct {
	private int bidsAmount;

	public MyHistoryProductForAuction(int id, String title, double paidPrice,
			double paidShippingPrice, String imgLink, Bitmap img, int bidsAmount) {
		super(id, title, paidPrice, paidShippingPrice, imgLink, img);
		this.bidsAmount = bidsAmount;
	}

	public int getBidAmount() {
		return bidsAmount;
	}

	public void setBidAmount(int bidAmount) {
		this.bidsAmount = bidAmount;
	}
	
}
