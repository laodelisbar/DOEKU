package CatatanTransaksi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dashboard.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;

import Catatan.DatabaseAnggaran;
import Tabungan.DatabaseTabungan;
import Tabungan.TabunganModel;

public class InputKategori2 extends AppCompatActivity {

    EditText nama_kategori, jumlah_uang, nama_sumber_dana, etTanggalPengeluaran;
    Button add_button1, add_button2, simpan;
    Spinner kategori_pengeluaran, sumber_dana_pengeluaran;
    DatabaseTransaksi1 dbTransaksi1;
    DatabaseAnggaran anggaranDB;
    DatabaseTabungan tabunganDb;
    ArrayList<String> anggaranKategoriList, tabunganKategoriList;

    boolean isAddButton1Clicked = false;
    boolean isAddButton2Clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_pemasukkan);

        anggaranDB = new DatabaseAnggaran(this);
        dbTransaksi1 = new DatabaseTransaksi1(this);
        tabunganDb = new DatabaseTabungan(this);

        nama_kategori = findViewById(R.id.etTambahKategoriPemasukkan);
        add_button1 = findViewById(R.id.btnTambahKategoriPemasukkan);
        nama_sumber_dana = findViewById(R.id.etTambahSumberDanaPemasukkan);
        add_button2 = findViewById(R.id.btnTambahSumberDanaPemasukkan);
        etTanggalPengeluaran = findViewById(R.id.etTanggalPemasukkan);
        jumlah_uang = findViewById(R.id.etJmlUangPemasukkan);
        kategori_pengeluaran = findViewById(R.id.spinnerKategoriPemasukkan);
        sumber_dana_pengeluaran = findViewById(R.id.spinnerSumberDanaPemasukkan);
        simpan = findViewById(R.id.btnSimpanPemasukkan);

        nama_kategori.setVisibility(View.GONE);
        add_button1.setVisibility(View.VISIBLE); // biarkan tombol terlihat agar bisa di klik
        nama_sumber_dana.setVisibility(View.GONE);
        add_button2.setVisibility(View.VISIBLE);

        populateSpinners();

        add_button1.setOnClickListener(view -> {
            if (!isAddButton1Clicked) {
                // Tampilkan EditText dan ubah tombol untuk fungsi tambah kategori
                nama_kategori.setVisibility(View.VISIBLE);
                isAddButton1Clicked = true;
            } else {
                KategoriPengeluaran myDB = new KategoriPengeluaran(InputKategori2.this);
                boolean success = myDB.addPengeluaran(nama_kategori.getText().toString().trim());
                if (success) {
                    Toast.makeText(InputKategori2.this, "Kategori berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputKategori2.this, "Gagal menambahkan kategori", Toast.LENGTH_SHORT).show();
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
                DatabaseSumberDanaPemasukkan myDB = new DatabaseSumberDanaPemasukkan(InputKategori2.this);
                boolean success = myDB.addSumber1(nama_sumber_dana.getText().toString().trim());
                if (success) {
                    Toast.makeText(InputKategori2.this, "Sumber dana berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputKategori2.this, "Gagal menambahkan sumber dana", Toast.LENGTH_SHORT).show();
                }
                populateSpinners();
                // Sembunyikan kembali setelah menambah sumber dana
                nama_sumber_dana.setVisibility(View.GONE);
                isAddButton2Clicked = false;
            }
        });

        etTanggalPengeluaran.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(InputKategori2.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1;
                        etTanggalPengeluaran.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Di dalam metode simpan pada kelas InputKategori2
        simpan.setOnClickListener(view -> {
            // Ambil nilai-nilai dari UI
            String tanggal = etTanggalPengeluaran.getText().toString(); // Mengambil tanggal dari EditText
            String jumlah = jumlah_uang.getText().toString().trim();
            String kategori = kategori_pengeluaran.getSelectedItem().toString();
            String sumberDana = sumber_dana_pengeluaran.getSelectedItem().toString();
            int idTransaksi = getIntent().getIntExtra("idTransaksi", -1); // Ambil ID transaksi dari intent

            // Validasi input jumlah uang
            int jumlahUang;
            try {
                jumlahUang = Integer.parseInt(jumlah);
            } catch (NumberFormatException e) {
                Toast.makeText(InputKategori2.this, "Masukkan jumlah uang yang valid", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cek apakah ini merupakan proses edit atau tambah data baru
            if (idTransaksi == -1) {
                // Proses tambah data baru
                boolean isSuccess = dbTransaksi1.addTransaction(kategori, sumberDana, tanggal, jumlahUang);

                if (isSuccess) {
                    Toast.makeText(InputKategori2.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    boolean isUpdated = anggaranDB.updateAnggaranProgress(kategori, jumlahUang);
                    if (isUpdated) {
                        // Cek apakah progres sudah memenuhi target
                        int targetAnggaran = anggaranDB.getTargetAnggaran(kategori);
                        Cursor cursor = anggaranDB.getAnggaranByKategori(kategori);
                        if (cursor != null && cursor.moveToFirst()) {
                            int progress = cursor.getInt(cursor.getColumnIndex("progress"));
                            cursor.close();
                            if (progress >= targetAnggaran) {
                                // Tampilkan pesan konfirmasi
                                AlertDialog.Builder builder = new AlertDialog.Builder(InputKategori2.this);
                                builder.setTitle("Progres Anggaran Terpenuhi");
                                builder.setMessage("Progres anggaran untuk kategori " + kategori + " telah mencapai atau melebihi target.");
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            }
                        }
                    } else {
                        Toast.makeText(InputKategori2.this, "Gagal memperbarui progres anggaran", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(InputKategori2.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Proses edit data
                boolean isSuccess = dbTransaksi1.updateTransaction(idTransaksi, kategori, sumberDana, tanggal, jumlahUang);

                if (isSuccess) {
                    Toast.makeText(InputKategori2.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(InputKategori2.this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(InputKategori2.this, MainActivity.class);
                startActivity(intent);
                // Jika ingin menutup activity saat ini
                finish();
            }
        });
    }

    private void populateSpinners() {
        KategoriPengeluaran kategoriDB = new KategoriPengeluaran(this);
        ArrayList<String> kategoriList = kategoriDB.getAllCategories();
        anggaranKategoriList = anggaranDB.getAllCategories();

        DatabaseTabungan databaseTabungan = new DatabaseTabungan(this);
        List<TabunganModel> tabunganList = databaseTabungan.getAllTabunganList();
        ArrayList<String> tabunganNames = new ArrayList<>();
        for (TabunganModel tabungan : tabunganList) {
            tabunganNames.add(tabungan.getNama());
        }

        DatabaseSumberDanaPemasukkan sumberDanaDB = new DatabaseSumberDanaPemasukkan(this);
        ArrayList<String> sumberDanaList = sumberDanaDB.getAllSumberDana();

        LinkedHashSet<String> uniqueKategoriSet = new LinkedHashSet<>(kategoriList);
        uniqueKategoriSet.addAll(anggaranKategoriList);
        uniqueKategoriSet.addAll(tabunganNames);
        ArrayList<String> mergedKategoriList = new ArrayList<>(uniqueKategoriSet);

        ArrayAdapter<String> kategoriAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mergedKategoriList);
        kategoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori_pengeluaran.setAdapter(kategoriAdapter);

        ArrayAdapter<String> sumberDanaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sumberDanaList);
        sumberDanaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sumber_dana_pengeluaran.setAdapter(sumberDanaAdapter);

        sumberDanaAdapter.addAll(tabunganNames);

        sumber_dana_pengeluaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedSumberDana = sumberDanaAdapter.getItem(position);

                // Cari target tabungan terkait dan tampilkan di EditText jumlah uang
                int targetTabungan = 0; // Default value jika tidak ditemukan
                for (TabunganModel tabungan : tabunganList) {
                    if (tabungan.getNama().equals(selectedSumberDana)) {
                        targetTabungan = tabungan.getNominal();
                        break;
                    }
                }
                jumlah_uang.setText(String.valueOf(targetTabungan)); // Set target tabungan di EditText jumlah uang
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Tidak melakukan apa-apa
            }
        });

        kategori_pengeluaran.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = kategori_pengeluaran.getSelectedItemPosition();
                String selectedCategory = kategori_pengeluaran.getItemAtPosition(position).toString();

                new AlertDialog.Builder(InputKategori2.this)
                        .setTitle("Hapus Kategori")
                        .setMessage("Apakah Anda yakin ingin menghapus kategori " + selectedCategory + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            // Hapus kategori dari database
                            boolean isDeleted = kategoriDB.deleteCategory(selectedCategory);
                            if (isDeleted) {
                                Toast.makeText(InputKategori2.this, "Kategori berhasil dihapus", Toast.LENGTH_SHORT).show();
                                // Refresh Spinner
                                populateSpinners();
                            } else {
                                Toast.makeText(InputKategori2.this, "Gagal menghapus kategori", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Batal", null)
                        .show();
                return true;
            }
        });

// Tambahkan listener long click untuk Spinner sumber_dana_pengeluaran
        sumber_dana_pengeluaran.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = sumber_dana_pengeluaran.getSelectedItemPosition();
                String selectedSumberDana = sumber_dana_pengeluaran.getItemAtPosition(position).toString();

                new AlertDialog.Builder(InputKategori2.this)
                        .setTitle("Hapus Sumber Dana")
                        .setMessage("Apakah Anda yakin ingin menghapus sumber dana " + selectedSumberDana + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> {
                            // Hapus sumber dana dari database
                            boolean isDeleted = sumberDanaDB.deleteSumberDana(selectedSumberDana);
                            if (isDeleted) {
                                Toast.makeText(InputKategori2.this, "Sumber dana berhasil dihapus", Toast.LENGTH_SHORT).show();
                                // Refresh Spinner
                                populateSpinners();
                            } else {
                                Toast.makeText(InputKategori2.this, "Gagal menghapus sumber dana", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Batal", null)
                        .show();
                return true;
            }
        });

    }



}

