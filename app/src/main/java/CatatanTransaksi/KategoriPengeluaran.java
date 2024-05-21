package CatatanTransaksi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class KategoriPengeluaran extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BookLibraryPengeluaran.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "kategori_pengeluaran";
    public static final String COLUMN_ID = "id_kategori";
    public static final String COLUMN_NAMA_KATEGORI_PENGELUARAN = "nama_kategori";
    private final Context context;

    public KategoriPengeluaran(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA_KATEGORI_PENGELUARAN + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addPengeluaran(String nama) {
        if (nama.isEmpty()) {
            return false; // Data kosong
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAMA_KATEGORI_PENGELUARAN}, COLUMN_NAMA_KATEGORI_PENGELUARAN + "=?", new String[]{nama}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return false; // Duplikasi data
        }

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAMA_KATEGORI_PENGELUARAN, nama);
        long result = db.insert(TABLE_NAME, null, cv);
        cursor.close();
        db.close();
        return result != -1; // Berhasil jika result bukan -1
    }

    public ArrayList<String> getAllCategories() {
        ArrayList<String> kategoriList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String namaKategori = cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_KATEGORI_PENGELUARAN));
                    kategoriList.add(namaKategori);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return kategoriList;
    }

    public boolean deleteCategory(String namaKategori) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_NAMA_KATEGORI_PENGELUARAN + "=?", new String[]{namaKategori}) > 0;
    }

}
