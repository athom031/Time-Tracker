package com.example.timetracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TIME_TRACKER_TABLE = "TIME_TRACKER_TABLE";

    /*  DatabaseHelper constructor
     *  @param @Nullable Context context
     *
     *  required constructor because extension of SQLiteOpenHelper
     *  means there are variables needed to be passed into super function to create helper
     *  calls SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
     *  hard code values other than application context
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, "timeTracker.db", null, 1);

    }


    /*  creation of database
     *  @param SQLiteDatabase db
     *
     *  will be called the first time database is accessed
     *  if no database there to access, create the new database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TIME_TRACKER_TABLE + " (ID INTEGER PRIMARY )";

        db.execSQL(createTableStatement);
    }

    /*  update database design on change
     *  @param SQLiteDatabase db, int oldVersion, int newVersion
     *
     *  if the database version number changes
     *  this function will prevent the previous users app from breaking
     *  when the database design changes
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
