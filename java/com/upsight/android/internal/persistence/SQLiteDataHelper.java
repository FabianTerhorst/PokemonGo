package com.upsight.android.internal.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLiteDataHelper implements DataHelper {
    private static final String DATABASE_NAME = "upsight.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MODELS = "models";
    private SQLiteOpenHelper mOpenHelper;

    private static final class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, SQLiteDataHelper.DATABASE_NAME, null, SQLiteDataHelper.DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE models (_id TEXT PRIMARY KEY NOT NULL, type TEXT NOT NULL, data TEXT NOT NULL  );");
            db.execSQL("CREATE INDEX ID_INDEX ON models (_id);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS models");
            onCreate(db);
        }
    }

    SQLiteDataHelper(Context context) {
        this.mOpenHelper = new DatabaseHelper(context);
    }

    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return this.mOpenHelper.getReadableDatabase().query(TABLE_MODELS, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public long insert(ContentValues contentValues) {
        return this.mOpenHelper.getWritableDatabase().insert(TABLE_MODELS, null, contentValues);
    }

    public int delete(String selection, String[] selectionArgs) {
        return this.mOpenHelper.getWritableDatabase().delete(TABLE_MODELS, selection, selectionArgs);
    }

    public int update(ContentValues contentValues, String selection, String[] selectionArgs) {
        return this.mOpenHelper.getWritableDatabase().update(TABLE_MODELS, contentValues, selection, selectionArgs);
    }
}
