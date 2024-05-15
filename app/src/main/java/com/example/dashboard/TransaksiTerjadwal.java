package com.example.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TransaksiTerjadwal extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaksi_terjadwal);

        Intent intent = getIntent();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk membuka halaman TambahTransaksi.java
                Intent intent = new Intent(TransaksiTerjadwal.this, TambahTransaksi.class);
                startActivity(intent);
            }
        });
    }
}
