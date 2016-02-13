package com.facultad.checklist4planes.beans;

public class Plane {

	int plane_id;
	String plane_name;
	
	public Plane(int plane_id, String plane_name) {
		super();
		this.plane_id = plane_id;
		
		this.plane_name = plane_name;
	}

	public int getPlane_id() {
		return plane_id;
	}

	public void setPlane_id(int plane_id) {
		this.plane_id = plane_id;
	}

	public String getPlane_name() {
		return plane_name;
	}

	public void setPlane_name(String plane_name) {
		this.plane_name = plane_name;
	}

	@Override
	public String toString() {
		return "Plane [" + plane_name + "]";
	}
}
