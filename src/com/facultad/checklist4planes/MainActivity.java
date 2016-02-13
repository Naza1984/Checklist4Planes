package com.facultad.checklist4planes;

import java.util.ArrayList;
import java.util.List;

import com.facultad.checklist4planes.beans.Plane;
import com.facultad.checklist4planes.constants.Constants;

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
import com.facultad.checklist4planes.db.SQLite;
import com.facultad.checklist4planes.R;

public class MainActivity extends Activity {

	MainApplication app;
	SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d(Constants.AppName, "MainActivity->onCreate");
		
		//Obtengo el Objeto Applicacion 
		app = (MainApplication) getApplication();
		
		//Llamo a la AsyncTask para obtener el listado de Aviones
		new GetPlanes().execute();
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
	
	//AsyncTask para Cargar la Lista de aviones
	class GetPlanes extends AsyncTask<Void, Void, List<Plane>> {
		private static final String TAG = "GetPlanes";

		@Override
		protected List<Plane> doInBackground(Void... params) {
			Log.d(Constants.AppName, "MainActivity->GetPlanes->doInBackground");
			Log.d(Constants.AppName, "MainActivity->GetPlanes->doInBackground");
			try {			
				Plane avion;			
				List<Plane> planes = new ArrayList<Plane>();				
				
				SQLite sqlite = new SQLite( MainActivity.this );
				sqlite.abrir();
				
				Cursor cursor = sqlite.getPlanes();
				
				if( cursor.moveToFirst() )
				{
					do
					{
						int plane_id = cursor.getInt(0);
						String plane_name = cursor.getString(1);
						avion = new Plane(plane_id,plane_name);

						planes.add( avion );
			            
					} while ( cursor.moveToNext() );
				}	
				
				sqlite.cerrar();
				return planes;
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(List<Plane> planes) {
			super.onPostExecute(planes);

			Log.d(Constants.AppName, "MainActivity->GetPlanes->onPostExecute");
			
			ListView listView = (ListView) findViewById(R.id.listViewPlanes);
			PlanesAdapter planeAdapter = new PlanesAdapter(planes);
			listView.setAdapter(planeAdapter);
		}
	}
	
	//Creo el Adapter
	class PlanesAdapter extends BaseAdapter implements ListAdapter {

		private final List<Plane> planes;
		
		public PlanesAdapter(List<Plane> planes) {
			this.planes = planes;
		}
		
		@Override
		public int getCount() {
			return planes.size();
		}

		@Override
		public Object getItem(int position) {
			return planes.get(position);
		}

		@Override
		public long getItemId(int position) {
			return planes.get(position).getPlane_id();
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Log.d(Constants.AppName, "MainActivity->PlanesAdapter->getView");
			
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.btn_plane, null);
			}
			
			Button btn_plane = (Button) convertView;
			btn_plane.setText(planes.get(position).getPlane_name());
			btn_plane.setId(planes.get(position).getPlane_id());
			btn_plane.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					//TODO: Aca tengo que llamar a la activity de las categorias
					Button btn_plane = (Button) v;
					
		        	app.setPlane_id(btn_plane.getId());
		        	
		        	Intent CategoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
		        	startActivity(CategoryIntent);
				}
			});
			
			return btn_plane;
		}
	}
}
