package com.facultad.checklist4planes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "dbChecklist";
	//versión de la base de datos
	private static final int VERSION = 1;
	
	//nombre de tabla y campos para aviones
	public final String TABLA_AVIONES = "Planes";
	public final String AVIONES_ID_AVION = "plane_id";
	public final String AVIONES_NOMBRE_AVION = "plane_name";
	
	//nombre de tabla y campos para categorias
	public final String TABLA_CATEGORIAS = "Categories";
	public final String CATEGORIA_ID_CATEGORIA = "category_id";
	public final String CATEGORIA_ID_AVION = "plane_id";
	public final String CATEGORIA_NOMBRE_CATEGORIA = "category_name";
	
	//nombre de tabla y campos para items
	public final String TABLA_ITEMS = "Items";
	public final String ITEMS_ID_ITEM = "item_id";
	public final String ITEMS_ID_CATEGORIA = "category_id";
	public final String ITEMS_ID_AVION = "plane_id";
	public final String ITEMS_DESC_ITEM = "item_name";
	public final String ITEMS_ACTIVO = "item_active";//por default el item no estará activo	
	
	private final String sql_aviones = 
			"CREATE TABLE " + TABLA_AVIONES + " ( " 
			+ AVIONES_ID_AVION + " INTEGER NOT NULL, " 
			+ AVIONES_NOMBRE_AVION + " TEXT NOT NULL, "
			+ " PRIMARY KEY ( " + AVIONES_ID_AVION + ") "
			+ " )";
	
	private final String sql_categorias = 
			"CREATE TABLE " + TABLA_CATEGORIAS + " ( " 
			+ CATEGORIA_ID_CATEGORIA + " INTEGER NOT NULL, " 
			+ CATEGORIA_ID_AVION + " INTEGER NOT NULL, "
			+ CATEGORIA_NOMBRE_CATEGORIA + " TEXT NOT NULL, "
			+ " PRIMARY KEY ( " + CATEGORIA_ID_CATEGORIA + "," + CATEGORIA_ID_AVION + "), "
			+ " FOREIGN KEY(" + CATEGORIA_ID_AVION + ") REFERENCES " + TABLA_AVIONES + "(" + AVIONES_ID_AVION + ") "
			+ " )";
	
	private final String sql_items = 
			"CREATE TABLE " + TABLA_ITEMS + " ( " 
			+ ITEMS_ID_ITEM + " INTEGER NOT NULL, " 
			+ ITEMS_ID_CATEGORIA + " INTEGER NOT NULL, "
			+ ITEMS_ID_AVION + " INTEGER NOT NULL, " 
			+ ITEMS_DESC_ITEM + " TEXT NOT NULL, " 
			+ ITEMS_ACTIVO + " INTEGER, " 
			+ " PRIMARY KEY ( " + ITEMS_ID_ITEM + "," + ITEMS_ID_CATEGORIA + "," + ITEMS_ID_AVION + ") ,"
			+ " FOREIGN KEY(" + ITEMS_ID_AVION + ") REFERENCES " + TABLA_AVIONES + "(" + AVIONES_ID_AVION + "), "
			+ " FOREIGN KEY(" + ITEMS_ID_CATEGORIA + ") REFERENCES " + TABLA_CATEGORIAS + "(" + CATEGORIA_ID_CATEGORIA + ") "
			+ " )";
	

	public SQLiteHelper(Context context) {		
		super( context, DB_NAME, null, VERSION );		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		
		 db.execSQL( sql_aviones );		 
		 db.execSQL( sql_categorias );
		 db.execSQL( sql_items );
		 
		 loadData(db);
	}

	@Override
	public void onUpgrade( SQLiteDatabase db,  int oldVersion, int newVersion ) {		
		if ( newVersion > oldVersion )
		{
			//eliminamos las tablas
			db.execSQL( "DROP TABLE IF EXISTS " + TABLA_AVIONES );
			db.execSQL( "DROP TABLE IF EXISTS " + TABLA_CATEGORIAS );
			db.execSQL( "DROP TABLE IF EXISTS " + TABLA_ITEMS );
			//y luego recreamos las tablas con los cambios
			db.execSQL( sql_aviones );		 
			db.execSQL( sql_categorias );
			db.execSQL( sql_items );
			
			loadData(db);
		}
	}
	
	public void loadData(SQLiteDatabase db)
	{
		 //aviones
		 db.execSQL("INSERT INTO PLANES VALUES (0,'Cessna 152') " );
		 db.execSQL("INSERT INTO PLANES VALUES (1,'Cessna 172') " );
		 db.execSQL("INSERT INTO PLANES VALUES (2,'Cessna 182') " );
		 
		 //categorias Cessna 152
		 db.execSQL("INSERT INTO CATEGORIES VALUES (0,0,'PREFLIGHT') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (1,0,'ENGINE START') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (2,0,'PRE-TAKEOFF') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (3,0,'PRE-LANDING') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (4,0,'AFTER-LANDING') " );
		 
		 //categorias Cessna 172
		 db.execSQL("INSERT INTO CATEGORIES VALUES (0,1,'CABIN CHECK') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (1,1,'EXTERIOR INSPECTION') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (2,1,'BEFORE ENGINE START') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (3,1,'AFTER ENGINE START') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (4,1,'TAXI') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (5,1,'BEFORE TAKEOFF') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (6,1,'TAKEOFF') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (7,1,'BEFORE LANDING') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (8,1,'AFTER LANDING CHECK') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (9,1,'ENGINE SHUTDOWN') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (10,1,'SECURING AIRCRAFT') " );
		 
		 //categorias Cessna 182
		 db.execSQL("INSERT INTO CATEGORIES VALUES (0,2,'PREFLIGHT') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (1,2,'ENGINE START') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (2,2,'PRE-TAKEOFF') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (3,2,'PRE-LANDING') " );
		 db.execSQL("INSERT INTO CATEGORIES VALUES (4,2,'AFTER-LANDING') " );
		 
		 
		 //items cessna 152
		 db.execSQL("INSERT INTO ITEMS VALUES (0,0,0,'Documents - ON BOARD',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,0,0,'Control Lock - REMOVE',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,0,0,'Avionics - OFF',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,0,0,'Flaps - EXTEND',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (4,0,0,'Lights - CHECK',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,1,0,'Flight plan - FILED',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,1,0,'Sets and Belts - SET AND SECURED',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,1,0,'Beacon - ON',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,1,0,'Start Engine - ENGAGE',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (4,1,0,'Avionics - ON',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,2,0,'Flaps - SET FOR T-O',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,2,0,'Trim - SET FOR T-O',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,2,0,'Mixture - FULL RICH',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,2,0,'Transponder - ALT',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,3,0,'Fuel selector - ON-BOTH',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,3,0,'Mixture - FULL RICH',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,3,0,'Carb Heat - ON',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,4,0,'Flaps - RETRACTED',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,4,0,'Transponder - STANDBY',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,4,0,'Carb Heat - OFF',0) " );
		 
		 //items Cessna 172
		 db.execSQL("INSERT INTO ITEMS VALUES (0,0,1,'Ignition Key ON GLARESHIELD',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,0,1,'Documents (AROW) CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,0,1,'Hobbs Meter CHECK TIME',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,0,1,'Control Lock REMOVE',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (4,0,1,'Electrical & Avionics OFF',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (5,0,1,'Master Switch ON',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (6,0,1,'Avionics Master Switch ON-CHECK FAN-OFF',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (7,0,1,'Annunciator Panel Switch TEST LIGHTS',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (8,0,1,'Fuel Gauges CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (9,0,1,'Flaps DOWN',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (10,0,1,'Exterior Lights CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (11,0,1,'Master Switch OFF',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (12,0,1,'Parking Brake ON',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,1,1,'Fuel Sumps SAMPLE (5)',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,1,1,'Fuselage Left Side CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,1,1,'Elevator/Rudder CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,1,1,'Tail Tie-down REMOVE',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (4,1,1,'Fuselage Right Side CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (5,1,1,'Right Flap & Aileron CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (6,1,1,'Wing Tie-down REMOVE',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (7,1,1,'Fuel Sumps SAMPLE (5)',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (8,1,1,'Main Wheel Tire/Brakes CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (9,1,1,'Chocks REMOVE',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (10,1,1,'Fuel Quantity (Right Tank) CHECK VISUALLY',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,2,1,'Seatbelts/Shoulder Harness FASTENED',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,2,1,'Brakes TEST & SET',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,2,1,'Fuel Selector BOTH',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,2,1,'Fuel Shutoff Valve ON (IN)',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,3,1,'Ignition Switch START',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,3,1,'Mixture (At Engine Start) RICH',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,3,1,'Engine RPM 1000 RPM',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,4,1,'Brakes CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,4,1,'Magnetic Compass MOVEMENT FREE',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,4,1,'Flight Instruments CHECK',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,5,1,'Parking Brakes SET',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,5,1,'Flight Controls FREE & CORRECT',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,5,1,'Flight Instruments SET',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,5,1,'Fuel Selector BOTH',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (4,5,1,'Elevator & Rudder Trim SET',0) " );

		 db.execSQL("INSERT INTO ITEMS VALUES (0,6,1,'LIGHTS (ALL) ON',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,6,1,'CAMERA (Transponder) ON',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,6,1,'ACTION (RPM, Oil Pres., Time) FULL POWER',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,7,1,'Seatbelts ADJUST',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,7,1,'Fuel Selector BOTH',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,7,1,'Engine Gauges CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,7,1,'Heading Indicator ALIGNED',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,8,1,'LIGHTS (Except Beacon) OFF',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,8,1,'CAMERA (Transponder) OFF',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,9,1,'Throttle IDLE',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,9,1,'Mags GROUND CHECK',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,9,1,'Throttle 1000 RPM',0) " );
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,10,1,'Hobbs & Tach RECORD',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,10,1,'Control Lock INSTALL',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,10,1,'Tiedowns/Chocks INSTALL',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,10,1,'Propeller (For Fuel) VERTICAL',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (4,10,1,'Fuel RIGHT TANK',0) " );
		 		 

		 //items cessna 182
		 db.execSQL("INSERT INTO ITEMS VALUES (0,0,2,'Documents - ON BOARD',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,0,2,'Control Lock - REMOVE',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,0,2,'Avionics - OFF',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,0,2,'Flaps - EXTEND',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (4,0,2,'Lights - CHECK',0) " );
		
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,1,2,'Flight plan - FILED',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,1,2,'Sets and Belts - SET AND SECURED',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,1,2,'Beacon - ON',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,1,2,'Start Engine - ENGAGE',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (4,1,2,'Avionics - ON',0) " );
		 
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,2,2,'Flaps - SET FOR T-O',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,2,2,'Trim - SET FOR T-O',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,2,2,'Mixture - FULL RICH',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (3,2,2,'Transponder - ALT',0) " );
		 
		 
		 db.execSQL("INSERT INTO ITEMS VALUES (0,3,2,'Fuel selector - ON-BOTH',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,3,2,'Mixture - FULL RICH',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,3,2,'Carb Heat - ON',0) " );
		 
		
		 db.execSQL("INSERT INTO ITEMS VALUES (0,4,2,'Flaps - RETRACTED',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (1,4,2,'Transponder - STANDBY',0) " );
		 db.execSQL("INSERT INTO ITEMS VALUES (2,4,2,'Carb Heat - OFF',0) " );
		 
		 
	}
	
}
