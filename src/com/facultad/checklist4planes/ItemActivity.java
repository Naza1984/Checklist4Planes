package com.facultad.checklist4planes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.facultad.checklist4planes.beans.Item;
import com.facultad.checklist4planes.constants.Constants;
import com.facultad.checklist4planes.db.SQLite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends Activity {

	MainApplication app;
	SharedPreferences settings;
	boolean listSaved = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);
		
		Log.d(Constants.AppName, "ItemActivity->onCreate");
		
		//Obtengo el Objeto Applicacion 
		app = (MainApplication) getApplication();
		
		//Obtengo plane_id y el categpry_id
		int plane_id = app.getPlane_id();
		int category_id = app.getCategory_id();

		new GetItemsTitle().execute(category_id);
		new GetItems().execute(plane_id, category_id);

		//Boton Guardar		
		Button btnSave = (Button) findViewById(R.id.btnSave);
		
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		if(settings.getBoolean("AutoSaveOnClick", false))
		{
			Log.d(Constants.AppName, "CategoryActivity->onCreate->AutoSaveOnClick = TRUE");
			btnSave.setEnabled(false);
		}
		else
		{
			Log.d(Constants.AppName, "CategoryActivity->onCreate->AutoSaveOnClick = FALSE");
			btnSave.setOnClickListener(new Button.OnClickListener()
			{
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
					//Llamo a la asynktask para guardar
					new SaveItems().execute(app.getItems());
					
					//Marco que no hay cambios pendientes
					listSaved = true;
				}
			});
		}
		
		//Boton Resetear
		Button btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				//Llamo a la asynktask para Resetear
				new ResetItems().execute(app.getPlane_id(), app.getCategory_id());
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onBackPressed() {
		// No debemos llamar al constructor de la clase padre porque queremos controlar nosotros la salida
		// super.onBackPressed();
		
		Log.d(Constants.AppName, "ItemActivity->onBackPressed");
		
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		
		//Verificamos si esta activo el AutoSaveOnClick para deshabilitar el AutoSaveOnBack o el popup
		if(!settings.getBoolean("AutoSaveOnClick", false))
		{
			//Verifico si el AutoSaveOnBack esta activo para guardar la lista
			if(settings.getBoolean("AutoSaveOnBack", false))
			{
				Log.d(Constants.AppName, "ItemActivity->onBackPressed->AutoSaveOnBack = TRUE");
				new SaveItems().execute(app.getItems());
				ItemActivity.this.finish();
			}
			//Verifico si se han hecho cambios, en tal caso muestro el popup de guardado
			else if(!listSaved)
			{
				Log.d(Constants.AppName, "ItemActivity->onBackPressed->AutoSaveOnBack = FALSE");
				
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
	
				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage(R.string.savedialog_ask_question_1)
		           .setCancelable(false)
		           .setPositiveButton(R.string.save_msg, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		            	   new SaveItems().execute(app.getItems());
		            	   Log.d(Constants.AppName, "ItemActivity->onBackPressed->setPositiveButton");
		            	   ItemActivity.this.finish();
		               }
		           })
		           .setNegativeButton(R.string.discard_msg, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		                   //dialog.cancel();
		                   Log.d(Constants.AppName, "ItemActivity->onBackPressed->setNegativeButton");
		                   ItemActivity.this.finish();
		               }
		           });
	
				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
				//No puedo finalizar la activity en este lugar porque tambien se cerraria el alert
			}
			//Si AutoSaveOnBack esta desactivado y la lista no fue modificada
			else
			{
				ItemActivity.this.finish();
			}
		}
		//Si AutoSaveOnClick esta activada
		else
		{
			ItemActivity.this.finish();
		}
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

	class GetItemsTitle extends AsyncTask<Integer, Void, String> {
		private static final String TAG = "GetItemsTitle";
		
		@Override
		protected String doInBackground(Integer... params) {
			
			Log.d(Constants.AppName, "ItemActivity->GetItemsTitle->doInBackground");
			
			//obtengo el id del avion elegido y pasado a esta tarea a traves del inicio de la  Activity
			int category_id = params[0];
			
			try {				
				//seteo contexto y abro conexion a sqlite
				SQLite sqlite = new SQLite( ItemActivity.this );
				sqlite.abrir();
				
				//obtengo de la bbdd el nombre del avion con el id seleccionado
				String title = sqlite.getCategoryById(category_id);
				
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
			
			Log.d(Constants.AppName, "ItemActivity->GetItemsTitle->onPostExecute");
			
			TextView tvTitle = (TextView) findViewById(R.id.textViewItemsTittle);
			tvTitle.setText(title);
		}
		
	}
	
	class GetItems extends AsyncTask<Integer, Void, List<Item>> {
		private static final String TAG = "GetItems";
		
		@Override
		protected List<Item> doInBackground(Integer... params) {
			
			Log.d(Constants.AppName, "ItemActivity->GetItems->doInBackground");
			
			//obtengo el id del avion y de categoria elegidos y pasados a esta tarea a traves del inicio de la Activity
			int plane_id = params[0];
			int category_id = params[1];
			
			try {
				Item item;
				List<Item> items = new ArrayList<Item>();			
				
				SQLite sqlite = new SQLite( ItemActivity.this );
				sqlite.abrir();
				
				Cursor cursor = sqlite.getItems(plane_id, category_id);
				
				if( cursor.moveToFirst() )
				{
					do
					{
						int item_id = cursor.getInt(0);
						String item_name = cursor.getString(1);
						int item_active = cursor.getInt(2);

						item = new Item(item_id, category_id, plane_id, item_name);
						if(item_active == 1)
							item.setItem_active(true);
						else
							item.setItem_active(false);
						
						items.add( item );
					} while ( cursor.moveToNext() );
				}	
				
				sqlite.cerrar();
				return items;
				
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(List<Item> items) {
			// TODO Auto-generated method stub
			super.onPostExecute(items);
			
			Log.d(Constants.AppName, "ItemActivity->GetItems->onPostExecute");
			
			//Guardo la referencia a la lista de items
			app.setItems(items);
			
			ListView listView = (ListView) findViewById(R.id.listViewItems);
			ItemsAdapter itemsAdapter = new ItemsAdapter(items);
			listView.setAdapter(itemsAdapter);
		}
	}
	
	//Creo el Adapter
	class ItemsAdapter extends BaseAdapter implements ListAdapter {

		private final List<Item> items;
		
		public ItemsAdapter(List<Item> items) {
			this.items = items;
		}
		
		public List<Item> getListItems()
		{
			return items;
		}
		
		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return items.get(position).getItem_id();
		}
		
		public int getCheckedItemsCount()
		{
			int count = 0;
			for(Item i:items)
			{
				if(i.getItem_active())
					count++;
			}
			return count;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
//			Log.d(Constants.AppName, "ItemActivity->ItemsAdapter->getView");
			
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.ckdtx_item, null);
			}
			
			CheckedTextView ckdtx_item = (CheckedTextView) convertView;
			ckdtx_item.setId(items.get(position).getItem_id());
			ckdtx_item.setText(items.get(position).getItem_name());
			if(items.get(position).getItem_active())
			{
				ckdtx_item.setChecked(true);
				ckdtx_item.setTextColor(Color.parseColor("#00FF00"));
			}
			else
			{
				ckdtx_item.setChecked(false);
				ckdtx_item.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
			}
			
			ckdtx_item.setOnClickListener(new CheckedTextView.OnClickListener(){
				@Override
				public void onClick(View v) {
					
					Log.d(Constants.AppName, "ItemActivity->ItemsAdapter->getView->onClick");
					
					// Marco la lista como que fue modificada, pero no necesariamente guardada
					listSaved = false;
					
					CheckedTextView ckdtx_item = (CheckedTextView) v;
					
					TextView tv = (TextView) findViewById(R.id.textViewSelected);
					
					if(ckdtx_item.isChecked() == true)
					{
						ckdtx_item.setChecked(false);
						ckdtx_item.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
						items.get(ckdtx_item.getId()).setItem_active(false);
					}
					else
					{
						ckdtx_item.setChecked(true);
						ckdtx_item.setTextColor(Color.parseColor("#00FF00"));
						items.get(ckdtx_item.getId()).setItem_active(true);
					}
					
					tv.setText(getCheckedItemsCount() + "/" + getCount());
					
					settings = PreferenceManager.getDefaultSharedPreferences(ItemActivity.this);
					if(settings.getBoolean("AutoSaveOnClick", false))
					{
						//Almaceno el Item en la Base de Datos
						new SaveItem().execute(items.get(ckdtx_item.getId()));
					}
				}
			});
			
			return ckdtx_item;
		}
	}
	
	class ResetItems extends AsyncTask<Integer, Void, Integer> {
		private static final String TAG = "ResetItems";
		
		@Override
		protected Integer doInBackground(Integer... params) {
			int items_updated = 0;
			//obtengo el id del avion y de categoria elegidos y pasados a esta tarea a traves del inicio de la Activity
			int plane_id = params[0];
			int category_id = params[1];
			
			try {
				SQLite sqlite = new SQLite( ItemActivity.this );
				sqlite.abrir();
				
				items_updated = sqlite.ResetItems(plane_id, category_id);
				
				if(items_updated > 0)
					Log.i("ItemActivity", "AsyncTask ResetItems: Items actualizados = " + items_updated );
				
				sqlite.cerrar();
				
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
			return items_updated;
		}

		@Override
		protected void onPostExecute(Integer items_updated) {
			// TODO Auto-generated method stub
			super.onPostExecute(items_updated);
			
			Toast.makeText(getBaseContext(), getString(R.string.chklReseted) + "..." ,Toast.LENGTH_SHORT).show();
			
			app = (MainApplication) getApplication();
			
			int plane_id = app.getPlane_id();
			int category_id = app.getCategory_id();
				
			new GetItems().execute(plane_id, category_id);
		}
	}
	
	class SaveItem extends AsyncTask<Item, Void, Void> {
		private static final String TAG = "SaveItem";

		@Override
		protected Void doInBackground(Item... params) {
			// TODO Auto-generated method stub
			Log.d(Constants.AppName, "ItemActivity->SaveItem->doInBackground");
			
			int item_id;
			int plane_id;
			int category_id;
			int item_active;
			
			Item item = params[0];
			
			try {	
				SQLite sqlite = new SQLite( ItemActivity.this );
				sqlite.abrir();
				
				item_id = item.getItem_id();
				plane_id = item.getPlane_id();
				category_id = item.getCategory_id();
				if (item.getItem_active())
					item_active = Constants.INT_TRUE;
				else
					item_active = Constants.INT_FALSE;
				
				sqlite.saveItem(item_id, plane_id, category_id, item_active);

				sqlite.cerrar();
				
				Log.d(Constants.AppName, "ItemActivity->SaveItem->doInBackground->Item Almacenado...");
				
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
			return null;
		}
	}
	
	class SaveItems extends AsyncTask<List<Item>, Void, Integer> {
		private static final String TAG = "SaveItems";
		
		@Override
		protected Integer doInBackground(List<Item>... params) {			
			Log.d(Constants.AppName, "ItemActivity->SaveItems->doInBackground");
			int item_id;
			int plane_id;
			int category_id;
			int item_active;
			int items_updated = 0;
			
			List<Item> items = params[0];
			
			try {	
				SQLite sqlite = new SQLite( ItemActivity.this );
				sqlite.abrir();
				
				Iterator<Item> iterator = items.iterator();
				while (iterator.hasNext()) {
					Item item = iterator.next();
					item_id = item.getItem_id();
					plane_id = item.getPlane_id();
					category_id = item.getCategory_id();
					if (item.getItem_active())
						item_active = Constants.INT_TRUE;
					else
						item_active = Constants.INT_FALSE;
					
					if (sqlite.saveItem(item_id, plane_id, category_id, item_active) > 0)
						items_updated++;
				}
				
				sqlite.cerrar();
				return items_updated;
				
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
			return items_updated;
			
		}

		@Override
		protected void onPostExecute(Integer items_updated) {
			// TODO Auto-generated method stub
			super.onPostExecute(items_updated);
			
			Toast.makeText(getBaseContext(), getString(R.string.chklSaved) + "..." ,Toast.LENGTH_SHORT).show();
		}
	}
	
}
