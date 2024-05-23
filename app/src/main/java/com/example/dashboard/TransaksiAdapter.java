package com.example.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder> {

    private Context context;
    private List<Transaksi> transaksiList;

    public TransaksiAdapter(Context context, List<Transaksi> transaksiList) {
        this.context = context;
        this.transaksiList = transaksiList;
    }

    public void updateTransactions(List<Transaksi> newTransaksiList) {
        this.transaksiList = newTransaksiList;
        notifyDataSetChanged(); // Refresh the recycler view
    }

    @NonNull
    @Override
    public TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi, parent, false);
        return new TransaksiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiViewHolder holder, int position) {
        Transaksi transaksi = transaksiList.get(position);

        holder.tvJenis.setText(transaksi.getJenis());
        holder.tvNominal.setText(String.format("Rp.%d", transaksi.getNominal()));
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    static class TransaksiViewHolder extends RecyclerView.ViewHolder {

        TextView tvJenis;
        TextView tvNominal;

        public TransaksiViewHolder(View itemView) {
            super(itemView);
            tvJenis = itemView.findViewById(R.id.tvJenisTransaksi);
            tvNominal = itemView.findViewById(R.id.tvNominal);
        }
    }
}
