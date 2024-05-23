package com.example.dashboard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TambahTransaksi extends AppCompatActivity {
    private EditText etJenisTransaksi;
    private EditText etNominal;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_transaksi);

        etJenisTransaksi = findViewById(R.id.editTextText);
        etNominal = findViewById(R.id.editTextText2);
        dbHelper = new DatabaseHelper(this);

        Button btnSimpan = findViewById(R.id.button);
        btnSimpan.setOnClickListener(v -> {
            String jenis = etJenisTransaksi.getText().toString();
            String nominalStr = etNominal.getText().toString();
            if (!nominalStr.isEmpty()) {
                int nominal = Integer.parseInt(nominalStr);
                long newRowId = dbHelper.addTransaksi(jenis, nominal);

                if (newRowId != -1) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Error adding transaction!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Nominal cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
