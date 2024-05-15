package CatatanTransaksi;

import static CatatanTransaksi.DatabaseSumberDanaPemasukkan.COLUMN_NAMA_SUMBER_DANA;
import static CatatanTransaksi.KategoriPemasukkan.COLUMN_NAMA_KATEGORI_PEMASUKKAN;
import static CatatanTransaksi.KategoriPemasukkan.TABLE_NAME;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dashboard.R;

import java.util.ArrayList;
import java.util.Calendar;

public class InputKategori4 extends AppCompatActivity {
    EditText nama_kategori, jumlah_uang, nama_sumber_dana, etTanggalPemasukkan;
    Button add_button1, add_button2, simpan;
    Spinner kategori_pemasukkan, sumber_dana_pemasukkan;
    DatabaseTransaksi dbTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_pemasukkan);

        // Inisialisasi database transaksi
        dbTransaksi = new DatabaseTransaksi(this);

        // Inisialisasi elemen UI
        nama_kategori = findViewById(R.id.etTambahKategoriPemasukkan);
        add_button1 = findViewById(R.id.btnTambahKategoriPemasukkan);
        nama_sumber_dana = findViewById(R.id.etTambahSumberDanaPemasukkan);
        add_button2 = findViewById(R.id.btnTambahSumberDanaPemasukkan);
        etTanggalPemasukkan = findViewById(R.id.etTanggalPemasukkan);
        jumlah_uang = findViewById(R.id.etJmlUangPemasukkan);
        kategori_pemasukkan = findViewById(R.id.spinnerKategoriPemasukkan);
        sumber_dana_pemasukkan = findViewById(R.id.spinnerSumberDanaPemasukkan);
        simpan = findViewById(R.id.btnSimpanPemasukkan);

        // Panggil method untuk mengisi Spinner
        populateSpinners();

        // Atur listener untuk tombol tambah kategori
        add_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KategoriPemasukkan myDB = new KategoriPemasukkan(InputKategori4.this);
                myDB.addPemasukkan(nama_kategori.getText().toString().trim());
            }
        });

        // Atur listener untuk tombol tambah sumber dana
        add_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseSumberDanaPemasukkan myDB = new DatabaseSumberDanaPemasukkan(InputKategori4.this);
                myDB.addSumber1(nama_sumber_dana.getText().toString().trim());
            }
        });

        etTanggalPemasukkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tampilkan DatePicker saat EditText diklik
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(InputKategori4.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Proses tanggal yang dipilih
                                String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                etTanggalPemasukkan.setText(selectedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ambil nilai-nilai dari UI
                String tanggal = etTanggalPemasukkan.getText().toString(); // Mengambil tanggal dari EditText
                String jumlah = jumlah_uang.getText().toString().trim();
                String kategori = kategori_pemasukkan.getSelectedItem().toString();
                String sumberDana = sumber_dana_pemasukkan.getSelectedItem().toString();

                // Validasi input jumlah uang
                int jumlahUang;
                try {
                    jumlahUang = Integer.parseInt(jumlah);
                } catch (NumberFormatException e) {
                    Toast.makeText(InputKategori4.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lakukan penyimpanan data ke database transaksi
                boolean isSuccess = dbTransaksi.addTransaction(kategori, sumberDana, tanggal, jumlahUang);

                // Periksa apakah penyimpanan berhasil
                if (isSuccess) {
                    // Tampilkan pesan sukses
                    Toast.makeText(InputKategori4.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Tampilkan pesan gagal jika terjadi masalah
                    Toast.makeText(InputKategori4.this, "Failed to insert data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Method untuk mengisi Spinner dengan data dari tabel kategori_pemasukkan dan sumber_dana_pemasukkan
    private void populateSpinners() {
        // Ambil database kategori_pemasukkan
        KategoriPemasukkan kategoriDB = new KategoriPemasukkan(this);
        ArrayList<String> kategoriList = kategoriDB.getAllCategories();

        // Ambil database sumber_dana_pemasukkan
        DatabaseSumberDanaPemasukkan sumberDanaDB = new DatabaseSumberDanaPemasukkan(this);
        ArrayList<String> sumberDanaList = sumberDanaDB.getAllSumberDana();

        // Buat ArrayAdapter untuk kategori dan sumber dana
        ArrayAdapter<String> kategoriAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriList);
        ArrayAdapter<String> sumberDanaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sumberDanaList);

        // Atur layout dropdown untuk ArrayAdapter
        kategoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sumberDanaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Atur ArrayAdapter ke Spinner
        kategori_pemasukkan.setAdapter(kategoriAdapter);
        sumber_dana_pemasukkan.setAdapter(sumberDanaAdapter);
    }

}



