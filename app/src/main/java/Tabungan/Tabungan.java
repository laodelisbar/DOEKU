package Tabungan;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;import com.example.dashboard.R;

public class Tabungan extends AppCompatActivity {
    private EditText inputNamaTabungan, inputNominalTabungan;
    private Button buttonSimpan;
    private DatabaseTabungan databaseTabungan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabungan);

        inputNamaTabungan = findViewById(R.id.et_nama);
        inputNominalTabungan = findViewById(R.id.et_target);
        buttonSimpan = findViewById(R.id.btn_tb);
        databaseTabungan = new DatabaseTabungan(this);

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahTabungan(); // Panggil metode tambahTabungan saat tombol disimpan diklik
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Aktifkan tombol Kembali
    }

    // Tangani navigasi saat tombol Kembali ditekan di Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Navigasi kembali ke activity induk
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tambahTabungan() {
        String nama = inputNamaTabungan.getText().toString().trim();
        String nominalStr = inputNominalTabungan.getText().toString().trim();

        if (!nama.isEmpty() && !nominalStr.isEmpty()) {
            int nominal = Integer.parseInt(nominalStr);
            databaseTabungan.addTabungan(nama, nominal);
            Toast.makeText(Tabungan.this, "Tabungan berhasil disimpan", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(Tabungan.this, "Nama dan nominal tabungan harus diisi", Toast.LENGTH_SHORT).show();
        }
    }}
