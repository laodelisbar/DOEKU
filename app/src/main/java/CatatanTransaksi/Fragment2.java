package CatatanTransaksi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Fragment2 extends Fragment {

    private RecyclerView recyclerView;
    public TextView tvPemasukkan, tvPengeluaran, tvTotal;
    private TransaksiAdapter1 adapter1;
    private ArrayList<TransaksiGroup1> transaksiGroups1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.catatan_transaksi_pemasukkan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi TextView dan RecyclerView
        tvPemasukkan = view.findViewById(R.id.tv_totalpemasukkan);
        tvPengeluaran = view.findViewById(R.id.tv_totalpengeluaran);
        tvTotal = view.findViewById(R.id.tv_total);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi ArrayList untuk data transaksi
        transaksiGroups1 = new ArrayList<>();

        // Inisialisasi adapter TransaksiAdapter dan setel ke RecyclerView
        adapter1 = new TransaksiAdapter1(getContext(), transaksiGroups1);
        recyclerView.setAdapter(adapter1);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton2);

        // Tambahkan OnClickListener untuk floating action button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan pengguna ke halaman input pemasukkan
                Intent intent = new Intent(getActivity(), InputKategori2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Panggil metode untuk memperbarui data transaksi
        refreshData();
    }

    // Metode untuk memperbarui data transaksi
     void refreshData() {
        // Misalkan Anda ingin mengambil data dari DatabaseTransaksi1
        DatabaseTransaksi1 dbTransaksi = new DatabaseTransaksi1(getContext());
        ArrayList<String> tanggalList = dbTransaksi.getAllDates();
        ArrayList<String> hariList = dbTransaksi.getAllDays();

        // Mengelompokkan transaksi dengan tanggal dan hari yang sama ke dalam TransaksiGroup
        transaksiGroups1.clear(); // Membersihkan data yang lama sebelum menambahkan data yang baru

        for (int i = 0; i < tanggalList.size(); i++) {
            String tanggal = tanggalList.get(i);
            String hari = hariList.get(i);

            boolean groupExists = false;
            for (TransaksiGroup1 group : transaksiGroups1) {
                if (group.getTanggal().equals(tanggal) && group.getHari().equals(hari)) {
                    group.addTransaksi1(new Transaksi1(tanggal, hari));
                    groupExists = true;
                    break;
                }
            }

            if (!groupExists) {
                TransaksiGroup1 newGroup = new TransaksiGroup1(tanggal, hari);
                newGroup.addTransaksi1(new Transaksi1(tanggal, hari));
                transaksiGroups1.add(newGroup);
            }
        }

        // Memperbarui adapter dengan data yang baru
        adapter1.notifyDataSetChanged();

        // Misalkan Anda ingin mengambil data pemasukkan dari DatabaseTransaksi dan pengeluaran dari DatabaseTransaksi1
        DatabaseTransaksi dbPemasukkan = new DatabaseTransaksi(getContext());
        DatabaseTransaksi1 dbPengeluaran = new DatabaseTransaksi1(getContext());

        // Menghitung dan menampilkan total pemasukkan
        int totalPemasukkan = dbPemasukkan.getTotalPemasukkan();
        tvPemasukkan.setText("Rp. " + totalPemasukkan);

        // Menghitung dan menampilkan total pengeluaran
        int totalPengeluaran = dbPengeluaran.getTotalPengeluaran();
        tvPengeluaran.setText("Rp. " + totalPengeluaran);

        // Menghitung dan menampilkan total saldo
        int totalSaldo = totalPemasukkan - totalPengeluaran;
        tvTotal.setText("Rp. " + totalSaldo);
    }
}
