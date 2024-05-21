package CatatanTransaksi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dashboard.R;

import java.util.ArrayList;
import java.util.Calendar;

public class InputKategori4 extends AppCompatActivity {
    EditText nama_kategori, jumlah_uang, nama_sumber_dana, etTanggalPemasukkan;
    Button add_button1, add_button2, simpan;
    Spinner kategori_pemasukkan, sumber_dana_pemasukkan;
    DatabaseTransaksi dbTransaksi;

    boolean isAddButton1Clicked = false;
    boolean isAddButton2Clicked = false;

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

        nama_kategori.setVisibility(View.GONE);
        add_button1.setVisibility(View.VISIBLE); // biarkan tombol terlihat agar bisa di klik
        nama_sumber_dana.setVisibility(View.GONE);
        add_button2.setVisibility(View.VISIBLE);

        // Panggil method untuk mengisi Spinner
        populateSpinners();

        // Ambil data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            String kategori = intent.getStringExtra("kategori");
            String harga = intent.getStringExtra("harga");
            int idTransaksi = intent.getIntExtra("idTransaksi", -1); // Ambil ID transaksi dari intent

            if (kategori != null) {
                nama_kategori.setText(kategori);
            }
            if (harga != null) {
                jumlah_uang.setText(harga);
            }
        }



        add_button1.setOnClickListener(view -> {
            if (!isAddButton1Clicked) {
                // Tampilkan EditText dan ubah tombol untuk fungsi tambah kategori
                nama_kategori.setVisibility(View.VISIBLE);
                isAddButton1Clicked = true;
            } else {
                KategoriPemasukkan myDB = new KategoriPemasukkan(InputKategori4.this);
                boolean success = myDB.addPemasukkan(nama_kategori.getText().toString().trim());
                if (success) {
                    Toast.makeText(InputKategori4.this, "Kategori berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputKategori4.this, "Gagal menambahkan kategori", Toast.LENGTH_SHORT).show();
                }
                populateSpinners();
                // Sembunyikan kembali setelah menambah kategori
                nama_kategori.setVisibility(View.GONE);
                isAddButton1Clicked = false;
            }
        });

        add_button2.setOnClickListener(view -> {
            if (!isAddButton2Clicked) {
                // Tampilkan EditText dan ubah tombol untuk fungsi tambah sumber dana
                nama_sumber_dana.setVisibility(View.VISIBLE);
                isAddButton2Clicked = true;
            } else {
                DatabaseSumberDanaPemasukkan myDB = new DatabaseSumberDanaPemasukkan(InputKategori4.this);
                boolean success = myDB.addSumber1(nama_sumber_dana.getText().toString().trim());
                if (success) {
                    Toast.makeText(InputKategori4.this, "Sumber dana berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputKategori4.this, "Gagal menambahkan sumber dana", Toast.LENGTH_SHORT).show();
                }
                populateSpinners();
                // Sembunyikan kembali setelah menambah sumber dana
                nama_sumber_dana.setVisibility(View.GONE);
                isAddButton2Clicked = false;
            }
        });

        etTanggalPemasukkan.setOnClickListener(view -> {
            // Tampilkan DatePicker saat EditText diklik
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(InputKategori4.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Proses tanggal yang dipilih
                        String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                        etTanggalPemasukkan.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        simpan.setOnClickListener(view -> {
            // Ambil nilai-nilai dari UI
            String tanggal = etTanggalPemasukkan.getText().toString(); // Mengambil tanggal dari EditText
            String jumlah = jumlah_uang.getText().toString().trim();
            String kategori = kategori_pemasukkan.getSelectedItem().toString();
            String sumberDana = sumber_dana_pemasukkan.getSelectedItem().toString();
            int idTransaksi = getIntent().getIntExtra("idTransaksi", -1); // Ambil ID transaksi dari intent

            // Validasi input jumlah uang
            int jumlahUang;
            try {
                jumlahUang = Integer.parseInt(jumlah);
            } catch (NumberFormatException e) {
                Toast.makeText(InputKategori4.this, "Masukkan jumlah uang yang valid", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cek apakah ini merupakan proses edit atau tambah data baru
            if (idTransaksi == -1) {
                // Proses tambah data baru
                boolean isSuccess = dbTransaksi.addTransaction(kategori, sumberDana, tanggal, jumlahUang);

                if (isSuccess) {
                    Toast.makeText(InputKategori4.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputKategori4.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Proses edit data
                boolean isSuccess = dbTransaksi.updateTransaction(idTransaksi, kategori, sumberDana, tanggal, jumlahUang);

                if (isSuccess) {
                    Toast.makeText(InputKategori4.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputKategori4.this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Mengatur ikon navigasi
        toolbar.setNavigationIcon(R.drawable.back);

        // Menambahkan listener untuk ikon navigasi
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke halaman lain
                Intent intent = new Intent(InputKategori4.this, MainActivity.class);
                startActivity(intent);
                // Jika ingin menutup activity saat ini
                finish();
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

        // Tambahkan listener long click untuk Spinner kategori_pemasukkan
        kategori_pemasukkan.setOnLongClickListener(v -> {
            int position = kategori_pemasukkan.getSelectedItemPosition();
            String selectedCategory = kategoriList.get(position);

            new AlertDialog.Builder(InputKategori4.this)
                    .setTitle("Hapus Kategori")
                    .setMessage("Apakah Anda yakin ingin menghapus kategori " + selectedCategory + "?")
                    .setPositiveButton("Hapus", (dialog, which) -> {
                        // Hapus kategori dari database
                        boolean isDeleted = kategoriDB.deleteCategory(selectedCategory);
                        if (isDeleted) {
                            Toast.makeText(InputKategori4.this, "Kategori berhasil dihapus", Toast.LENGTH_SHORT).show();
                            // Refresh Spinner
                            populateSpinners();
                        } else {
                            Toast.makeText(InputKategori4.this, "Gagal menghapus kategori", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Batal", null)
                    .show();
            return true;
        });

        // Tambahkan listener long click untuk Spinner sumber_dana_pemasukkan
        sumber_dana_pemasukkan.setOnLongClickListener(v -> {
            int position = sumber_dana_pemasukkan.getSelectedItemPosition();
            String selectedSumberDana = sumberDanaList.get(position);

            new AlertDialog.Builder(InputKategori4.this)
                    .setTitle("Hapus Sumber Dana")
                    .setMessage("Apakah Anda yakin ingin menghapus sumber dana " + selectedSumberDana + "?")
                    .setPositiveButton("Hapus", (dialog, which) -> {
                        // Hapus sumber dana dari database
                        boolean isDeleted = sumberDanaDB.deleteSumberDana(selectedSumberDana);
                        if (isDeleted) {
                            Toast.makeText(InputKategori4.this, "Sumber dana berhasil dihapus", Toast.LENGTH_SHORT).show();
                            // Refresh Spinner
                            populateSpinners();
                        } else {
                            Toast.makeText(InputKategori4.this, "Gagal menghapus sumber dana", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Batal", null)
                    .show();
            return true;
        });
    }
}

