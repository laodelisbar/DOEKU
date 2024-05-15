package Tabungan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;
import com.example.dashboard.R;

public class RencanaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rencana_tabungan);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Extract the strings
        String nama = intent.getStringExtra("nama");
        int nominal = intent.getIntExtra("nominal", 0);

        // Get references to the TextViews
        TextView namaTextView = findViewById(R.id.rencana_nama); // replace with your actual ID
        TextView nominalTextView = findViewById(R.id.rencana_target); // replace with your actual ID

        // Set the text of the TextViews
        namaTextView.setText(nama);
        nominalTextView.setText(String.valueOf(nominal));

        // Set up Toolbar with navigation icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable Up button
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
}


