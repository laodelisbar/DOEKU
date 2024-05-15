package Tabungan;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.annotation.SuppressLint;
import android.widget.Toast;import com.example.dashboard.R;



public class TabunganAdapter extends RecyclerView.Adapter<TabunganAdapter.TabunganViewHolder> {
    private Context context;
    private List<TabunganModel> tabunganList;

    public TabunganAdapter(Context context, List<TabunganModel> tabunganList) {
        this.context = context;
        this.tabunganList = tabunganList;
    }

    @NonNull
    @Override
    public TabunganViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tabungan, parent, false);
        return new TabunganViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TabunganViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final TabunganModel tabungan = tabunganList.get(position);
        holder.namaTextView.setText(tabungan.getNama());
        holder.nominalTextView.setText(String.valueOf(tabungan.getNominal()));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inisialisasi DatabaseHelper
                DatabaseHelper databaseHelper = new DatabaseHelper(context);

                // Hapus data tabungan berdasarkan nama dari database
                databaseHelper.deleteAllTabungan(tabungan.getNama());

                // Perbarui RecyclerView dengan data terbaru dari database
                // Anda perlu menentukan implementasi updateTabunganListFromDatabase()
                updateTabunganListFromDatabase();
            }
        });
    }


    // Method to update tabunganList with latest data from the database
    private void updateTabunganListFromDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        tabunganList.clear(); // Clear the existing list
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_TABUNGAN, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA));
                int nominal = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOMINAL));
                TabunganModel tabunganModel = new TabunganModel(nama, nominal);
                tabunganModel.setId(id);
                tabunganList.add(tabunganModel);
            }
            cursor.close();
        }
        notifyDataSetChanged(); // Notify the adapter that the dataset has changed
    }

    @Override
    public int getItemCount() {
        return tabunganList.size();
    }

    public class TabunganViewHolder extends RecyclerView.ViewHolder {
        public View deleteButton;
        TextView namaTextView, nominalTextView, detail;
        Context context; // Add a context variable

        public TabunganViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext(); // Initialize context

            namaTextView = itemView.findViewById(R.id.tb_nama);
            nominalTextView = itemView.findViewById(R.id.tb_nominal);
            detail = itemView.findViewById(R.id.tb_detail);

            // Set OnClickListener for the detail TextView
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get nama and nominal from TextViews
                    String nama = namaTextView.getText().toString();
                    int nominal = Integer.parseInt(nominalTextView.getText().toString());

                    // Create an Intent to start RencanaActivity
                    Intent intent = new Intent(context, RencanaActivity.class);
                    // Add nama and nominal as extras to the Intent
                    intent.putExtra("nama", nama);
                    intent.putExtra("nominal", nominal);
                    // Start the activity
                    context.startActivity(intent);
                }
            });
        }
    }
}