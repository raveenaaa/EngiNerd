package com.example.raveena.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class SqliteHandler extends SQLiteOpenHelper {
    private static final String TAG = SqliteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myApp";
    private static final String TABLE_NAME = "college";
    private static final String COLLEGE_ID = "id";
    public static final String COLLEGE_NAME = "name";
    public static final String COLLEGE_LOC = "location";


    public SqliteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table_sql = "CREATE TABLE college (id INTEGER PRIMARY KEY" +
                ", name TEXT, location TEXT)";

        db.execSQL(table_sql);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }

    void addColleges(String name, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLLEGE_NAME, name);
        contentValues.put(COLLEGE_LOC, location);

        long id = db.insert(TABLE_NAME, null, contentValues);
        db.close(); // Closing database connection

        Log.d(TAG, "New college inserted into sqlite: " + id);
    }

    public ArrayList<String> getAllName() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> collegeNames = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();


            do {
                String name = cursor.getString(cursor.getColumnIndex(COLLEGE_NAME));

               collegeNames.add(name);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return collegeNames;

    }

    public Cursor getAllCollegesDescription(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selections = {String.valueOf(id)};
        String columns[] = {COLLEGE_NAME, COLLEGE_LOC};
        Cursor cursor = db.query(TABLE_NAME, columns, COLLEGE_ID + "=?",
                selections, null, null, null);
        //db.close();
        return cursor;
    }
}
