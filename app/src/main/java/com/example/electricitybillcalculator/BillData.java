package com.example.electricitybillcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BillData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ElectrictyBillDatabase";
    private static final int DB_VERSION = 1;
    private static final String BILL_TABLE = "BillProfile";
    private static final String KEY_NAME = "ID_Name";
    private static final String KEY_ID = "ID_Number";
    private static final String DATE = "Date";
    private static final String KEY_PREVIOUS = "Previous_Reading";
    private static final String KEY_CURRENT = "Current_Reading";
    private static final String KEY_AMOUNT = "Bill_Amount";


    public BillData(Context context){
        super(context,DATABASE_NAME,null,DB_VERSION);
    } //Constructor

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE " + BILL_TABLE +
                "(" +KEY_NAME+ " TEXT ," +KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT ," +KEY_AMOUNT+" TEXT " + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {

    }

    public void createProfileTable(SQLiteDatabase DB , String name){

        DB.execSQL("CREATE TABLE " + name +
                      "(" +DATE+ " TEXT ," +KEY_PREVIOUS+ " TEXT UNIQUE ," +KEY_CURRENT+" TEXT UNIQUE ,"+KEY_AMOUNT+" TEXT " + ")" );


    }

    public void addProfileInBill(String name ,String amount ){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_AMOUNT, amount);
        DB.insert(BILL_TABLE,null ,values);
    }

    public String getNameFromTable(int id){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT " +KEY_NAME+" FROM " + BILL_TABLE +" WHERE " + KEY_ID + " = " + id , null);
        String previous = null;
        String name = null;
        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(0);
            cursor.close();

        }
        return name;

    }

    public String getPreviousReading(String name){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT " +KEY_CURRENT+" FROM " + name , null);
        String previous = "0";
        if (cursor.moveToLast()) {
            previous = cursor.getString(0);
            cursor.close();
        }
        return previous;
    }

    public void addNewBill(String name,String date, String previous, String current, String amount){
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(KEY_PREVIOUS, previous);
        values.put(KEY_CURRENT, current);
        values.put(KEY_AMOUNT, amount);

        DB.insert( name ,null ,values);
    }

    public void updateAmount(String name, String amountText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, amountText);
        // Update the rows where the name matches
        db.update(BILL_TABLE, values, KEY_NAME+" = ?", new String[]{name});

    }

    public Cursor readDataFromBillTable(){
        String quary = "SELECT * FROM " + BILL_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(quary , null);
        }
        return cursor;
    } // for show last bills

    public Cursor readDataFromProfileTable(String name){
        String quary = "SELECT * FROM " + name;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(quary , null);
        }
        return cursor;
    } // for show bills history by profile

    public Cursor readNameFromBillTable(){
        String quary = "SELECT "+KEY_NAME+" FROM " + BILL_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(quary , null);
        }
        return cursor;
    }

    public Cursor readAmountFromBillTable(){
        String quary = "SELECT "+KEY_AMOUNT+" FROM " + BILL_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(quary , null);
        }
        return cursor;
    }

    long deleteProfile(String name){

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ name);

        long result = db.delete(BILL_TABLE, KEY_NAME +"=?" , new String[]{name});

        return result;
    }



}

