package com.example.timetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;


import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TIME_TRACKER_TABLE = "TIME_TRACKER_TABLE";
    public static final String COLUMN_TRACKED_YEAR = "YEAR";
    public static final String COLUMN_TRACKED_MONTH = "MONTH";
    public static final String COLUMN_TRACKED_DAY = "DAY";
    public static final String COLUMN_TRACKED_MINUTES = "MINUTES";

    /*  DataBaseHelper constructor
     *  @param @Nullable Context context
     *  @return VOID
     *
     *  required constructor because extension of SQLiteOpenHelper
     *  means there are variables needed to be passed into super function to create helper
     *  calls SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
     *  hard code values other than application context
     */
    public DataBaseHelper(@Nullable Context context) {
        super(context, "timeTracker.db", null, 1);

    }

    /*  creation of database
     *  @param SQLiteDatabase db
     *  @return VOID
     *
     *  will be called the first time database is accessed
     *  if no database there to access, create the new database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = String.format("CREATE TABLE %s (%s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, PRIMARY KEY(%s,%s,%s) )",
                TIME_TRACKER_TABLE, COLUMN_TRACKED_YEAR, COLUMN_TRACKED_MONTH, COLUMN_TRACKED_DAY,
                COLUMN_TRACKED_MINUTES, COLUMN_TRACKED_YEAR, COLUMN_TRACKED_MONTH, COLUMN_TRACKED_DAY);

        db.execSQL(createTableStatement);
    }

    /*  update database design on change
     *  @param SQLiteDatabase db, int oldVersion, int newVersion
     *  @return VOID
     *
     *  if the database version number changes
     *  this function will prevent the previous users app from breaking
     *  when the database design changes
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*  add timeTrackingModel object values to db
     *  @param TimeTrackingModel timeTrackingModel
     *  @return int (# of minutes worked on that date)
     *
     *  search db for date primary key and return minutes associated with row
     *  if no row found will return 0
     */
    public int clockedMinutesToday(TimeTrackingModel timeTrackingModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = String.format("SELECT %s FROM %s WHERE %s = %d AND %s = %d AND %s = %d",
                COLUMN_TRACKED_MINUTES, TIME_TRACKER_TABLE,
                COLUMN_TRACKED_YEAR, timeTrackingModel.getYear(),
                COLUMN_TRACKED_MONTH, timeTrackingModel.getMonth(),
                COLUMN_TRACKED_DAY, timeTrackingModel.getDay());

        Cursor cursor = db.rawQuery(queryString, null);

        int curMinutes = cursor.moveToFirst() ? cursor.getInt(0) + timeTrackingModel.getMinutes() : 0;

        cursor.close();
        db.close();

        return curMinutes;
    }

    /*  db insertion
     *  @param TimeTrackingModel timeTrackingModel
     *  @return boolean (if operation is successful)
     *
     *  add timeTrackingModel object values to db
     *  if the timeTrackingModel has a primary key that causes conflict
     *  update the minutes worked that day with the minutes instance
     */
    public boolean updateOnDuplicate(TimeTrackingModel timeTrackingModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES(%d, %d, %d, %d) ON CONFLICT(%s, %s, %s) DO UPDATE SET %s = %s + %d",
                TIME_TRACKER_TABLE,
                COLUMN_TRACKED_YEAR, COLUMN_TRACKED_MONTH,
                COLUMN_TRACKED_DAY, COLUMN_TRACKED_MINUTES,
                timeTrackingModel.getYear(), timeTrackingModel.getMonth(),
                timeTrackingModel.getDay(), timeTrackingModel.getMinutes(),
                COLUMN_TRACKED_YEAR, COLUMN_TRACKED_MONTH, COLUMN_TRACKED_DAY,
                COLUMN_TRACKED_MINUTES, COLUMN_TRACKED_MINUTES, timeTrackingModel.getMinutes());

        Cursor cursor = db.rawQuery(queryString, null);

        boolean flag = cursor.moveToFirst();

        cursor.close();
        db.close();

        return flag;
    }

}
