package CatatanTransaksi;

import android.content.Context;
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

    public ItemAdapter(Context context, ArrayList<Item> itemList) {
        mContext = context;
        mItemList = itemList;
    }
    public void setItemList(ArrayList<Item> itemList) {
        mItemList = itemList;
        notifyDataSetChanged(); // Memperbarui tampilan setelah mengatur daftar item
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
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
