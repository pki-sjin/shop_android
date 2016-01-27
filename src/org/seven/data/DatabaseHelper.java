package org.seven.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public DatabaseHelper(Context context) {
		super(context, "1510", null, 5);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE " + "shop_table" + " ("
//                + "ID" + " LONG PRIMARY KEY,"
//                + "SHOP_ID" + " TEXT,"
//                + "SHOP" + " TEXT,"
//                + "SHOP_PINYIN" + " TEXT,"
//                + "SELL_VALUE1" + " TEXT,"
//                + "SELL_VALUE2" + " TEXT,"
//                + "TYPE" + " INTEGER,"
//                + "SHOP_FROM" + " INTEGER,"
//                + "REGION" + " TEXT,"
//                + "REGION_PINYIN" + " TEXT"
//                + ");");
//		
//		db.execSQL("CREATE TABLE " + "sum_table" + " ("
//                + "ID" + " LONG PRIMARY KEY,"
//                + "SUM_SELL_VALUE1" + " TEXT,"
//                + "SUM_SELL_VALUE2" + " TEXT,"
//                + "SUM_TIME" + " TEXT"
//                + ");");
//		
//		db.execSQL("CREATE TABLE " + "shop_table_date" + " ("
//                + "ID" + " LONG PRIMARY KEY,"
//                + "SHOP_ID" + " TEXT,"
//                + "SHOP_DATE" + " TEXT,"
//                + "SHOP" + " TEXT,"
//                + "SHOP_PINYIN" + " TEXT,"
//                + "SELL_VALUE1" + " TEXT,"
//                + "SELL_VALUE2" + " TEXT,"
//                + "TYPE" + " INTEGER,"
//                + "SHOP_FROM" + " INTEGER,"
//                + "REGION" + " TEXT,"
//                + "REGION_PINYIN" + " TEXT,"
//                + "DATE_TYPE"+ " TEXT"
//                + ");");
//		
//		db.execSQL("CREATE TABLE " + "sum_table_date" + " ("
//                + "ID" + " LONG PRIMARY KEY,"
//                + "SUM_SELL_VALUE1" + " TEXT,"
//                + "SUM_SELL_VALUE2" + " TEXT,"
//                + "SUM_TIME" + " TEXT"
//                + ");");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("DROP TABLE IF EXISTS shop_table");
//		db.execSQL("DROP TABLE IF EXISTS sum_table");
//		db.execSQL("DROP TABLE IF EXISTS shop_table_date");
//		db.execSQL("DROP TABLE IF EXISTS sum_table_date");
        onCreate(db);
	}
}