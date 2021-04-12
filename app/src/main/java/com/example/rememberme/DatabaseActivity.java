package com.example.rememberme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseActivity {

    private DatabaseHelper dbHelper;

    private SQLiteDatabase database;

    public final static String REMEMBERME = "RememberNotes"; // name of table

    public final static String NOTE_ID = "id"; // id value for food item
    public final static String NOTES = "Notes"; // id value for food item
    public final static String CREATED_DATE = "created_date"; // name of food

    /**
     * @param context
     */
    public DatabaseActivity(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long insertNotes(String notes, String created_date) {
        ContentValues values = new ContentValues();
        values.put(NOTES, notes);
        values.put(CREATED_DATE, created_date);
        return database.insert(REMEMBERME, null, values);
    }

    public Cursor selectAllNotes() {
        String[] notes = new String[]{NOTE_ID,NOTES, CREATED_DATE};
        Cursor mCursor = database.query(true, REMEMBERME, notes, null, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int UpdateNotes(String id, String notes) {
        ContentValues values = new ContentValues();
        values.put(NOTES, notes);
        return database.update(REMEMBERME, values, NOTE_ID + "=?", new String[]{id});
    }

    public void deleteNotes(String id) {
        System.out.println("Note deleted with id: " + id);
        database.delete(REMEMBERME, NOTE_ID + "=?", new String[]{id});
    }
}
