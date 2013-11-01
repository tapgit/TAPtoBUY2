package com.gui.taptobuy.Entities;

public class Category {
	private String name;
	private boolean hasSubCategories;

	public Category(String name, boolean hasSubCategories){
		this.name = name;
		this.hasSubCategories= hasSubCategories;		
	}

	public String getName() {
		return name;
	}

	public boolean HasSubCategories() {
		return hasSubCategories;
	}
}
