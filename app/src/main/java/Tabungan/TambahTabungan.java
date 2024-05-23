package Tabungan;

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
import androidx.appcompat.app.AlertDialog;
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
    private DatabaseTabungan databaseTabungan;
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

        databaseTabungan = new DatabaseTabungan(this);

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
        SQLiteDatabase db = databaseTabungan.getReadableDatabase();
        Cursor cursor = db.query(DatabaseTabungan.TABLE_TABUNGAN, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseTabungan.COLUMN_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTabungan.COLUMN_NAMA));
                int nominal = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseTabungan.COLUMN_NOMINAL));

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
                    showDeleteConfirmationDialog(tabungan, position);
                }
            });

            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nama = tabungan.getNama();
                    int nominal = tabungan.getNominal();
                    int id = tabungan.getId(); // Get the id of the tabungan item
                    int position = holder.getAdapterPosition(); // Get the position of the item in the RecyclerView

                    Intent intent = new Intent(TambahTabungan.this, RencanaActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("nama", nama);
                    intent.putExtra("nominal", nominal);
                    intent.putExtra("position", position); // Pass the position value
                    startActivity(intent);
                }
            });
        }

        private void showDeleteConfirmationDialog(final TabunganModel tabungan, final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TambahTabungan.this)
                    .setMessage("Apakah Anda yakin ingin menghapus " + tabungan.getNama() + "?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDatabase db = databaseTabungan.getWritableDatabase();
                            int deletedRows = db.delete(DatabaseTabungan.TABLE_TABUNGAN, DatabaseTabungan.COLUMN_ID + "=?", new String[]{String.valueOf(tabungan.getId())});
                            db.close();

                            if (deletedRows > 0) {
                                tabunganList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, tabunganList.size());

                                if (tabunganList.isEmpty()) {
                                    imgview.setVisibility(View.VISIBLE);
                                }

                                Toast.makeText(TambahTabungan.this, tabungan.getNama() + " berhasil dihapus", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            // Create the AlertDialog instance
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            // Change button colors
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(Color.parseColor("#FFB9CA")); // Change color to red

            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(Color.parseColor("#FFB9CA")); // Change color to green
        }



        @Override
        public int getItemCount() {
            return tabunganList.size();
        }

        // ViewHolder for RecyclerView
        public class TabunganViewHolder extends RecyclerView.ViewHolder {
            TextView namaTextView, nominalTextView, detail;
            ImageButton deleteButton;

            public TabunganViewHolder(@NonNull View itemView) {
                super(itemView);
                namaTextView = itemView.findViewById(R.id.tb_nama);
                nominalTextView = itemView.findViewById(R.id.tb_nominal);
                deleteButton = itemView.findViewById(R.id.delete);
                detail = itemView.findViewById(R.id.tb_detail);
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
