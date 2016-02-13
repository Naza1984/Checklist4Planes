package com.facultad.checklist4planes.beans;

public class Category {

	int category_id;
	int plane_id;
	String category_name;
	
	public Category(int category_id, int plane_id, String category_name) {
		super();
		this.category_id = category_id;
		this.plane_id = plane_id;
		this.category_name = category_name;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getPlane_id() {
		return plane_id;
	}

	public void setPlane_id(int plane_id) {
		this.plane_id = plane_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	@Override
	public String toString() {
		return "Category [category_id=" + category_id + ", plane_id="
				+ plane_id + ", category_name=" + category_name + "]";
	}
}
