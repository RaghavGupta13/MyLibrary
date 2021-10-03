package com.corsair.mylibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "bookLibrary.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "library";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_PUBLISHER = "publisher";
    private static final String COLUMN_PAGES = "pages";
    private static final String COLUMN_PUBLISHED_YEAR = "published_year";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_SUMMARY = "summary";
    private static final String COLUMN_IMAGE = "image";
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_AUTHOR + " TEXT, " +
                        COLUMN_PUBLISHER + " INTEGER, " +
                        COLUMN_PAGES + " INTEGER, " +
                        COLUMN_PUBLISHED_YEAR  + " INTEGER, " +
                        COLUMN_CATEGORY + " TEXT, " +
                        COLUMN_SUMMARY + " TEXT, " +
                        COLUMN_IMAGE + " BLOB);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addData(LibraryModel libraryModel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, libraryModel.getBook_title());
        cv.put(COLUMN_AUTHOR, libraryModel.getBook_author());
        cv.put(COLUMN_PUBLISHER, libraryModel.getBook_publisher());
        cv.put(COLUMN_PAGES, libraryModel.getBook_pages());
        cv.put(COLUMN_PUBLISHED_YEAR, libraryModel.getBook_published_year());
        cv.put(COLUMN_CATEGORY, libraryModel.getBook_category());
        cv.put(COLUMN_SUMMARY, libraryModel.getBook_summary());
        cv.put(COLUMN_IMAGE, libraryModel.getBook_image());

        long insert = db.insert(TABLE_NAME, null, cv);
        db.close();

    }

    public Cursor readData(){

        SQLiteDatabase db = this.getReadableDatabase();
        String readQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(readQuery, null);
        }

        return cursor;
    }

    public void updateData(String row_id, LibraryModel libraryModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, libraryModel.getBook_title());
        cv.put(COLUMN_AUTHOR, libraryModel.getBook_author());
        cv.put(COLUMN_PUBLISHER, libraryModel.getBook_publisher());
        cv.put(COLUMN_PAGES, libraryModel.getBook_pages());
        cv.put(COLUMN_PUBLISHED_YEAR, libraryModel.getBook_published_year());
        cv.put(COLUMN_CATEGORY, libraryModel.getBook_category());
        cv.put(COLUMN_SUMMARY, libraryModel.getBook_summary());
        cv.put(COLUMN_IMAGE, libraryModel.getBook_image());

        long result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[] {String.valueOf(row_id)});

    }
    public void deleteData(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[] {row_id});

        db.close();
    }
}
