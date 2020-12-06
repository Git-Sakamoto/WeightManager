package com.example.weightmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

public class DBAdapter {
    private static final String DB_NAME = "weightmanager.db";
    private static final String DB_TABLE_USER = "user";//ユーザーテーブル
    private static final String DB_TABLE_HISTORY = "history";//履歴テーブル
    private static final int DB_VERSION = 1;

    public final static String COL_ID = "_id";//ID
    public final static String COL_NAME = "name";//ユーザー名
    public final static String COL_HEIGHT = "height";//身長
    public final static String COL_WEIGHT = "weight";//体重
    public final static String COL_TARGET_WEIGHT = "target_weight";//目標体重
    public final static String COL_DATE = "date";//日付
    public final static String COL_TIME = "time";//時間

    private SQLiteDatabase db = null;
    private DatabaseHelper dbHelper;
    protected Context context;

    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    public DBAdapter openDB() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void closeDB() {
        db.close();
        db = null;
    }

    public Cursor selectUser(){
        return db.query(DB_TABLE_USER,null,null,null,null,null,null);
    }

    public void insertUser(String name,String height,String weight,String targetWeight){
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_NAME, name);
            values.put(COL_HEIGHT, height);
            values.put(COL_WEIGHT, weight);
            values.put(COL_TARGET_WEIGHT, targetWeight);
            db.insert(DB_TABLE_USER, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void updateProfile(String id,String name,String height,String weight,String targetWeight){
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_NAME, name);
            values.put(COL_HEIGHT, height);
            values.put(COL_WEIGHT, weight);
            values.put(COL_TARGET_WEIGHT, targetWeight);
            db.update(DB_TABLE_USER, values, COL_ID + " = ?", new String[]{id});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void updateProfileWeight(String id,String weight){
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_WEIGHT, weight);
            db.update(DB_TABLE_USER, values, COL_ID + " = ?", new String[]{id});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor selectHistory(String date){
        return db.query(DB_TABLE_HISTORY,null,COL_DATE + " = ?",new String[]{date},null,null,null);
    }

    public Cursor selectAllHistory(){
        return db.query(DB_TABLE_HISTORY,null,null,null,null,null,COL_DATE + " DESC");
    }

    public SimpleCursorAdapter getHistoryList(Context context){
        Cursor c = selectAllHistory();
        String[] from = {COL_WEIGHT,COL_DATE,COL_TIME};
        int[] to = {R.id.text_weight, R.id.text_date,R.id.text_time};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, R.layout.layout_list_history, c, from, to, 0);
        return  adapter;
    }

    public void insertHistory(String id,String weight,String date,String time){
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_ID,id);
            values.put(COL_WEIGHT, weight);
            values.put(COL_DATE, date);
            values.put(COL_TIME, time);
            db.insert(DB_TABLE_HISTORY, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void updateHistory(String weight,String date){
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_WEIGHT, weight);
            db.update(DB_TABLE_HISTORY, values, COL_DATE + " = ?", new String[]{date});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            final String CREATE_TABLE_USER =
                    "create table " + DB_TABLE_USER + "("
                            + COL_ID + " integer primary key,"
                            + COL_NAME +" text,"
                            + COL_HEIGHT +" text,"
                            + COL_WEIGHT +" text,"
                            + COL_TARGET_WEIGHT +" text);";

            final String CREATE_TABLE_HISTORY =
                    "create table " + DB_TABLE_HISTORY + "("
                            + COL_ID + " integer,"
                            + COL_WEIGHT +" text,"
                            + COL_DATE +" text,"
                            + COL_TIME +" text);";

            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_HISTORY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_USER);
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_HISTORY);
            onCreate(db);
        }
    }
}
