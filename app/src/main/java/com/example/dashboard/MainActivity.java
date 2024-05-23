package com.example.dashboard;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

import CatatanTransaksi.DatabaseSumberDanaPemasukkan;
import CatatanTransaksi.DatabaseTransaksi;
import CatatanTransaksi.SumberDanaAdapter;
import CatatanTransaksi.SumberDanaWithSaldo;

public class MainActivity extends AppCompatActivity {

    TextView Pengeluaran, Pemasukan;
    PieChart pieChart;
    Button btnTransaksi, btnCatatan, btnTabungan, btnAnggaran;
    RecyclerView recyclerView;
    SumberDanaAdapter adapter;
    ArrayList<SumberDanaWithSaldo> sumberDanaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pengeluaran = findViewById(R.id.pengeluaran);
        Pemasukan = findViewById(R.id.pemasukan);
        pieChart = findViewById(R.id.piechart);
        btnTransaksi = findViewById(R.id.bttransaksi);
        btnCatatan = findViewById(R.id.btcatatan);
        btnTabungan = findViewById(R.id.bttabungan);
        btnAnggaran = findViewById(R.id.btanggaran);
        recyclerView = findViewById(R.id.rvDashboard);

        // Inisialisasi RecyclerView dan Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sumberDanaList = new ArrayList<>();
        adapter = new SumberDanaAdapter(sumberDanaList);
        recyclerView.setAdapter(adapter); // Atur adapter ke RecyclerView di sini

        // Memuat data dari database setelah inisialisasi adapter
        loadDataFromDatabase();

        setData();

        // Mendengarkan klik tombol
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

        // Ambil referensi ke AppCompatButton dari layout
        AppCompatButton downsaldo = findViewById(R.id.downsaldo);

        // Tambahkan OnClickListener ke AppCompatButton
        downsaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Periksa apakah RecyclerView sedang terlihat atau tidak
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    // Jika terlihat, sembunyikan RecyclerView
                    recyclerView.setVisibility(View.GONE);
                } else {
                    // Jika tidak terlihat, tampilkan RecyclerView
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Memuat data dari database setelah inisialisasi adapter
        loadDataFromDatabase();
        setData();
    }

    // Mendapatkan data total saldo untuk setiap sumber dana
    private void loadDataFromDatabase() {
        // Ambil data nama sumber dana
        DatabaseSumberDanaPemasukkan dbSumberDana = new DatabaseSumberDanaPemasukkan(this);
        ArrayList<String> namaSumberDanaList = dbSumberDana.getAllSumberDana();

        // Ambil data total saldo
        DatabaseTransaksi dbTransaksi = new DatabaseTransaksi(this);
        ArrayList<Integer> totalSaldoList = new ArrayList<>();
        for (String namaSumberDana : namaSumberDanaList) {
            int totalSaldo = dbTransaksi.getTotalSaldoBySumberDana(namaSumberDana);
            totalSaldoList.add(totalSaldo);
        }

        // Gabungkan data nama sumber dana dan total saldo
        ArrayList<SumberDanaWithSaldo> sumberDanaWithSaldoList = new ArrayList<>();
        for (int i = 0; i < namaSumberDanaList.size(); i++) {
            String namaSumberDana = namaSumberDanaList.get(i);
            int totalSaldo = totalSaldoList.get(i);
            SumberDanaWithSaldo sumberDanaWithSaldo = new SumberDanaWithSaldo(namaSumberDana, totalSaldo);
            sumberDanaWithSaldoList.add(sumberDanaWithSaldo);
        }

        // Set data ke adapter
        adapter.updateData(sumberDanaWithSaldoList);
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
