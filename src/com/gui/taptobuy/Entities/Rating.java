package com.gui.taptobuy.Entities;

public class Rating {
	
	private String buyerUN;
	private int buyerRate;
	
	public Rating (String buyerUsername,int buyerRate){
		buyerUN = buyerUsername;
		this.buyerRate = buyerRate;
	}

	public String getBuyerUN() {
		return buyerUN;
	}

	public int getBuyerRate() {
		return buyerRate;
	}
	

}
