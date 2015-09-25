package com.yanushka.tipcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "products.db";

    public static final String TABLE_TIPS = "tips";
    public static final String COLUMN_ID = "_id";
    public static final int COLUMN_ID_NUM = 0;
    public static final String COLUMN_BILL_DATE = "bill_date";
    public static final int COLUMN_BILL_DATE_NUM = 1;
    public static final String COLUMN_BILL_AMOUNT = "bill_amount";
    public static final int COLUMN_BILL_AMOUNT_NUM = 2;
    public static final String COLUMN_TIP_PERCENT = "tip_percent";
    public static final int COLUMN_TIP_PERCENT_NUM = 3;

    public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_TIPS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BILL_DATE + " INTEGER, " + COLUMN_BILL_AMOUNT + " REAL, " + COLUMN_TIP_PERCENT + " REAL " + ");";
        db.execSQL(query);
        // first test data row to add
        String testValues1 = "INSERT INTO " + TABLE_TIPS + " (" + COLUMN_BILL_DATE + ", " + COLUMN_BILL_AMOUNT +
                ", " + COLUMN_TIP_PERCENT + ") VALUES (" + System.currentTimeMillis() + ", 26.43, .15)";
        db.execSQL(testValues1);
        // second test data row to add
        String testValues2 = "INSERT INTO " + TABLE_TIPS + " (" + COLUMN_BILL_DATE + ", " + COLUMN_BILL_AMOUNT +
                ", " + COLUMN_TIP_PERCENT + ") VALUES (" + System.currentTimeMillis() + ", 47.89, .20)";
        db.execSQL(testValues2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPS);
        onCreate(db);
    }

    public ArrayList<Tip> getTips(){
        ArrayList<Tip> tips = new ArrayList<Tip>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIPS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            Tip tip = new Tip();
            tip.setId(c.getInt(COLUMN_ID_NUM));
            tip.setDateMillis(c.getLong(COLUMN_BILL_DATE_NUM));
            tip.setBillAmount(c.getFloat(COLUMN_BILL_AMOUNT_NUM));
            tip.setTipPercent(c.getFloat(COLUMN_TIP_PERCENT_NUM));
            tips.add(tip);
            c.moveToNext();
        }
        db.close();
        return tips;
    }

    public void addTip(Tip tip){
        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_AMOUNT, tip.getBillAmount());
        values.put(COLUMN_BILL_DATE, tip.getDateMillis());
        values.put(COLUMN_TIP_PERCENT, tip.getTipPercent());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TIPS, null, values);
        db.close();
    }

    public Tip lastTip(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIPS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToLast();
        Tip tip = new Tip();
        tip.setId(c.getInt(COLUMN_ID_NUM));
        tip.setDateMillis(c.getLong(COLUMN_BILL_DATE_NUM));
        tip.setBillAmount(c.getFloat(COLUMN_BILL_AMOUNT_NUM));
        tip.setTipPercent(c.getFloat(COLUMN_TIP_PERCENT_NUM));
        db.close();
        return tip;
    }

    public float averageFloat(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIPS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        float average = 0;
        int count = 0;
        while(!c.isAfterLast()){
            count += 1;
            average += c.getFloat(COLUMN_TIP_PERCENT_NUM);
            c.moveToNext();
        }
        average = average / count;
        return average;
    }
}
