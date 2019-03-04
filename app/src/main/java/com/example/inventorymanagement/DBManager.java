package com.example.inventorymanagement;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBManager extends SQLiteOpenHelper {

    static final String dbName = "product_db";

    // PRODUCT TABLE
    static final String product_table = "product";
    static final String card_col = "card_no";
    static final String model_col = "model_no"; // primary key
    static final String price_buy_col = "buy_price";
    static final String price_sell_col = "sell_price";
    static final String description_col = "description";
    static final String place_col = "place";
    static final String product_name_col = "product_name";
    static final String quantity_col = "quantity";

    // LOG TABLE
    static final String product_log_table = "product_log";
    static final String product_id_col = "product_id";
    static final String customer_col = "customer";
    static final String quantity_change_col = "quantity";
    static final String sell_or_buy_col = "SoB";
    static final String created_at_col = "created_at";

    public DBManager(Context context){
        super(context, dbName , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // PRODUCT TABLE
        db.execSQL("CREATE TABLE " +product_table+ "(" +model_col+ " VARCHAR(255), " +
                card_col+ " VARCHAR(255) NOT NULL PRIMARY KEY, " +price_buy_col+ " DECIMAL(10,5) NOT NULL, " +
                price_sell_col+ " DECIMAL(10,5) NOT NULL, " +description_col+ " VARCHAR(255), " +
                place_col+ " VARCHAR(255) NOT NULL, " +product_name_col+ " VARCHAR(255) NOT NULL, " +
                quantity_col+ " INTEGER DEFAULT 0);");

        // LOG TABLE
        db.execSQL("CREATE TABLE " +product_log_table+ "(" +product_id_col+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                customer_col+ " VARCHAR(255), " +created_at_col+ " DATE DEFAULT CURRENT_DATE, " +
                quantity_change_col+ " INTEGER NOT NULL, "+
                sell_or_buy_col+ " VARCHAR(1) NOT NULL, " +card_col+ " VARCHAR(255) NOT NULL, "+
                "FOREIGN KEY (" +card_col+") REFERENCES "+product_table+" ("+card_col+"));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+product_log_table);
        db.execSQL("DROP TABLE IF EXISTS "+product_table);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            //Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // Register a new product to inventory
    public int addNewProduct(String model_no, String card_no, Double p_buy, Double p_sell, String place, String desc, String p_name, int qnt){

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("INSERT INTO " +product_table+ "(" +model_col+ "," +card_col+ "," +price_buy_col+ "," +
                    price_sell_col+ "," +place_col+ "," +description_col+ "," +product_name_col+ "," +quantity_col+ ") VALUES("+"'" +model_no+ "','" +card_no+ "'," +p_buy+ "," +
                    p_sell+ ",'" +place+ "','" +desc+ "','" +p_name+ "'," +qnt+ ");");
            db.close();
            return 1;    // success
        }catch (SQLiteException e){
            e.printStackTrace();
            return 0;   // fail
        }
    }

    // Update a single Product in inventory
    public int updateProduct(String model_no, String card_no, Double p_buy, Double p_sell, String place, String desc, String p_name, int q) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE " +product_table+ " SET "+model_col+ "='" +model_no+ "', " +quantity_col+ "=" +q+ ", " +price_sell_col+ "=" +p_sell+ ", " +
                    price_buy_col+ "=" +p_buy+ ", " +description_col+ "='" +desc+ "', " +place_col+ "='" +place+ "', " +product_name_col+ "='" +p_name+ "' WHERE " +
                    card_col+ "='" +card_no+ "';");
            db.close();
            return 1; // success
        }catch (SQLiteException e){
            e.printStackTrace();
            return 0; // fail
        }
    }

    // Delete product from inventory
    public int deleteProduct(String card_no){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " +product_log_table+ " WHERE " +card_col+ "='" +card_no+ "';");    // delete from log table
            db.execSQL("DELETE FROM " +product_table+ " WHERE " +card_col+ "='" +card_no+ "'");   // delete from product table
            db.close();
            return 1; // success
        }catch (SQLiteException e){
            e.printStackTrace();
            return 0; // fail
        }
    }

    // Sell or Buy Product
    public int sellorbuyProduct(String card_no, String customer, String sob, int quantity){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " +product_table+ " WHERE " +card_col+ "='" +card_no+"';";
            Cursor c = db.rawQuery(selectQuery,null);
            int n = 0 ;
            if(c.getCount() > 0){
                c.moveToFirst();
                n = c.getInt(7);
                c.close();
            }

            if(sob.equals("s")){
                if(n < quantity){
                    db.close();
                    return 0; // fail getColumnIndex
                }
                n = n-quantity;
                db.execSQL("UPDATE " +product_table+ " SET " +quantity_col+ "=" +n+ " WHERE " +card_col+ "='" +card_no+ "';");
                db.execSQL("INSERT INTO " +product_log_table+ "(" +customer_col+ ", " +card_col+ "," +
                        quantity_change_col+ ", " +sell_or_buy_col+ ") VALUES('" +customer+ "','" +card_no+ "'," +quantity+ ",'s')");

            }
            else if(sob.equals("b")){
                n = n+quantity;
                db.execSQL("UPDATE " +product_table+ " SET " +quantity_col+ "=" +n+ " WHERE " +card_col+ "='" +card_no+ "';");
                db.execSQL("INSERT INTO " +product_log_table+ "(" +customer_col+ ", " +card_col+ "," +
                        quantity_change_col+ ", " +sell_or_buy_col+ ") VALUES('" +customer+ "','" +card_no+ "'," +quantity+ ",'b')");
            }
            db.close();
            return 1; // success
        }catch (SQLiteException e){
            e.printStackTrace();
            return 0; // fail
        }
    }

    // Get all details about a single product
    public void getProduct(Product p, String c_no){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " +product_table+ " WHERE " +card_col+ "='" +c_no+"';";   // change from product_name to card_no

        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();

            p.setProduct_name(cursor.getString(6));
            p.setModel_no(cursor.getString(0));
            p.setCard_no(cursor.getString(1));
            p.setPrice_buy(cursor.getDouble(2));
            p.setPrice_sell(cursor.getDouble(3));
            p.setDescription(cursor.getString(4));
            p.setPlace(cursor.getString(5));
            p.setQuantitiy(cursor.getInt(7));

            cursor.close();
        }

        db.close();
    }

    // Get log cursor
    public Cursor getLogCursor(String card_no){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " +product_log_table+ " WHERE " +card_col+ "='" +card_no+ "';";
        Cursor c = db.rawQuery(selectQuery,null);
        return c;
    }

}
