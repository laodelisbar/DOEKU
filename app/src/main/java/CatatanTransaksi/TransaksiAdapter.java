package CatatanTransaksi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard.R;

import java.util.ArrayList;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.TransaksiGroupViewHolder> {

    private Context mContext;
    private ArrayList<TransaksiGroup> mTransaksiGroupList;
    private DatabaseTransaksi mDatabaseTransaksi;

    // Constructor adapter RecyclerView
    public TransaksiAdapter(Context context, ArrayList<TransaksiGroup> transaksiGroupList) {
        mContext = context;
        mTransaksiGroupList = transaksiGroupList;
        mDatabaseTransaksi = new DatabaseTransaksi(context);
    }

    // ViewHolder untuk menampung tampilan item RecyclerView
    public static class TransaksiGroupViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTanggal, tvHari, tvPemasukkan, tvPengeluaran, tvTotal;
        public RecyclerView recyclerView;
        public ItemAdapter transaksiItemAdapter;
        // Tambahkan objek adapter

        public TransaksiGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTanggal = itemView.findViewById(R.id.tv_date);
            tvHari = itemView.findViewById(R.id.tv_day);
            recyclerView = itemView.findViewById(R.id.recyclerViewitemharian);
            transaksiItemAdapter = new ItemAdapter(itemView.getContext(), new ArrayList<>()); // Inisialisasi adapter
            recyclerView.setAdapter(transaksiItemAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }

    @NonNull
    @Override
    public TransaksiGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_data_harian, parent, false);
        return new TransaksiGroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiGroupViewHolder holder, int position) {
        TransaksiGroup currentGroup = mTransaksiGroupList.get(position);
        holder.tvTanggal.setText(currentGroup.getTanggal());
        holder.tvHari.setText(currentGroup.getHari());

        // Ambil daftar item dari database
        ArrayList<Item> itemList = mDatabaseTransaksi.getAllItemsFromDatabase(currentGroup.getTanggal());

        // Setel daftar item ke dalam adapter
        holder.transaksiItemAdapter.setItemList(itemList);
    }

    @Override
    public int getItemCount() {
        return mTransaksiGroupList.size();
    }


}
