package Tabungan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
        Cursor cursor = db.query(DatabaseHelper.TABLE_TABUNGAN, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA));
                int nominal = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMINAL));

                TabunganModel tabungan = new TabunganModel(id, nama, nominal);
                tabunganList.add(tabungan);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        if (tabunganList.isEmpty()) {
            imgview.setVisibility(View.VISIBLE); // Show image if list is empty
        } else {
            imgview.setVisibility(View.GONE); // Hide image if list is not empty
        }

        tabunganAdapter.notifyDataSetChanged();
    }
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
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TambahTabungan.this);
                    builder.setMessage("Apakah Anda yakin ingin menghapus " + tabungan.getNama() + "?");
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Hapus item dari daftar
                            tabunganList.remove(position);
                            if (tabunganList.isEmpty()) {
                                // List kosong, tampilkan gambar
                                imgview.setVisibility(View.VISIBLE);
                            }

                            // Beritahu adapter bahwa dataset telah berubah
                            notifyDataSetChanged();
                            // Beritahu adapter bahwa dataset telah berubah

                            // Hapus item dari database
                            SQLiteDatabase db = databaseHelper.getWritableDatabase();
                            int deletedRows = db.delete(DatabaseHelper.TABLE_TABUNGAN, DatabaseHelper.COLUMN_NAMA + "=?", new String[]{tabungan.getNama()});
                            db.close();

                            // Tampilkan Toast jika penghapusan berhasil
                            if (deletedRows > 0) {
                                Toast.makeText(TambahTabungan.this, tabungan.getNama() + " berhasil dihapus", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing, just close the dialog
                            dialog.dismiss();
                        }
                    });
                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFB9CA"));

                    // Set text color for negative button
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFB9CA"));
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
            onBackPressed(); // Go back when home button is clicked
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
