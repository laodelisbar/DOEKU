package Catatan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private int totalPengeluaran = 0; // Total expenditure variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggaran);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button

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

    public void updateTotalAnggaran() {
        int total = 0;
        for (AnggaranModel anggaran : anggaranList) {
            total += anggaran.getNominal();
        }

        TextView textViewTotalAnggaran = findViewById(R.id.nominal_anggaran_total);
        textViewTotalAnggaran.setText("Rp. " + total);
    }

    public void updateTotalPengeluaran() {
        totalPengeluaran = 0;
        for (AnggaranModel anggaran : anggaranList) {
            totalPengeluaran += anggaran.getProgress();
        }

        TextView totalPengeluaranTextView = findViewById(R.id.nominal_pengeluaranTotal);
        totalPengeluaranTextView.setText("Rp. " + totalPengeluaran);
    }

    private void loadDataFromDatabase() {
        anggaranList.clear(); // Clear existing data
        Cursor cursor = databaseHelper.getAllAnggaran();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseAnggaran.COLUMN_ID));
                String kategori = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAnggaran.COLUMN_KATEGORI));
                int nominal = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseAnggaran.COLUMN_NOMINAL));
                int progress = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseAnggaran.COLUMN_PROGRESS)); // Add progress retrieval
                AnggaranModel anggaran = new AnggaranModel(id, kategori, nominal, progress); // Pass progress to the model
                anggaranList.add(anggaran);
            } while (cursor.moveToNext());
            // Set visibility of total anggaran cardview to VISIBLE if there are items
            findViewById(R.id.cardViewTotal).setVisibility(View.VISIBLE);
        } else {
            // Set visibility of total anggaran cardview to GONE if there are no items
            findViewById(R.id.cardViewTotal).setVisibility(View.GONE);
        }

        cursor.close();
        anggaranAdapter.notifyDataSetChanged();
        updateTotalAnggaran(); // Update total anggaran after loading data from the database
        updateTotalPengeluaran(); // Update total expenditure after loading data from the database
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

                if (kategori.isEmpty() || nominalStr.isEmpty()) {
                    Toast.makeText(Anggaran.this, "Silakan isi kategori dan nominal", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int nominal = Integer.parseInt(nominalStr);
                    databaseHelper.addAnggaran(kategori, nominal);
                    loadDataFromDatabase();

                    // Show the list of categories in DatabaseAnggaran
                    ArrayList<String> kategoriAnggaran = databaseHelper.getAllCategories();
                    Log.d("Kategori Anggaran", "Daftar Kategori: " + kategoriAnggaran.toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(Anggaran.this, "Nominal harus berupa angka", Toast.LENGTH_SHORT).show();
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

    // Handling when the back button in the toolbar is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Call onBackPressed() method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
