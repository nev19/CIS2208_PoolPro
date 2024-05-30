package com.example.poolproapp.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.poolproapp.Tip;

import java.util.ArrayList;

public class TipDbHelper extends SQLiteOpenHelper {



    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "poolprotips.db";
    private final String _message = TipContract.TipEntry.COLUMN_NAME_MESSAGE;

    public TipDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTables());
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTables());
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    private String createTables() {
        return "CREATE TABLE " + TipContract.TipEntry.TABLE_NAME + " ("
                + TipContract.TipEntry._ID + " INTEGER PRIMARY KEY, " + _message + " varchar)";
    }
    private String dropTables() {
        return "DROP TABLE IF EXISTS " + TipContract.TipEntry.TABLE_NAME;
    }
    public long insertTip(Tip tip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_message, tip.getMessage());
        long id = db.insert(TipContract.TipEntry.TABLE_NAME, null, values);
        return id;
    }
    public ArrayList<Tip> getTips() {
        ArrayList<Tip> tips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
            BaseColumns._ID, _message
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = BaseColumns._ID + " ASC";
        Cursor cursor = db.query(
            TipContract.TipEntry.TABLE_NAME, // The table to query
            projection, // The array of columns to return (pass null to get all)
            null, // The columns for the WHERE clause
            null, // The values for the WHERE clause
            null, // don't group the rows
            null, // don't filter by row groups
            sortOrder // The sort order
        );
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String message = cursor.getString(cursor.getColumnIndexOrThrow(_message));
            Tip tip = new Tip(id, message);
            tips.add(tip);
        }
        cursor.close();
        return tips;
    }


    public int getTotalTipsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TipContract.TipEntry.TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        Log.d("XXXXXX",count+" tips");
        return count;
    }


    public Tip getTipById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID, _message
        };
        // Filter results WHERE "id" = condition
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { Long.toString(id) };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = BaseColumns._ID + " ASC";
        Cursor cursor = db.query(
                TipContract.TipEntry.TABLE_NAME, // The table to query
                projection, // The array of columns to return (pass null to get all)
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );
        Tip tip = null;
        while(cursor.moveToNext()) {
            String message = cursor.getString(cursor.getColumnIndexOrThrow(_message));
            tip = new Tip(id, message);
        }
        cursor.close();
        return tip;
    }


    public void deleteTip(int tipId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TipContract.TipEntry.TABLE_NAME, BaseColumns._ID + " = ?", new String[]{String.valueOf(tipId)});
        db.close();
    }
}