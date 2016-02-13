package com.facultad.checklist4planes.beans;

public class Item {

	int item_id;
	int category_id;
	int plane_id;
	
	String item_name;
	boolean item_active;
	
	public Item(int item_id, int category_id, int plane_id, String item_name) {
		super();
		this.item_id = item_id;
		this.category_id = category_id;
		this.plane_id = plane_id;
		this.item_name = item_name;
		this.item_active = false;
	}

	public Item(int item_id, int category_id, int plane_id, String item_name, boolean item_active) {
		super();
		this.item_id = item_id;
		this.category_id = category_id;
		this.plane_id = plane_id;
		this.item_name = item_name;
		this.item_active = item_active;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
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

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public boolean getItem_active() {
		return item_active;
	}

	public void setItem_active(boolean item_active) {
		this.item_active = item_active;
	}

	@Override
	public String toString() {
		return "Item [item_id=" + item_id + ", category_id=" + category_id
				+ ", plane_id=" + plane_id + ", item_name=" + item_name
				+ ", item_active=" + item_active + "]";
	}
}
