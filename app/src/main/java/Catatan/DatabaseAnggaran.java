package Catatan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DatabaseAnggaran extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2; // Update versi database
    private static final String DATABASE_NAME = "AnggaranDB";
    public static final String TABLE_ANGGARAN = "anggaran";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_KATEGORI = "kategori";
    public static final String COLUMN_NOMINAL = "nominal";
    public static final String COLUMN_PROGRESS = "progress"; // Kolom baru

    public DatabaseAnggaran(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ANGGARAN + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_KATEGORI + " TEXT,"
                + COLUMN_NOMINAL + " INTEGER,"
                + COLUMN_PROGRESS + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_ANGGARAN + " ADD COLUMN " + COLUMN_PROGRESS + " INTEGER DEFAULT 0");
        }
    }

    public void addAnggaran(String kategori, int nominal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KATEGORI, kategori);
        values.put(COLUMN_NOMINAL, nominal);
        values.put(COLUMN_PROGRESS, 0); // Inisialisasi kolom progress
        long newRowId = db.insert(TABLE_ANGGARAN, null, values);
        db.close();

        if (newRowId != -1) {
            // Data berhasil ditambahkan
            Log.d("DatabaseAnggaran", "Kategori berhasil ditambahkan: " + kategori);
        } else {
            // Data gagal ditambahkan
            Log.e("DatabaseAnggaran", "Gagal menambahkan kategori: " + kategori);
        }
    }

    public boolean updateAnggaranProgress(String kategori, int jumlahUang) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_NOMINAL + ", " + COLUMN_PROGRESS + " FROM " + TABLE_ANGGARAN + " WHERE " + COLUMN_KATEGORI + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{kategori});

        if (cursor.moveToFirst()) {
            int totalAnggaran = cursor.getInt(cursor.getColumnIndex(COLUMN_NOMINAL));
            int currentProgress = cursor.getInt(cursor.getColumnIndex(COLUMN_PROGRESS));
            int newProgress = currentProgress + jumlahUang;

            ContentValues values = new ContentValues();
            values.put(COLUMN_PROGRESS, newProgress);

            int result = db.update(TABLE_ANGGARAN, values, COLUMN_KATEGORI + " = ?", new String[]{kategori});
            cursor.close();
            return result > 0;
        } else {
            cursor.close();
            return false;
        }
    }

    public Cursor getAllAnggaran() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ANGGARAN, null, null, null, null, null, null);
    }

    public void updateAnggaran(int id, String kategori, int nominal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KATEGORI, kategori);
        values.put(COLUMN_NOMINAL, nominal);
        db.update(TABLE_ANGGARAN, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAnggaran(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ANGGARAN, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getTargetAnggaran(String kategori) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_NOMINAL + " FROM " + TABLE_ANGGARAN + " WHERE " + COLUMN_KATEGORI + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{kategori});
        if (cursor != null && cursor.moveToFirst()) {
            int targetAnggaran = cursor.getInt(cursor.getColumnIndex(COLUMN_NOMINAL));
            cursor.close();
            return targetAnggaran;
        }
        return 0;
    }

    public ArrayList<String> getAllCategories() {
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_ANGGARAN, new String[]{COLUMN_KATEGORI}, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String kategori = cursor.getString(cursor.getColumnIndex(COLUMN_KATEGORI));
                categories.add(kategori);
            }
            cursor.close();
        }
        db.close();
        return categories;
    }

    public Cursor getAnggaranByKategori(String kategori) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_NOMINAL, COLUMN_PROGRESS}; // Kolom yang ingin Anda ambil
        String selection = COLUMN_KATEGORI + " = ?";
        String[] selectionArgs = {kategori};
        return db.query(TABLE_ANGGARAN, projection, selection, selectionArgs, null, null, null);
    }

}
