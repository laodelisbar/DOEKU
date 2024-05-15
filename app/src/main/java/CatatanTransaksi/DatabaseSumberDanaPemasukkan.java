package CatatanTransaksi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseSumberDanaPemasukkan extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BookLibrary.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "sumber_dana";
    public static final String COLUMN_ID = "id_sumber_dana";
    public static final String COLUMN_NAMA_SUMBER_DANA = "nama_sumber_dana";
    private final Context context;


    public DatabaseSumberDanaPemasukkan(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA_SUMBER_DANA + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addSumber1 (String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues ();

        cv.put(COLUMN_NAMA_SUMBER_DANA, nama);
        long result = db.insert (TABLE_NAME, null, cv );
        if (result == -1 ) {
            Toast.makeText (context, "Gagal menambahkan", Toast.LENGTH_SHORT).show() ;}
        else {
            Toast.makeText (context, "Sumber dana berhasil ditambahkan", Toast.LENGTH_SHORT).show() ;
        }
    }

    public ArrayList<String> getAllSumberDana() {
        ArrayList<String> sumberDanaList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String namaSumberDana = cursor.getString(cursor.getColumnIndex(COLUMN_NAMA_SUMBER_DANA));
                    sumberDanaList.add(namaSumberDana);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return sumberDanaList;
    }
}
