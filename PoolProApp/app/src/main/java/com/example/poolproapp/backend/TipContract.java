package com.example.poolproapp.backend;


import android.provider.BaseColumns;

public class TipContract {

    private TipContract(){}

    public static class TipEntry implements BaseColumns{
        public static final String TABLE_NAME = "tips";
        public static final String COLUMN_NAME_MESSAGE = "_message";
    }
}
