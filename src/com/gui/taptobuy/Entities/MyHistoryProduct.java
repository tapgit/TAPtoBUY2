package com.gui.taptobuy.Entities;

import android.graphics.Bitmap;

public class MyHistoryProduct {
	private int id;
	private String title;
	private double paidPrice;
	private double paidShippingPrice;
	private String imgLink;
	private Bitmap img;
	public MyHistoryProduct(int id, String title, double paidPrice,
			double paidShippingPrice, String imgLink, Bitmap img) {
		super();
		this.id = id;
		this.title = title;
		this.paidPrice = paidPrice;
		this.paidShippingPrice = paidShippingPrice;
		this.imgLink = imgLink;
		this.img = img;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getPaidPrice() {
		return paidPrice;
	}
	public void setPaidPrice(double paidPrice) {
		this.paidPrice = paidPrice;
	}
	public double getPaidShippingPrice() {
		return paidShippingPrice;
	}
	public void setPaidShippingPrice(double paidShippingPrice) {
		this.paidShippingPrice = paidShippingPrice;
	}
	public String getImgLink() {
		return imgLink;
	}
	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}
	public Bitmap getImg() {
		return img;
	}
	public void setImg(Bitmap img) {
		this.img = img;
	}
	
}
