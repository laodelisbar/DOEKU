package CatatanTransaksi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dashboard.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private HalamanAdapter adapter;
    private DatabaseTransaksi dbTransaksi; // Pemasukkan
    private DatabaseTransaksi1 dbTransaksi1; // Pengeluaran
    ImageView backButton;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlisbar);

        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);
        backButton = findViewById(R.id.backButton);

        dbTransaksi = new DatabaseTransaksi(this); // Tabel untuk pemasukkan
        dbTransaksi1 = new DatabaseTransaksi1(this); // Tabel untuk pengeluaran

        Intent intent = getIntent();
        int numTabs = 2; // Kita akan memiliki 2 tab

        adapter = new HalamanAdapter(getSupportFragmentManager(), getLifecycle(), numTabs);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Pemasukkan");
                        break;
                    case 1:
                        tab.setText("Pengeluaran");
                        break;
                }
            }
        }).attach();

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            final int index = i;
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setOnLongClickListener(v -> {
                    showConfirmationDialog(index);
                    return true;
                });
            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke halaman lain
                Intent intent = new Intent(MainActivity.this, com.example.dashboard.MainActivity.class);
                startActivity(intent);
                // Jika ingin menutup activity saat ini
                finish();
            }
        });
    }

    private void showConfirmationDialog(int tabIndex) {
        String type;
        if (tabIndex == 0) {
            type = "Pemasukkan";
        } else {
            type = "Pengeluaran";
        }

        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Penghapusan")
                .setMessage("Apakah Anda yakin ingin menghapus semua data " + type + "?")
                .setPositiveButton("Ya", (dialog, which) -> handleLongClick(tabIndex))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void handleLongClick(int tabIndex) {
        boolean isDeleted = false;
        String type;

        if (tabIndex == 0) {
            // Operasi penghapusan untuk tab "Pemasukkan"
            type = "Pemasukkan";
            isDeleted = dbTransaksi.deleteAllTransactions(); // Menghapus semua transaksi pemasukkan
        } else {
            // Operasi penghapusan untuk tab "Pengeluaran"
            type = "Pengeluaran";
            isDeleted = dbTransaksi1.deleteAllTransactions(); // Menghapus semua transaksi pengeluaran
        }

        if (isDeleted) {
            Toast.makeText(this, "Data " + type + " berhasil dihapus", Toast.LENGTH_SHORT).show();

            Fragment fragment;
            if (tabIndex == 0) {
                fragment = getSupportFragmentManager().findFragmentByTag("f" + 0);
            } else {
                fragment = getSupportFragmentManager().findFragmentByTag("f" + 1);
            }

// Periksa apakah fragment ditemukan
            if (fragment != null) {
                // Panggil metode refreshData() di dalam fragment untuk memperbarui tampilan
                if (tabIndex == 0) {
                    ((Fragment1) fragment).refreshData();
                } else {
                    ((Fragment2) fragment).refreshData();
                }
            } else {
                Log.e(TAG, "Fragment tidak ditemukan");
            }


        } else {
            Toast.makeText(this, "Gagal menghapus data " + type, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Gagal menghapus data dengan tipe: " + type);
        }
    }
}
