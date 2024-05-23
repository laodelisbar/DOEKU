package Tabungan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard.R;

import java.util.List;

public class TabunganAdapter extends RecyclerView.Adapter<TabunganAdapter.TabunganViewHolder> {
    private Context context;
    private List<TabunganModel> tabunganList;

    public TabunganAdapter(List<TabunganModel> tabunganList) {
        this.tabunganList = tabunganList;
    }

    @NonNull
    @Override
    public TabunganViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tabungan, parent, false);
        return new TabunganViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TabunganViewHolder holder, final int position) {
        final TabunganModel tabungan = tabunganList.get(position);
        holder.namaTextView.setText(tabungan.getNama());
        holder.nominalTextView.setText(String.valueOf(tabungan.getNominal()));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseTabungan databaseTabungan = new DatabaseTabungan(context);
                databaseTabungan.deleteAllTabungan(tabungan.getNama());
                updateTabunganListFromDatabase();
            }
        });

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RencanaActivity.class);
                intent.putExtra("id", tabungan.getId());
                intent.putExtra("nama", tabungan.getNama());
                intent.putExtra("nominal", tabungan.getNominal());
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    private void updateTabunganListFromDatabase() {
        DatabaseTabungan databaseTabungan = new DatabaseTabungan(context);
        tabunganList.clear();
        tabunganList.addAll(databaseTabungan.getAllTabunganList());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tabunganList.size();
    }

    public class TabunganViewHolder extends RecyclerView.ViewHolder {
        public View deleteButton;
        TextView namaTextView, nominalTextView, detail;

        public TabunganViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            namaTextView = itemView.findViewById(R.id.tb_nama);
            nominalTextView = itemView.findViewById(R.id.tb_nominal);
            detail = itemView.findViewById(R.id.tb_detail);
            deleteButton = itemView.findViewById(R.id.delete);
        }
    }
}
