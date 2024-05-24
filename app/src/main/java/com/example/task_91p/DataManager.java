package com.example.task_91p;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataManager extends SQLiteOpenHelper {

//    table data and setup
    private static final String DEBUG_TAG = "DEBUG_LOGS";
    private static final String DATABASE_NAME = "lostfound.db";
    private static final String TABLE_NAME = "lostfound";
    private static final int DATABASE_VERSION = 5;
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LON = "lon";

//    constructor
    public DataManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    when table is created
@Override
public void onCreate(SQLiteDatabase db) {
    String query = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TYPE + " TEXT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_PHONE + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_LOCATION + " TEXT, " +
            COLUMN_LAT + " REAL, " +
            COLUMN_LON + " REAL)";
    db.execSQL(query);
}

//      when table is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DATABASE_VERSION) {
            // drop old table
            String dropOldTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(dropOldTableQuery);

            // create new table
            onCreate(db);
        }
    }

//    get all items as List
    @SuppressLint("Range")
    public List getAllItems() {
        Log.d(DEBUG_TAG, "Fetching all items");
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List items = new ArrayList();
        while (cursor.moveToNext()) {
            LostFoundItem item = new LostFoundItem();
            item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            item.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
            item.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            item.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            item.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            item.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
            item.setLat(cursor.getDouble(cursor.getColumnIndex(COLUMN_LAT)));
            item.setLon(cursor.getDouble(cursor.getColumnIndex(COLUMN_LON)));
            items.add(item);
        }
//        cursor.close();
        for (int i = 0; i < items.size(); i++) {
            LostFoundItem currentItem = (LostFoundItem) items.get(i);
            Log.d(DEBUG_TAG, "Item ID: " + currentItem.getId());
            Log.d(DEBUG_TAG, "Item name: " + currentItem.getName());
        }
        return items;
    }

//    add new LostFoundItem to db
public boolean addLostFoundItem(LostFoundItem item) {
        Log.d(DEBUG_TAG, "Inserting item with name: " + item.getName());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, item.getName());
        contentValues.put(COLUMN_TYPE, item.getType());
        contentValues.put(COLUMN_PHONE, item.getPhone());
        contentValues.put(COLUMN_DESCRIPTION, item.getDescription());
        contentValues.put(COLUMN_DATE, item.getDate());
        contentValues.put(COLUMN_LOCATION, item.getLocation());
        contentValues.put(COLUMN_LAT, item.getLat());
        contentValues.put(COLUMN_LON, item.getLon());
        long rowId = db.insert(TABLE_NAME, null, contentValues);
        return rowId != -1;
}

//    remove LostFoundItem from db
public boolean deleteItem(LostFoundItem item) {
    Log.d(DEBUG_TAG, "Deleting item with name: " + item.getName());
    SQLiteDatabase db = this.getWritableDatabase();
    int result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(item.getId())});
    if (result <= 0) {
        Log.e(DEBUG_TAG, "Failed to delete item, rows affected: " + result);
    } else {
        Log.d(DEBUG_TAG, "Successfully deleted item, rows affected: " + result);
    }
    return result > 0;
}





}
