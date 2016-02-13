package com.facultad.checklist4planes;

import java.util.ArrayList;
import java.util.List;

import com.facultad.checklist4planes.beans.Category;
import com.facultad.checklist4planes.constants.Constants;
import com.facultad.checklist4planes.db.SQLite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryActivity extends Activity {

	MainApplication app;
	SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		Log.d(Constants.AppName, "CategoryActivity->onCreate");
		
		//Obtengo el Objeto Applicacion 
		app = (MainApplication) getApplication();
		
		//Obtengo el plane_id
		int plane_id = app.getPlane_id();
		
		new GetCategoriesTitle().execute(plane_id);
		new GetCategories().execute(plane_id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent settingsActivity = new Intent(this, SettingsActivity.class);
			startActivity(settingsActivity);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class GetCategoriesTitle extends AsyncTask<Integer, Void, String> {
		private static final String TAG = "GetCategoriesTitle";
		
		@Override
		protected String doInBackground(Integer... params) {
			
			Log.d(Constants.AppName, "CategoryActivity->GetCategoriesTitle->doInBackground");
			
			//obtengo el id del avion elegido y pasado a esta tarea a traves del inicio de la  Activity
			int plane_id = params[0];
			
			try {				
				//seteo contexto y abro conexion a sqlite
				SQLite sqlite = new SQLite( CategoryActivity.this );
				sqlite.abrir();
				
				//obtengo de la bbdd el nombre del avion con el id seleccionado
				String title = sqlite.getPlaneById(plane_id);
				
				sqlite.cerrar();
				return title;
				
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				return null;
			}		
			
		}

		@Override
		protected void onPostExecute(String title) {
			// TODO Auto-generated method stub
			super.onPostExecute(title);
			
			Log.d(Constants.AppName, "CategoryActivity->GetCategoriesTitle->onPostExecute");
			
			TextView tvTitle = (TextView) findViewById(R.id.textViewCategoriesTittle);
			tvTitle.setText(title);
		}
		
	}
	
	class GetCategories extends AsyncTask<Integer, Void, List<Category>> {
		private static final String TAG = "GetCategories";
		
		@Override
		protected List<Category> doInBackground(Integer... params) {
			
			Log.d(Constants.AppName, "CategoryActivity->GetCategories->doInBackground");
			
			//obtengo el id del avion elegido y pasado a esta tarea a traves del inicio de la  Activity
			int plane_id = params[0];
			
			try {			
				Category category;
				List<Category> categories = new ArrayList<Category>();				
				
				SQLite sqlite = new SQLite( CategoryActivity.this );
				sqlite.abrir();
				
				Cursor cursor = sqlite.getCategories(plane_id);
				
				if( cursor.moveToFirst() )
				{
					do
					{
						int category_id = cursor.getInt(0);
						String category_name = cursor.getString(1);
						category = new Category(category_id,plane_id,category_name);

						categories.add( category );
			            
					} while ( cursor.moveToNext() );
				}	
				
				sqlite.cerrar();
				return categories;
				
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(List<Category> categories) {
			// TODO Auto-generated method stub
			super.onPostExecute(categories);
			
			Log.d(Constants.AppName, "CategoryActivity->GetCategories->onPostExecute");
			
			ListView listView = (ListView) findViewById(R.id.listViewCategories);
			CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categories);
			listView.setAdapter(categoriesAdapter);
		}
	}
	
	//Creo el Adapter
	class CategoriesAdapter extends BaseAdapter implements ListAdapter {

		private final List<Category> categories;
		
		public CategoriesAdapter(List<Category> categories) {
			this.categories = categories;
		}
		
		@Override
		public int getCount() {
			return categories.size();
		}

		@Override
		public Object getItem(int position) {
			return categories.get(position);
		}

		@Override
		public long getItemId(int position) {
			return categories.get(position).getCategory_id();
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Log.d(Constants.AppName, "CategoryActivity->CategoriesAdapter->getView");
			
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.btn_category, null);
			}
			
			Button btn_category = (Button) convertView;
			btn_category.setText(categories.get(position).getCategory_name());
			btn_category.setId(categories.get(position).getCategory_id());
			btn_category.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					//TODO: Aca tengo que llamar a la activity de las categorias
					Button btn_category = (Button) v;
					
					app.setCategory_id(btn_category.getId());
					
		        	Intent ItemIntent = new Intent(CategoryActivity.this, ItemActivity.class);
		        	startActivity(ItemIntent);
				}
			});
			
			return btn_category;
		}
	}
}
