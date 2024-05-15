package CatatanTransaksi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dashboard.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private HalamanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlisbar);

        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);

        Intent intent = getIntent();
        // Jumlah tab
        int numTabs = 2; // Kita akan memiliki 3 tab

        // Buat adapter
        adapter = new HalamanAdapter(getSupportFragmentManager(), getLifecycle(), numTabs);
        viewPager.setAdapter(adapter);

        // Hubungkan ViewPager dengan TabLayout
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
    }
}
