package CatatanTransaksi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;import com.example.dashboard.R;

import java.util.ArrayList;

public class Fragment2 extends Fragment {

    private RecyclerView recyclerView;
    private TransaksiAdapter1 adapter1; // Menggunakan adapter TransaksiAdapter
    private ArrayList<TransaksiGroup1> transaksiGroups1; // Menggunakan ArrayList<TransaksiGroup>

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.catatan_transaksi_pemasukkan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi ArrayList untuk data transaksi
        transaksiGroups1 = new ArrayList<>();

        // Misalkan Anda ingin mengambil data dari DatabaseTransaksi
        DatabaseTransaksi1 dbTransaksi = new DatabaseTransaksi1(getContext());
        ArrayList<String> tanggalList = dbTransaksi.getAllDates();
        ArrayList<String> hariList = dbTransaksi.getAllDays();

        // Mengelompokkan transaksi dengan tanggal dan hari yang sama ke dalam TransaksiGroup
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
}