package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static SQLiteDatabase database;
    protected static final String ACTIVITY_NAME = "ChatDatabaseHelper";
    public String[] columns = {"KEY_ID", "KEY_MESSAGE"};
    public static final String TABLE_NAME = "dataTable";
    public static final String DATABASE_NAME = "Messages.db";
    public static final int VERSION_NUM = 8;
    public static final String KEY_ID = "ID";
    public static final String KEY_MESSAGE = "Message";

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ACTIVITY_NAME, "Calling onCreate");
        String DATABASE_CREATE = "create table " + TABLE_NAME + "(" + KEY_ID
                + " integer primary key autoincrement, " + KEY_MESSAGE
                + " text not null);";
        this.database = db;
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);

    }

}
