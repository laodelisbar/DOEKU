package com.example.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import Tabungan.RencanaActivity;


public class MainActivity extends AppCompatActivity {

    TextView Pengeluaran, Pemasukan;
    PieChart pieChart;
    Button btnTransaksi, btnCatatan, btnTabungan, btnAnggaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pengeluaran = findViewById(R.id.pengeluaran); // Inisialisasi TextView Pengeluaran
        Pemasukan = findViewById(R.id.pemasukan); // Inisialisasi TextView Pemasukan
        pieChart = findViewById(R.id.piechart);
        btnTransaksi = findViewById(R.id.bttransaksi);
        btnCatatan = findViewById(R.id.btcatatan);
        btnTabungan = findViewById(R.id.bttabungan);
        btnAnggaran = findViewById(R.id.btanggaran);

        setData();

        btnTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransaksiTerjadwal.class);
                startActivity(intent);
            }
        });
        btnCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CatatanTransaksi.MainActivity.class);
                startActivity(intent);
            }
        });
        btnTabungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Tabungan.TambahTabungan.class);
                startActivity(intent);
            }
        });
        btnAnggaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Catatan.Anggaran.class);
                startActivity(intent);
            }
        });

    }
    private void setData() {
        Pemasukan.setText(Integer.toString(40));
        Pengeluaran.setText(Integer.toString(30));

        pieChart.addPieSlice(new PieModel("Pemasukan",
                Integer.parseInt(Pemasukan.getText().toString()),
                Color.parseColor("#FFA726")));
        pieChart.addPieSlice(new PieModel("Pengeluaran",
                Integer.parseInt(Pengeluaran.getText().toString()),
                Color.parseColor("#66BB6A")));
        pieChart.startAnimation();
    }
}
