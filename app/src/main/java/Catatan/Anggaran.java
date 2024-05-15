package Catatan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class Anggaran extends AppCompatActivity {

    private RecyclerView recyclerViewAnggaran;
    private AnggaranAdapter anggaranAdapter;
    private List<AnggaranModel> anggaranList;
    private DatabaseAnggaran databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggaran);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Tampilkan tombol kembali

        recyclerViewAnggaran = findViewById(R.id.recycler_view_anggaran);
        anggaranList = new ArrayList<>();
        anggaranAdapter = new AnggaranAdapter(this, anggaranList, new DatabaseAnggaran(this));
        recyclerViewAnggaran.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAnggaran.setAdapter(anggaranAdapter);

        databaseHelper = new DatabaseAnggaran(this);

        FloatingActionButton fabAnggaran = findViewById(R.id.fab_anggaran);
        fabAnggaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanDialogTambahAnggaran();
            }
        });

        // Load data from SQLite database
        loadDataFromDatabase();
        Intent intent = getIntent();
    }

    private void loadDataFromDatabase() {
        anggaranList.clear(); // Clear existing data
        Cursor cursor = databaseHelper.getAllAnggaran();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseAnggaran.COLUMN_ID));
                String kategori = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAnggaran.COLUMN_KATEGORI));
                int nominal = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseAnggaran.COLUMN_NOMINAL));
                AnggaranModel anggaran = new AnggaranModel(id, kategori, nominal);
                anggaranList.add(anggaran);
            } while (cursor.moveToNext());
        }
        cursor.close();
        anggaranAdapter.notifyDataSetChanged();
    }

    private void tampilkanDialogTambahAnggaran() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Data Anggaran");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tambah_anggaran, null);

        final TextInputEditText inputKategori = view.findViewById(R.id.edit_text_kategori);
        final TextInputEditText inputNominal = view.findViewById(R.id.edit_text_nominal);

        builder.setView(view);

        builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String kategori = inputKategori.getText().toString().trim();
                String nominalStr = inputNominal.getText().toString().trim();

                if (!kategori.isEmpty() && !nominalStr.isEmpty()) {
                    int nominal = Integer.parseInt(nominalStr);
                    databaseHelper.addAnggaran(kategori, nominal);
                    // Refresh RecyclerView dengan memuat data ulang dari database
                    loadDataFromDatabase();
                } else {
                    Toast.makeText(Anggaran.this, "Silakan isi kategori dan nominal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Menangani ketika tombol kembali di toolbar diklik
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Panggil metode onBackPressed()
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}











