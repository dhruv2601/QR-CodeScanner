package internship.datapole.qrcode;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by dhruv on 23/5/17.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cardsBD";
    public static final String TABLE_CARDS = "cards";

    // card table names

    public static final String KEY_ID = "id";
    public static final String KEY_CARD_TYPE = "cardType";
    public static final String KEY_CARD_NUMBER = "cardNum";
    public static final String KEY_CARD_PUTIN = "cardPutIn"; // in which tab is that scanned QR is put in

    public static final String TAG = "DBHandle";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CARDS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_CARD_TYPE + " VARCHAR NOT NULL,"    // there is a potential error here
                + KEY_CARD_NUMBER + " VARCHAR NOT NULL,"
                + KEY_CARD_PUTIN + " INTEGER NOT NULL";    // send the tabNO here and then proceed further as per the sequence of tabs you have set earlier in the interface

        db.execSQL(CREATE_CARDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXITS " + TABLE_CARDS);
    }

    public void clearDataBase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARDS, null, null);
    }

    public void addScannedQR(int id, String type, String num, int putIN) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_CARD_TYPE, type);
        values.put(KEY_CARD_NUMBER, num);
        values.put(KEY_CARD_PUTIN, putIN);

        db.insert(TABLE_CARDS, null, values);
    }

//    public Pair<Integer,Pair<String, Integer>>[] queryDB()
//    {
//        String selectQuery = "SELECT "+KEY_ID+" FROM "+TABLE_CARDS;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cr = db.rawQuery(selectQuery, null);
//        Pair<Integer, Pair<String,Integer>> tableData[] = new Pair[]
//
//        if(cr.moveToFirst())
//        {
//            do{
//                int id = cr.getInt(0);
//
//            }
//        }
//    }

}
