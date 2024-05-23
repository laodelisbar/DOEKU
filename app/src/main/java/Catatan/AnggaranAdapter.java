package Catatan;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard.R;

import java.util.List;

public class AnggaranAdapter extends RecyclerView.Adapter<AnggaranAdapter.AnggaranViewHolder> {
    private Context context;
    private List<AnggaranModel> anggaranList;
    private DatabaseAnggaran databaseHelper;

    public AnggaranAdapter(Context context, List<AnggaranModel> anggaranList, DatabaseAnggaran databaseHelper) {
        this.context = context;
        this.anggaranList = anggaranList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public AnggaranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anggaran, parent, false);
        return new AnggaranViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnggaranViewHolder holder, final int position) {
        final AnggaranModel anggaran = anggaranList.get(position);
        holder.kategoriTextView.setText(anggaran.getKategori());
        holder.nominalTextView.setText(String.valueOf(anggaran.getNominal()));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.deleteButton, position);
            }
        });

        // Panggil fungsi updateProgressBar untuk setiap item anggaran
        updateProgressBar(holder, anggaran);

        // Menghitung nominal pengeluaran berdasarkan persentase progres
        calculateNominalPengeluaran(holder, anggaran);
    }

    @Override
    public int getItemCount() {
        return anggaranList.size();
    }

    public class AnggaranViewHolder extends RecyclerView.ViewHolder {
        TextView kategoriTextView, nominalTextView;
        ImageButton deleteButton;
        ProgressBar progressBarAnggaran;
        TextView tvProgressAnggaran;
        TextView txtNominalPengeluaran;

        public AnggaranViewHolder(@NonNull View itemView) {
            super(itemView);
            kategoriTextView = itemView.findViewById(R.id.kategori_anggaran_txt);
            nominalTextView = itemView.findViewById(R.id.nominal_anggaran_txt);
            deleteButton = itemView.findViewById(R.id.updateDelete_anggaran);
            progressBarAnggaran = itemView.findViewById(R.id.progressBarAnggaran);
            tvProgressAnggaran = itemView.findViewById(R.id.persentasi_progres);
            txtNominalPengeluaran = itemView.findViewById(R.id.nominal_pengeluaran);
        }
    }

    private void showPopupMenu(final View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_update) {
                    showUpdateDialog(position);
                    return true;
                } else if (itemId == R.id.menu_delete) {
                    deleteAnggaran(position);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    private void showUpdateDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Data Anggaran");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_anggaran, null);
        builder.setView(dialogView);

        final EditText editTextKategori = dialogView.findViewById(R.id.edit_text_kategori);
        final EditText editTextNominal = dialogView.findViewById(R.id.edit_text_nominal);

        AnggaranModel anggaran = anggaranList.get(position);
        editTextKategori.setText(anggaran.getKategori());
        editTextNominal.setText(String.valueOf(anggaran.getNominal()));

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String kategori = editTextKategori.getText().toString().trim();
                String nominalStr = editTextNominal.getText().toString().trim();

                if (kategori.isEmpty() || nominalStr.isEmpty()) {
                    Toast.makeText(context, "Silakan isi kategori dan nominal", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int nominal = Integer.parseInt(nominalStr);
                    if (nominal < 0) {
                        Toast.makeText(context, "Nominal tidak boleh negatif", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AnggaranModel anggaran = anggaranList.get(position);
                    anggaran.setKategori(kategori);
                    anggaran.setNominal(nominal);
                    databaseHelper.updateAnggaran(anggaran.getId(), kategori, nominal);
                    notifyItemChanged(position);

                    // Perbarui total anggaran setelah mengedit item
                    ((Anggaran) context).updateTotalAnggaran();
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Nominal harus berupa angka", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    private void updateTotalNominalPengeluaran() {
        int totalNominalPengeluaran = 0;
        for (AnggaranModel anggaran : anggaranList) {
            totalNominalPengeluaran += anggaran.getProgress();
        }

        TextView totalNominalPengeluaranTextView = ((Anggaran) context).findViewById(R.id.nominal_pengeluaranTotal);
        totalNominalPengeluaranTextView.setText("Rp. " + totalNominalPengeluaran);
    }
    private void deleteAnggaran(int position) {
        AnggaranModel anggaran = anggaranList.get(position);
        int id = anggaran.getId();
        databaseHelper.deleteAnggaran(id);
        anggaranList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, anggaranList.size());

        // Perbarui total anggaran setelah menghapus item
        ((Anggaran) context).updateTotalAnggaran();
        // Perbarui total nominal pengeluaran setelah menghapus item
        updateTotalNominalPengeluaran();
    }

    // Menambahkan fungsi updateProgressBar
    private void updateProgressBar(AnggaranViewHolder holder, AnggaranModel anggaran) {
        ProgressBar progressBar = holder.progressBarAnggaran;
        TextView tvProgressAnggaran = holder.tvProgressAnggaran;

        Cursor cursor = databaseHelper.getAnggaranByKategori(anggaran.getKategori());
        if (cursor != null && cursor.moveToFirst()) {
            int totalAnggaran = cursor.getInt(cursor.getColumnIndex("nominal"));
            int progress = cursor.getInt(cursor.getColumnIndex("progress"));
            cursor.close();

            // Pastikan progres tidak melebihi total anggaran
            if (progress > totalAnggaran) {
                progress = totalAnggaran;

            }

            int progressPercentage = (int) ((double) progress / totalAnggaran * 100);
            // Pastikan persentase tidak melebihi 100%
            if (progressPercentage > 100) {
                progressPercentage = 100;
            }

            progressBar.setProgress(progressPercentage);
            tvProgressAnggaran.setText(progressPercentage + "%");

            // Update progress in AnggaranModel
            anggaran.setProgress(progress);
            // Update nominal pengeluaran
            calculateNominalPengeluaran(holder, anggaran);
        }
    }

    private void calculateNominalPengeluaran(AnggaranViewHolder holder, AnggaranModel anggaran) {
        int progres = anggaran.getProgress();
        int nominalAnggaran = anggaran.getNominal();
        double persentase = (double) progres / nominalAnggaran * 100.0;  // Corrected formula
        double nominalPengeluaran = persentase * nominalAnggaran / 100;  // Corrected formula
        holder.txtNominalPengeluaran.setText(String.valueOf((int) nominalPengeluaran));
    }
}
