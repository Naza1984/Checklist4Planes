package com.facultad.checklist4planes;

import java.util.List;

import com.facultad.checklist4planes.beans.Item;
import com.facultad.checklist4planes.constants.Constants;

import android.app.Application;
import android.util.Log;

public class MainApplication extends Application {
	
	private int plane_id = -1;
	private int category_id = -1;
	private List<Item> items = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Log.d(Constants.AppName, "MainApplication->onCreate");
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		
		Log.d(Constants.AppName, "MainApplication->onTerminate");
	}

	public int getPlane_id() {
		return plane_id;
	}

	public void setPlane_id(int plane_id) {
		this.plane_id = plane_id;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
