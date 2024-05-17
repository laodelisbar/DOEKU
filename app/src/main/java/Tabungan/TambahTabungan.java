package Tabungan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back when home button is clicked
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
