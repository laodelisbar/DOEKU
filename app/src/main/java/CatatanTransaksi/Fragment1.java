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

import com.example.dashboard.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Fragment1 extends Fragment {

    private RecyclerView recyclerView;
    private TransaksiAdapter adapter; // Menggunakan adapter TransaksiAdapter
    private ArrayList<TransaksiGroup> transaksiGroups; // Menggunakan ArrayList<TransaksiGroup>

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
        transaksiGroups = new ArrayList<>();

        // Misalkan Anda ingin mengambil data dari DatabaseTransaksi
        DatabaseTransaksi dbTransaksi = new DatabaseTransaksi(getContext());
        ArrayList<String> tanggalList = dbTransaksi.getAllDates();
        ArrayList<String> hariList = dbTransaksi.getAllDays();

        // Mengelompokkan transaksi dengan tanggal dan hari yang sama ke dalam TransaksiGroup
        for (int i = 0; i < tanggalList.size(); i++) {
            String tanggal = tanggalList.get(i);
            String hari = hariList.get(i);

            boolean groupExists = false;
            for (TransaksiGroup group : transaksiGroups) {
                if (group.getTanggal().equals(tanggal) && group.getHari().equals(hari)) {
                    group.addTransaksi(new Transaksi(tanggal, hari));
                    groupExists = true;
                    break;
                }
            }

            if (!groupExists) {
                TransaksiGroup newGroup = new TransaksiGroup(tanggal, hari);
                newGroup.addTransaksi(new Transaksi(tanggal, hari));
                transaksiGroups.add(newGroup);
            }
        }

        // Inisialisasi adapter TransaksiAdapter dan setel ke RecyclerView
        adapter = new TransaksiAdapter(getContext(), transaksiGroups);
        recyclerView.setAdapter(adapter);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton2);

        // Tambahkan OnClickListener untuk floating action button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan pengguna ke halaman input pemasukkan
                Intent intent = new Intent(getActivity(), InputKategori4.class);
                startActivity(intent);
            }
        });
    }
}
