package com.example.autoassistant.viewmodel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.autoassistant.model.MyDbHelper;
import com.example.autoassistant.model.User;

public class MyDbManager {

    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }

    public void openDb(){
        db = myDbHelper.getWritableDatabase();
    }

    public void insertToDb(String mass_of_car, String rad_of_wheel){
        ContentValues cv = new ContentValues();
        cv.put(User.MASS_OF_CAR, mass_of_car);
        cv.put(User.RAD_OF_WHEEL, rad_of_wheel);
        db.insert(User.TABLE_NAME, null, cv);
    }

    public void getFromDb () {
        Cursor cursor = db.query(User.TABLE_NAME, null, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            String mass_of_car = cursor.getString(cursor.getColumnIndex(User.MASS_OF_CAR));
            float m = Float.parseFloat(mass_of_car);
            String rad_of_wheel = cursor.getString(cursor.getColumnIndex(User.RAD_OF_WHEEL));
            float r = Float.parseFloat(rad_of_wheel);
        }

        cursor.close();
    }

    public void closeDb(){
        myDbHelper.close();
    }

}
