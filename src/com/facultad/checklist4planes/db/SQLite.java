package com.facultad.checklist4planes.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLite {

	private SQLiteHelper sqliteHelper;
	private SQLiteDatabase db;	
	
	public SQLite(Context context)
	{
		sqliteHelper = new SQLiteHelper( context );
	}

	public void abrir(){
		Log.i("SQLite", "Se abre conexion a la base de datos " + sqliteHelper.getDatabaseName() );
		db = sqliteHelper.getWritableDatabase(); 		
	}
	
	public void cerrar()
	{
		Log.i("SQLite", "Se cierra conexion a la base de datos " + sqliteHelper.getDatabaseName() );
		sqliteHelper.close();		
	}
	
	/* Obtiene todos los aviones */
	public Cursor getPlanes()
	{
		return db.query( sqliteHelper.TABLA_AVIONES ,				
					new String[]{
							sqliteHelper.AVIONES_ID_AVION ,
							sqliteHelper.AVIONES_NOMBRE_AVION
								  }, 
				null, null, null, null, null);
	}
	
	/* Obtiene el nombre del avion para el id pasado como parametro */
	public String getPlaneById(int plane_id)
	{
		String desc; 
		String[] tableColumns = new String[]{
			sqliteHelper.AVIONES_NOMBRE_AVION
		};
		
		//String whereClause = "CAST(" + sqliteHelper.AVIONES_ID_AVION + " as TEXT) = ?"; 
		
		String whereClause = sqliteHelper.AVIONES_ID_AVION + " = ? ";
		String[] whereArgs = new String[] {
				String.valueOf(plane_id)
			};
		

		Cursor cursor = db.query(sqliteHelper.TABLA_AVIONES ,				
						tableColumns, 
						whereClause, whereArgs, null, null, null);
		
		cursor.moveToFirst();
		desc = cursor.getString(0);
		
		return desc;
	}
	
	/* Obtiene el nombre de la categoria para el id pasado como parametro */
	public String getCategoryById(int category_id)
	{
		String desc; 
		String[] tableColumns = new String[]{
			sqliteHelper.CATEGORIA_NOMBRE_CATEGORIA
		};
		
		//String whereClause = "CAST(" + sqliteHelper.CATEGORIA_ID_CATEGORIA + " as TEXT) = ?"; 
		
		String whereClause = sqliteHelper.CATEGORIA_ID_CATEGORIA + " = ? ";
		String[] whereArgs = new String[] {
				String.valueOf(category_id)
			};
		

		Cursor cursor = db.query(sqliteHelper.TABLA_CATEGORIAS ,				
						tableColumns, 
						whereClause, whereArgs, null, null, null);
		
		cursor.moveToFirst();
		desc = cursor.getString(0);
		
		return desc;
	}
	
	/* Obtiene todas las categorias para un avion dado */
	public Cursor getCategories(int plane_id)
	{
		String[] tableColumns = new String[]{
				sqliteHelper.CATEGORIA_ID_CATEGORIA ,
				sqliteHelper.CATEGORIA_NOMBRE_CATEGORIA
			};
		
		String whereClause = sqliteHelper.CATEGORIA_ID_AVION + " = ? ";
		String[] whereArgs = new String[] {
				String.valueOf(plane_id)
			};
		
		return db.query( sqliteHelper.TABLA_CATEGORIAS,				
				tableColumns, whereClause, whereArgs, null, null, null);
	}
		

	/* Obtiene todos los items de una categoria y avion en particular */
	public Cursor getItems(int plane_id, int category_id)
	{
		String[] tableColumns = new String[]{
				sqliteHelper.ITEMS_ID_ITEM,
				sqliteHelper.ITEMS_DESC_ITEM,
				sqliteHelper.ITEMS_ACTIVO
			};
		
		String whereClause = sqliteHelper.ITEMS_ID_AVION + " = ? AND " + sqliteHelper.ITEMS_ID_CATEGORIA +  " = ? ";
		String[] whereArgs = new String[] {
				String.valueOf(plane_id),
				String.valueOf(category_id)
			};
		
		return db.query( sqliteHelper.TABLA_ITEMS ,				
				tableColumns, whereClause, whereArgs, null, null, null);
	}
	
	
	/* limpia todos los items de una categoria y avion en particular */
	public int ResetItems(int plane_id, int category_id){
		int items_updated = 0;
		
		//Establecemos los campos-valores a actualizar
		ContentValues values = new ContentValues();
		values.put(sqliteHelper.ITEMS_ACTIVO, String.valueOf(0));
		
		String whereClause = sqliteHelper.ITEMS_ID_AVION + " = ? AND " + sqliteHelper.ITEMS_ID_CATEGORIA +  " = ? ";
		String[] whereArgs = new String[] {
				String.valueOf(plane_id),
				String.valueOf(category_id),
			};
		 
		items_updated = db.update(sqliteHelper.TABLA_ITEMS, values, whereClause, whereArgs);
		
		return items_updated;
	}
		
	public int saveItem(int item_id, int plane_id, int category_id, int item_active){
		int items_updated = 0;
		
		//Establecemos los campos-valores a actualizar
		ContentValues values = new ContentValues();
		values.put(sqliteHelper.ITEMS_ACTIVO, String.valueOf(item_active));
		
		String whereClause = sqliteHelper.ITEMS_ID_ITEM + " = ? AND " + sqliteHelper.ITEMS_ID_AVION + " = ? AND " + sqliteHelper.ITEMS_ID_CATEGORIA +  " = ? ";
		String[] whereArgs = new String[] {
				String.valueOf(item_id),
				String.valueOf(plane_id),
				String.valueOf(category_id),
			};
		 
		items_updated = db.update(sqliteHelper.TABLA_ITEMS, values, whereClause, whereArgs);
		
		return items_updated;
	}
}
