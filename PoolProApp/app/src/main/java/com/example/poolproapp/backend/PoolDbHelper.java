package com.example.poolproapp.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.poolproapp.Pool;

import java.util.ArrayList;

public class PoolDbHelper extends SQLiteOpenHelper {



    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "poolpro.db";
    private final String _name = PoolContract.PoolEntry.COLUMN_NAME_NAME;
    private final String _owner = PoolContract.PoolEntry.COLUMN_NAME_OWNER;
    private final String _phone = PoolContract.PoolEntry.COLUMN_NAME_PHONE;
    private final String _capacity = PoolContract.PoolEntry.COLUMN_NAME_CAPACITY;


    public PoolDbHelper(Context context) {
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
        return "CREATE TABLE " + PoolContract.PoolEntry.TABLE_NAME + " ("
                + PoolContract.PoolEntry._ID + " INTEGER PRIMARY KEY, " +
                _name + " varchar, " + _owner + " varchar, "+ _phone + " varchar, "+ _capacity + " varchar)";
    }
    private String dropTables() {
        return "DROP TABLE IF EXISTS " + PoolContract.PoolEntry.TABLE_NAME;
    }
    public long insertPool(Pool pool) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_name, pool.getName());
        values.put(_owner, pool.getOwner());
        values.put(_phone, pool.getPhone());
        values.put(_capacity, pool.getCapacity());
        long id = db.insert(PoolContract.PoolEntry.TABLE_NAME, null, values);
        return id;
    }
    public ArrayList<Pool> getPools() {
        ArrayList<Pool> pools = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
            BaseColumns._ID, _name, _owner, _phone, _capacity
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = BaseColumns._ID + " ASC";
        Cursor cursor = db.query(
            PoolContract.PoolEntry.TABLE_NAME, // The table to query
            projection, // The array of columns to return (pass null to get all)
            null, // The columns for the WHERE clause
            null, // The values for the WHERE clause
            null, // don't group the rows
            null, // don't filter by row groups
            sortOrder // The sort order
        );
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(_name));
            String owner = cursor.getString(cursor.getColumnIndexOrThrow(_owner));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(_phone));
            double capacity = cursor.getDouble(cursor.getColumnIndexOrThrow(_capacity));
            Pool pool = new Pool(id, name, owner, phone, capacity);
            pools.add(pool);
        }
        cursor.close();
        return pools;
    }




    public Pool getPoolById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID, _name, _owner, _phone, _capacity
        };
        // Filter results WHERE "id" = condition
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { Long.toString(id) };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = BaseColumns._ID + " ASC";
        Cursor cursor = db.query(
                PoolContract.PoolEntry.TABLE_NAME, // The table to query
                projection, // The array of columns to return (pass null to get all)
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );
        Pool pool = null;
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(_name));
            String owner = cursor.getString(cursor.getColumnIndexOrThrow(_owner));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(_phone));
            double capacity = cursor.getDouble(cursor.getColumnIndexOrThrow(_capacity));
            pool = new Pool(id, name, owner, phone, capacity);
        }
        cursor.close();
        return pool;
    }


    public void deletePool(int poolId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PoolContract.PoolEntry.TABLE_NAME, BaseColumns._ID + " = ?", new String[]{String.valueOf(poolId)});
        db.close();
    }
}