package com.example.poolproapp.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


import com.example.poolproapp.Supplier;

import java.util.ArrayList;

public class SupplierDbHelper extends SQLiteOpenHelper {



    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "poolprosuppliers.db";
    private final String _email = SupplierContract.SupplierEntry.COLUMN_NAME_EMAIL;

    public SupplierDbHelper(Context context) {
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
        return "CREATE TABLE " + SupplierContract.SupplierEntry.TABLE_NAME + " ("
                + SupplierContract.SupplierEntry._ID + " INTEGER PRIMARY KEY, " + _email + " varchar)";
    }
    private String dropTables() {
        return "DROP TABLE IF EXISTS " + SupplierContract.SupplierEntry.TABLE_NAME;
    }
    public long insertSupplier(Supplier supplier) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_email, supplier.getEmail());
        long id = db.insert(SupplierContract.SupplierEntry.TABLE_NAME, null, values);
        return id;
    }
    public ArrayList<Supplier> getSupplier() {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
            BaseColumns._ID, _email
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = BaseColumns._ID + " ASC";
        Cursor cursor = db.query(
                SupplierContract.SupplierEntry.TABLE_NAME, // The table to query
            projection, // The array of columns to return (pass null to get all)
            null, // The columns for the WHERE clause
            null, // The values for the WHERE clause
            null, // don't group the rows
            null, // don't filter by row groups
            sortOrder // The sort order
        );
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(_email));
            Supplier supplier = new Supplier(id, email);
            suppliers.add(supplier);
        }
        cursor.close();
        return suppliers;
    }




    public Supplier getSupplierById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID, _email
        };
        // Filter results WHERE "id" = condition
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { Long.toString(id) };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = BaseColumns._ID + " ASC";
        Cursor cursor = db.query(
                SupplierContract.SupplierEntry.TABLE_NAME, // The table to query
                projection, // The array of columns to return (pass null to get all)
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );
        Supplier supplier = null;
        while(cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow(_email));
            supplier = new Supplier(id, email);
        }
        cursor.close();
        return supplier;
    }

    // Method to fetch all supplier emails from the database
    public ArrayList<String> getSupplierEmails() {
        ArrayList<String> supplierEmails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {_email}; // Select only the email column
        Cursor cursor = db.query(
                SupplierContract.SupplierEntry.TABLE_NAME, // The table to query
                projection, // The array of columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        // Iterate through the cursor and add emails to the list
        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow(_email));
            supplierEmails.add(email);
        }

        cursor.close();
        return supplierEmails;
    }


    public void deleteSupplier(int supplierId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SupplierContract.SupplierEntry.TABLE_NAME, BaseColumns._ID + " = ?", new String[]{String.valueOf(supplierId)});
        db.close();
    }

    public void deleteAllSuppliers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SupplierContract.SupplierEntry.TABLE_NAME, null, null);
        db.close();
    }
}