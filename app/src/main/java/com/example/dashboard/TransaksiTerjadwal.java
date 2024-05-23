package com.example.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TransaksiTerjadwal extends AppCompatActivity {

    private RecyclerView rvTransaksi;
    private TransaksiAdapter adapter;
    private DatabaseHelper dbHelper;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_terjadwal);

        rvTransaksi = findViewById(R.id.rvTransaksi);
        dbHelper = new DatabaseHelper(this);
        adapter = new TransaksiAdapter(this, dbHelper.getAllTransaksi());
        rvTransaksi.setAdapter(adapter);
        rvTransaksi.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            TambahTransaksiDialog dialog = TambahTransaksiDialog.newInstance();
            dialog.show(getSupportFragmentManager(), "TambahTransaksiDialog");
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Update transaction list after adding a new transaction
            adapter.updateTransactions(dbHelper.getAllTransaksi());
        }
    }
}
