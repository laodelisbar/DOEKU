package Tabungan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTabungan extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tabungan.db";
    public static final String TABLE_TABUNGAN = "tabungan";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_NOMINAL = "nominal";

    public DatabaseTabungan(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_TABUNGAN + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAMA + " TEXT,"
                + COLUMN_NOMINAL + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABUNGAN);
        onCreate(db);
    }

    public void addTabungan(String nama, int nominal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_NOMINAL, nominal);
        db.insert(TABLE_TABUNGAN, null, values);
        db.close();
    }

    public Cursor getAllTabungan() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TABUNGAN, null, null, null, null, null, null);
    }

    public void deleteAllTabungan(String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TABUNGAN, COLUMN_NAMA + " = ?", new String[]{nama});
        db.close();
    }

    public void updateTabungan(int id, String nama, int nominal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_NOMINAL, nominal);
        db.update(TABLE_TABUNGAN, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<TabunganModel> getAllTabunganList() {
        List<TabunganModel> tabunganList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TABUNGAN, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA));
                int nominal = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOMINAL));
                tabunganList.add(new TabunganModel(id, nama, nominal));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tabunganList;
    }
}
