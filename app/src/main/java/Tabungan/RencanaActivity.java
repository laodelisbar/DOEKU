package Tabungan;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard.R;

import java.util.ArrayList;
import java.util.List;

public class RencanaActivity extends AppCompatActivity {
    private TextView namaTextView, nominalTextView;
    private String nama;
    private int id, nominal, position;
    private List<TabunganModel> tabunganList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rencana_tabungan);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Extract the strings
        id = intent.getIntExtra("id", 0);
        nama = intent.getStringExtra("nama");
        nominal = intent.getIntExtra("nominal", 0);
        position = intent.getIntExtra("position", -1);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Get references to the TextViews
        namaTextView = findViewById(R.id.rencana_nama);
        nominalTextView = findViewById(R.id.rencana_target);

        // Set the text of the TextViews
        namaTextView.setText(nama);
        nominalTextView.setText(String.valueOf(nominal));

        // Initialize tabunganList
        tabunganList = new ArrayList<>(); // Initialize the list here or fill it with data

        // Set up Toolbar with navigation icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable Up button

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });
    }

    // Handle navigation when Up button is pressed in the Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Navigate back to parent activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to show the update dialog
    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(RencanaActivity.this, findViewById(R.id.update)); // Change with your anchor view id
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
                        String nama = editTextNama.getText().toString().trim();
                        String nominalStr = editTextNominal.getText().toString().trim();
                        if (!nama.isEmpty() && !nominalStr.isEmpty()) {
                            int nominal = Integer.parseInt(nominalStr);
                            TabunganModel tabungan = tabunganList.get(position);
                            tabungan.setNama(nama);
                            tabungan.setNominal(nominal);
                            databaseHelper.updateTabungan(tabungan.getId(), nama, nominal);
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

        // Set text color for positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFB9CA"));

        // Set text color for negative button
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFB9CA"));
    }
}
