package Catatan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.dashboard.R;
public class DatabaseAnggaran extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AnggaranDB";
    public static final String TABLE_ANGGARAN = "anggaran";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_KATEGORI = "kategori";
    public static final String COLUMN_NOMINAL = "nominal";

    public DatabaseAnggaran(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ANGGARAN + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_KATEGORI + " TEXT,"
                + COLUMN_NOMINAL + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANGGARAN);
        onCreate(db);
    }

    public void addAnggaran(String kategori, int nominal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KATEGORI, kategori);
        values.put(COLUMN_NOMINAL, nominal);
        db.insert(TABLE_ANGGARAN, null, values);
        db.close();
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
}

