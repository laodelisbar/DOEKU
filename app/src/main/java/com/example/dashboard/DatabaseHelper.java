package com.example.dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Doeku.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_TRANSAKSI = "transaksi";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_JENIS = "jenis_transaksi";
    public static final String COLUMN_NOMINAL = "nominal";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TRANSAKSI + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_JENIS + " TEXT NOT NULL, "
                + COLUMN_NOMINAL + " INTEGER NOT NULL" + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSAKSI);
        onCreate(db);
    }

    public long addTransaksi(String jenis, int nominal) {
        SQLiteDatabase db = getWritableDatabase();
        long newRowId = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_JENIS, jenis);
            values.put(COLUMN_NOMINAL, nominal);
            newRowId = db.insert(TABLE_TRANSAKSI, null, values);
        } finally {
            db.close();
        }
        return newRowId;
    }

    public List<Transaksi> getAllTransaksi() {
        List<Transaksi> transaksiList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSAKSI;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Transaksi transaksi = new Transaksi(
                            cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_JENIS)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_NOMINAL))
                    );
                    transaksiList.add(transaksi);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }
        return transaksiList;
    }

    public void deleteTransaksi(Transaksi transaksi) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete(TABLE_TRANSAKSI, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(transaksi.getId())});
        } finally {
            db.close();
        }
    }
}
