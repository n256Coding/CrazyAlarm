package com.n256coding.crazyalarm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.n256coding.crazyalarm.helper.DateEx;
import com.n256coding.crazyalarm.model.Alarm;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AlarmDB.db";
    private static final String TABLE_NAME = "Alarm";
    private static final String COL_1 = "alarm_id";
    private static final String COL_2 = "alarm_time";
    private static final String COL_3 = "alarm_status";
    private static final String COL_4 = "alarm_sound";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+TABLE_NAME+" (" +
            COL_1 +" INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_2 +" DATETIME," +
            COL_3 +" TEXT," +
            COL_4 +" TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE "+TABLE_NAME;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long insert(Alarm alarm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, DateEx.getDateTimeString(alarm.getAlarmTime()));
        values.put(COL_3, alarm.getAlarmStatus());
        values.put(COL_4, alarm.getAlarmSound());

        return db.insert(TABLE_NAME, null, values);
    }

    public boolean update(Alarm alarm, int oldAlarmId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, DateEx.getDateTimeString(alarm.getAlarmTime()));
        values.put(COL_3, alarm.getAlarmStatus());
        values.put(COL_4, alarm.getAlarmSound());

        return db.update(TABLE_NAME, values, COL_1+" = ?",
                new String[]{String.valueOf(oldAlarmId)}) != 0;
    }

    public boolean updateAlarmStatusById(String status, int alarmId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(1);
        values.put(COL_3, status);
        return db.update(TABLE_NAME, values, COL_1+" = ?", new String[]{String.valueOf(alarmId)}) != 0;
    }

    public boolean delete(int alarmId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "alarm_id = ?", new String[]{String.valueOf(alarmId)}) != 0;
    }

    public Cursor getAllAlarms(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, new String[]{COL_1, COL_2, COL_3, COL_4},
                null, null, null, null, null);
    }

    public Cursor getAlarmById(int alarmId){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, new String[]{COL_1, COL_2, COL_3, COL_4}, COL_1+"=?",
                new String[]{String.valueOf(alarmId)}, null, null, null);
    }

    public Cursor getAlarmStatusById(int alarmId){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, new String[]{COL_3}, COL_1+"=?",
                new String[]{String.valueOf(alarmId)}, null, null, null);
    }
}
