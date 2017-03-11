package edu.csulb.android.photonotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hemant on 3/10/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    //Databse Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PhotoNotesDatabase";

    // Labels table name
    private static final String TABLE_LABELS = "labels";

    // Labels Table Columns names
    private static final String KEY_CAPTION = "caption";
    private static final String KEY_PATH = "path";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                TABLE_LABELS + "("
                + KEY_CAPTION + " PHOTO CAPTION," +
                KEY_PATH + " PHOTO FILE PATH)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);
        // Create tables again
        onCreate(db);
    }

    /**
     * Inserting a new photo into labels table
     */
    public void insertPhoto(String photoCaption, String photoFilePath) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAPTION, photoCaption);
        values.put(KEY_PATH, photoFilePath);

        db.insert(TABLE_LABELS, null, values);
        db.close();
    }

    public List<String> getAllPhotoCaptions() {
        List<String> captions = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_LABELS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to the list
        if (cursor.moveToFirst()) {
            do {
                captions.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // Closing connection
        cursor.close();
        db.close();

        // Returning photo caption
        return captions;
    }

    public String getPhotoFilePath(String caption) {

        String photoFilePath;

        // Select Query to get the file path for the caption
        String selectQuety = "SELECT * FROM " + TABLE_LABELS + "WHERE" + KEY_CAPTION
                + " = " + "'" + caption + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuety,null);

        photoFilePath = cursor.getString(1);

        // Closing connection
        cursor.close();
        db.close();

        // Returning photo file path
        return photoFilePath;
    }

}
