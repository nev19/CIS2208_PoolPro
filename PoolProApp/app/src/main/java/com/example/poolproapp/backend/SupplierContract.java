package com.example.poolproapp.backend;


import android.provider.BaseColumns;

public class SupplierContract {

    private SupplierContract(){}

    public static class SupplierEntry implements BaseColumns{
        public static final String TABLE_NAME = "suppliers";
        public static final String COLUMN_NAME_EMAIL = "_email";
    }
}
