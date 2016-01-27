package org.seven.data;


public class DBSQL {

//	public ArrayList<Cell> getData(String selection){
//		//按照classID以及status从数据库中查找对应项,构建后显示
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getReadableDatabase();
//		Cursor c = sd.query("shop_table", null, selection, null, null, null, "ID");
//		ArrayList<Cell> cells = new ArrayList<Cell>();
//		while(c.moveToNext()){
//			long index = c.getLong(0);
//			String shop_id = c.getString(1);
//			String shop = c.getString(2);
//			String shop_pinyin = c.getString(3);
//			String sell_value1 = c.getString(4);
//			String sell_value2 = c.getString(5);
//			int type = c.getInt(6);
//			int shop_from = c.getInt(7);
//			String region = c.getString(8);
//			String region_pinyin = c.getString(9);
//			Cell cell = new Cell(shop_id,index,shop,shop_pinyin,sell_value1,sell_value2,type,shop_from,region,region_pinyin,null,null);
//			cells.add(cell);
//		}
//		c.close();
//		sd.close();
//		return cells;
//	}
//	
//	public ArrayList<Object[]> getDataByRegion(String calc, String selection){
//		//按照classID以及status从数据库中查找对应项,构建后显示
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getReadableDatabase();
//		ArrayList<Object[]> result = new ArrayList<Object[]>();
//		Cursor c = sd.query("shop_table", new String[]{"total("+calc+")","REGION"}, selection, null, "REGION", null, null);
//		while(c.moveToNext()){
//			double total = c.getDouble(0);
//			String region = c.getString(1);
//			result.add(new Object[]{total,region.replaceAll("华","").replaceAll("区", "")});
//		}
//		c.close();
//		sd.close();
//		return result;
//	}
//	
//	public ArrayList<Object[]> getDataByShop(String calc, String selection){
//		//按照classID以及status从数据库中查找对应项,构建后显示
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getReadableDatabase();
//		ArrayList<Object[]> result = new ArrayList<Object[]>();
//		Cursor c = sd.query("shop_table", new String[]{"total("+calc+")","SHOP"}, selection, null, "SHOP", null, null);
//		while(c.moveToNext()){
//			double total = c.getDouble(0);
//			String shop = c.getString(1);
//			result.add(new Object[]{total,shop});
//		}
//		c.close();
//		sd.close();
//		return result;
//	}
//	
//	public ArrayList<Cell> getDataByDate(String selection){
//		//按照classID以及status从数据库中查找对应项,构建后显示
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getReadableDatabase();
//		Cursor c = sd.query("shop_table_date", null, selection, null, null, null, "ID");
//		ArrayList<Cell> cells = new ArrayList<Cell>();
//		while(c.moveToNext()){
//			long index = c.getLong(0);
//			String shop_id = c.getString(1);
//			String date = c.getString(2);
//			String shop = c.getString(3);
//			String shop_pinyin = c.getString(4);
//			String sell_value1 = c.getString(5);
//			String sell_value2 = c.getString(6);
//			int type = c.getInt(7);
//			int shop_from = c.getInt(8);
//			String region = c.getString(9);
//			String region_pinyin = c.getString(10);
//			String date_type = c.getString(11);
//			Cell cell = new Cell(shop_id,index,shop,shop_pinyin,sell_value1,sell_value2,type,shop_from,region,region_pinyin,date,date_type);
//			cells.add(cell);
//		}
//		c.close();
//		sd.close();
//		return cells;
//	}
//	
//	
//	public void delData(){
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getReadableDatabase();
//		sd.delete("shop_table", null, null);
//		sd.close();
//	}
//	
//	public void createData(ContentValues cv){
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getWritableDatabase();
//		sd.insert("shop_table", null, cv);
//		sd.close();
//	}
//	
//	public int createSum(ContentValues cv){
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getWritableDatabase();
//		long id = sd.insert("sum_table", null, cv);
//		sd.close();
//		return (int)id;
//	}
//	
//	public void delSum(){
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getWritableDatabase();
//		sd.delete("sum_table", null, null);
//		sd.close();
//	}
//	
//	public ArrayList<Sum> getSum(){
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getWritableDatabase();
//		Cursor c = sd.query("sum_table", null, null, null, null, null, null);
//		ArrayList<Sum> sum = new ArrayList<Sum>();
//		while(c.moveToNext()){
//			long id = c.getLong(0);
//			String sell_value1 = c.getString(1);
//			String sell_value2 = c.getString(2);
//			String time = c.getString(3);
//			Sum s = new Sum(id,sell_value1,sell_value2,time);
//			sum.add(s);
//		}
//		c.close();
//		sd.close();
//		return sum;
//	}
//	
//	public void delDataByDate(){
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getReadableDatabase();
//		sd.delete("shop_table_date", null, null);
//		sd.close();
//	}
//	
//	public void createDataByDate(ContentValues cv){
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getWritableDatabase();
//		sd.insert("shop_table_date", null, cv);
//		sd.close();
//	}
//	
//	public int createSumByDate(ContentValues cv){
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getWritableDatabase();
//		long id = sd.insert("sum_table_date", null, cv);
//		sd.close();
//		return (int)id;
//	}
//	
//	public void delSumByDate(){
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getWritableDatabase();
//		sd.delete("sum_table_date", null, null);
//		sd.close();
//	}
//	
//	public ArrayList<Sum> getSumByDate(){
//		SQLiteDatabase sd = GlobalParam.getInstance().getDB().getWritableDatabase();
//		Cursor c = sd.query("sum_table_date", null, null, null, null, null, null);
//		ArrayList<Sum> sum = new ArrayList<Sum>();
//		while(c.moveToNext()){
//			long id = c.getLong(0);
//			String sell_value1 = c.getString(1);
//			String sell_value2 = c.getString(2);
//			String time = c.getString(3);
//			Sum s = new Sum(id,sell_value1,sell_value2,time);
//			sum.add(s);
//		}
//		c.close();
//		sd.close();
//		return sum;
//	}
}
