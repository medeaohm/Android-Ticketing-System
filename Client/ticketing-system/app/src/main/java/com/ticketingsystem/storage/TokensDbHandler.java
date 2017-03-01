package com.ticketingsystem.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TokensDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tokens.db";
    private static final String TABLE_TOKENS = "tokens";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TOKEN = "token";
    private static final String COLUMN_TOKEN_TYPE = "tokenType";

    public TokensDbHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_TOKENS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TOKEN + " TEXT,"
                + COLUMN_TOKEN_TYPE + " TEXT"
                + ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOKENS);
        onCreate(db);
    }

    public void addToken(String token, String tokenType) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_TOKENS + " WHERE " + COLUMN_TOKEN_TYPE + "=\"login\"");


        ContentValues values = new ContentValues();

        values.put(COLUMN_TOKEN, token);
        values.put(COLUMN_TOKEN_TYPE, "login");
        db.insert(TABLE_TOKENS, null, values);

        db.close();
    }

    public String getToken(String tokenType) {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = { COLUMN_TOKEN };
        Cursor findEntry = db.query(TABLE_TOKENS, columns, COLUMN_TOKEN_TYPE + "=?", new String[]{ tokenType }, null, null, null);

        findEntry.moveToFirst();
        db.close();

        return findEntry.getString(0);
    }
}