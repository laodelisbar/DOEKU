package Tabungan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class TambahTabungan extends AppCompatActivity {
    private RecyclerView recyclerViewTabungan;
    private TabunganAdapter tabunganAdapter;
    private List<TabunganModel> tabunganList;
    private DatabaseHelper databaseHelper;
    private ImageView imgview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_tabungan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerViewTabungan = findViewById(R.id.rvtabungan);
        imgview = findViewById(R.id.imv_notb);
        tabunganList = new ArrayList<>();
        tabunganAdapter = new TabunganAdapter(tabunganList);
        recyclerViewTabungan.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTabungan.setAdapter(tabunganAdapter);

        databaseHelper = new DatabaseHelper(this);

        FloatingActionButton fabTabungan = findViewById(R.id.add_tb);
        fabTabungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TambahTabungan.this, Tabungan.class);
                startActivity(intent);
            }
        });

        // Load data from SQLite database
        loadTabunganData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data from SQLite database
        loadTabunganData();
    }

    private void loadTabunganData() {
        tabunganList.clear(); // Clear existing data
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = databaseHelper.getAllTabungan();
        if (cursor.moveToFirst()) {
            do {
                String nama;
                int nominal;
                try {
                    nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA));
                    nominal = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMINAL));
                } catch (IllegalArgumentException e) {
                    // Handle exception
                    e.printStackTrace();
                    continue; // Skip this row
                }

                TabunganModel tabungan = new TabunganModel(nama, nominal);
                tabunganList.add(tabungan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (tabunganList.isEmpty()) {
            // List kosong, tampilkan gambar
            imgview.setVisibility(View.VISIBLE);
        } else {
            // List tidak kosong, sembunyikan gambar
            imgview.setVisibility(View.GONE);
        }

        tabunganAdapter.notifyDataSetChanged();
    }

    // Adapter for RecyclerView
    private class TabunganAdapter extends RecyclerView.Adapter<TabunganAdapter.TabunganViewHolder> {

        private List<TabunganModel> tabunganList;

        public TabunganAdapter(List<TabunganModel> tabunganList) {
            this.tabunganList = tabunganList;
        }

        @NonNull
        @Override
        public TabunganViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tabungan, parent, false);
            return new TabunganViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TabunganViewHolder holder, final int position) {
            final TabunganModel tabungan = tabunganList.get(position);
            holder.namaTextView.setText(tabungan.getNama());
            holder.nominalTextView.setText(String.valueOf(tabungan.getNominal()));
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Hapus item dari daftar
                    tabunganList.remove(position);
                    if (tabunganList.isEmpty()) {
                        // List kosong, tampilkan gambar
                        imgview.setVisibility(View.VISIBLE);
                    }

                    // Beritahu adapter bahwa dataset telah berubah
                    tabunganAdapter.notifyDataSetChanged();
                    // Beritahu adapter bahwa dataset telah berubah

                    // Hapus item dari database
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    int deletedRows = db.delete(DatabaseHelper.TABLE_TABUNGAN, DatabaseHelper.COLUMN_NAMA + "=?", new String[]{tabungan.getNama()});
                    db.close();

                    // Tampilkan Toast jika penghapusan berhasil
                    if (deletedRows > 0) {
                        Toast.makeText(TambahTabungan.this, holder.namaTextView.getText().toString() + " berhasil dihapus", Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }


        @Override
        public int getItemCount() {
            return tabunganList.size();
        }

        // ViewHolder for RecyclerView
        public class TabunganViewHolder extends RecyclerView.ViewHolder {
            public Button transparentbtn;
            TextView namaTextView, nominalTextView, detail;
            ImageButton deleteButton;
            ImageView imgview;

            public TabunganViewHolder(@NonNull View itemView) {
                super(itemView);

                namaTextView = itemView.findViewById(R.id.tb_nama);
                nominalTextView = itemView.findViewById(R.id.tb_nominal);
                deleteButton = itemView.findViewById(R.id.delete);
                imgview = itemView.findViewById(R.id.imv_notb);
                detail = itemView.findViewById(R.id.tb_detail);

                detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Retrieve nama and nominal values from TextViews
                        String nama = namaTextView.getText().toString();
                        int nominal = Integer.parseInt(nominalTextView.getText().toString());

                        // Create an Intent to start RencanaActivity
                        Intent intent = new Intent(getApplicationContext(), RencanaActivity.class);
                        // Add nama and nominal as extras to the Intent
                        intent.putExtra("nama", nama);
                        intent.putExtra("nominal", nominal);
                        // Start the activity
                        startActivity(intent);
                    }
                });
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Panggil metode onBackPressed()
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
