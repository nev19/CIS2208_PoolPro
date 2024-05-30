package com.example.poolproapp.backend;


import android.provider.BaseColumns;

public class PoolContract {

    private PoolContract(){}

    public static class PoolEntry implements BaseColumns{
        public static final String TABLE_NAME = "pools";
        public static final String COLUMN_NAME_NAME = "_name";
        public static final String COLUMN_NAME_CAPACITY = "_capacity";
        public static final String COLUMN_NAME_OWNER = "_owner";
        public static final String COLUMN_NAME_PHONE = "_phone";

    }
}
