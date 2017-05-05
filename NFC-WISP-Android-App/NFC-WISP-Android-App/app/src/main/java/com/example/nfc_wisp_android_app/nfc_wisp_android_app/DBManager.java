package com.example.nfc_wisp_android_app.nfc_wisp_android_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class DBManager extends SQLiteOpenHelper {

    public static final String DB_NAME = "USER_MEASUREMENT_DB";
    public static final String PROFILE_TABLE_NAME = "PROFILE";
    public static final String MEASUREMENT_TABLE_NAME = "MEASUREMENT";

    public DBManager(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + PROFILE_TABLE_NAME + "(" +
                "uid varchar(20), " +
                "password varchar(20), " +
                "first_name varchar(100), " +
                "last_name varchar(100), " +
                "gender varchar(10), " +
                "age int, " +
                "weight int, " +
                "primary key(uid)" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + MEASUREMENT_TABLE_NAME + "(" +
                "uid varchar(20), " +
                "m_ir blob, " +
                "m_rd blob, " +
                "time varchar(50), " +
                "foreign key (uid) references PROFILE(uid), " +
                "primary key(time)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MEASUREMENT_TABLE_NAME);
        onCreate(db);
    }


    public void onInsertMeasurement(String uid, byte[] m_ir, byte[] m_rd, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO " + MEASUREMENT_TABLE_NAME + " (uid, m_ir, m_rd, time) " +
                "VALUES (?, ?, ?, ?)";
        SQLiteStatement insert = db.compileStatement(sql);
        insert.bindString(1, uid);
        insert.bindBlob(2, m_ir);
        insert.bindBlob(3, m_rd);
        insert.bindString(4, time);
        insert.executeInsert();
    }

    public void onSignUp(String uid,
                         String password,
                         String fname,
                         String lname,
                         String userGender,
                         int age,
                         int weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO " + PROFILE_TABLE_NAME + " (uid, password, first_name, last_name, gender, age ,weight) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement insert = db.compileStatement(sql);
        insert.clearBindings();
        insert.bindString(1, uid);
        insert.bindString(2, password);
        insert.bindString(3, fname);
        insert.bindString(4, lname);
        insert.bindString(5, userGender);
        insert.bindLong(6, age);
        insert.bindLong(7, weight);
        insert.executeInsert();
    }

    public Cursor getUserProfile(String uid, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor uinfo = db.rawQuery("SELECT p.first_name, p.last_name, p.gender, p.age, p.weight " +
                "FROM " + PROFILE_TABLE_NAME + " p " +
                "WHERE p.uid='" + uid + "' AND p.password='" + password + "'", null);


        return uinfo;
    }

    public Cursor getSingleMeasurementHistory(String uid, String time) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor singleMeasurement = db.rawQuery("SELECT m_ir, m_rd FROM " +
                MEASUREMENT_TABLE_NAME + " " +
                "WHERE uid = '" + uid + "' AND time = '" + time + "'", null);
        return singleMeasurement;
    }


    public Cursor getUserMeasurementHistory(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor measurement = db.rawQuery("SELECT time AS _id " +
                "FROM " + MEASUREMENT_TABLE_NAME + " " +
                "WHERE uid = '" + uid + "'", null);
        return measurement;
    }

    public boolean checkUserName(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor check = db.rawQuery("SELECT * FROM " + PROFILE_TABLE_NAME + " WHERE uid = '" + uid + "'", null);
        return check.moveToFirst();
    }
}
