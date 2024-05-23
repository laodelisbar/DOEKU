package Tabungan;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dashboard.R;

import java.util.List;

public class RencanaActivity extends AppCompatActivity {
    private TextView namaTextView, nominalTextView;
    private String nama;
    private int id, nominal, position;
    private List<TabunganModel> tabunganList;
    private DatabaseTabungan databaseTabungan;
    private TabunganAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rencana_tabungan);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        nama = intent.getStringExtra("nama");
        nominal = intent.getIntExtra("nominal", 0);
        position = intent.getIntExtra("position", -1);

        databaseTabungan = new DatabaseTabungan(this);
        namaTextView = findViewById(R.id.rencana_nama);
        nominalTextView = findViewById(R.id.rencana_target);

        namaTextView.setText(nama);
        nominalTextView.setText(String.valueOf(nominal));

        tabunganList = databaseTabungan.getAllTabunganList();
        adapter = new TabunganAdapter(tabunganList);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(RencanaActivity.this, findViewById(R.id.update));
        popupMenu.getMenuInflater().inflate(R.menu.update_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_update) {
                    showUpdateDialog();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RencanaActivity.this);
        builder.setTitle("Update Data Tabungan");

        View dialogView = LayoutInflater.from(RencanaActivity.this).inflate(R.layout.update_tabungan, null);
        builder.setView(dialogView);

        final EditText editTextNama = dialogView.findViewById(R.id.ET_nama);
        final EditText editTextNominal = dialogView.findViewById(R.id.ET_target);

        editTextNama.setText(nama);
        editTextNominal.setText(String.valueOf(nominal));

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newNama = editTextNama.getText().toString().trim();
                        String nominalStr = editTextNominal.getText().toString().trim();
                        if (!newNama.isEmpty() && !nominalStr.isEmpty()) {
                            int newNominal = Integer.parseInt(nominalStr);
                            databaseTabungan.updateTabungan(id, newNama, newNominal);

                            // Update the TextViews
                            namaTextView.setText(newNama);
                            nominalTextView.setText(String.valueOf(newNominal));

                            // Update the list and notify adapter
                            TabunganModel updatedTabungan = tabunganList.get(position);
                            updatedTabungan.setNama(newNama);
                            updatedTabungan.setNominal(newNominal);

                            adapter.notifyItemChanged(position);

                            Toast.makeText(RencanaActivity.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RencanaActivity.this, "Nama dan Nominal harus diisi", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFB9CA"));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFB9CA"));
    }
}
