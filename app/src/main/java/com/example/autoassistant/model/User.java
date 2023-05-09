package com.example.autoassistant.model;

public class User {

    public static final String TABLE_NAME = "my_table";
    public static final String _ID = "_id";
    public static final String MASS_OF_CAR = "mass_of_car";
    public static final String RAD_OF_WHEEL = "rad_of_wheel";
    public static final String DB_NAME = "my_db.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS" +
            TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," +
            MASS_OF_CAR + " TEXT," + RAD_OF_WHEEL + " TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME;






}
