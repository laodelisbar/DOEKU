package CatatanTransaksi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private Context mContext;
    private ArrayList<Item> mItemList;
    private OnItemDeleteListener onItemDeleteListener;

    private DatabaseTransaksi db;


    public ItemAdapter(Context context, ArrayList<Item> itemList) {
        mContext = context;
        mItemList = itemList;
        db = new DatabaseTransaksi(context); // Inisialisasi database
    }

    public void setItemList(ArrayList<Item> itemList) {
        mItemList = itemList;
        notifyDataSetChanged();
    }

    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.onItemDeleteListener = listener;
    }



    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvKategori, tvHarga;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKategori = itemView.findViewById(R.id.kategori);
            tvHarga = itemView.findViewById(R.id.harga);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item_detail, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item currentItem = mItemList.get(position);
        holder.tvKategori.setText(currentItem.getKategori());
        holder.tvHarga.setText(currentItem.getHarga());

        // Simpan ID transaksi sebagai tag pada itemView
        holder.itemView.setTag(currentItem.getIdTransaksi());

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(mContext)
                    .setTitle("Pilih Aksi")
                    .setItems(new String[]{"Hapus", "Edit"}, (dialog, which) -> {
                        if (which == 0) {
                            // Hapus item
                            showDeleteDialog(currentItem, holder.getAdapterPosition());
                        } else {
                            // Edit item
                            showEditDialog(currentItem, holder.getAdapterPosition());
                        }
                    })
                    .setNegativeButton("Batal", null)
                    .show();
            return true;
        });
    }

    private void showDeleteDialog(Item item, int position) {
        new AlertDialog.Builder(mContext)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus item ini?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    // Ambil ID transaksi dari tag itemView
                    int idTransaksi = (int) mItemList.get(position).getIdTransaksi();

                    // Hapus item dari database
                    if (db.deleteTransaction(idTransaksi)) {
                        // Hapus item dari RecyclerView
                        mItemList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mItemList.size());

                        // Panggil listener jika ada
                        if (onItemDeleteListener != null) {
                            onItemDeleteListener.onItemDelete(item);

                        }
                    } else {
                        // Tampilkan pesan error jika penghapusan gagal
                        new AlertDialog.Builder(mContext)
                                .setTitle("Error")
                                .setMessage("Gagal menghapus item.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void showEditDialog(Item item, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Edit Item");

        builder.setMessage("Apakah Anda ingin mengedit item ini?");
        builder.setPositiveButton("Ya", (dialog, which) -> {
            // Panggil metode untuk memulai aktivitas edit dengan menyertakan data item
            Intent intent = new Intent(mContext, InputKategori4.class);
            intent.putExtra("position", position);
            intent.putExtra("idTransaksi", item.getIdTransaksi()); // Sertakan ID transaksi
            mContext.startActivity(intent);
        });
        builder.setNegativeButton("Tidak", null);
        builder.show();


    }


    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public interface OnItemDeleteListener {
        void onItemDelete(Item item);


    }

}
