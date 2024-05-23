package CatatanTransaksi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.dashboard.R;

public class SumberDanaAdapter extends RecyclerView.Adapter<SumberDanaAdapter.SumberDanaViewHolder> {

    private ArrayList<SumberDanaWithSaldo> sumberDanaList;

    public SumberDanaAdapter(ArrayList<SumberDanaWithSaldo> sumberDanaList) {
        this.sumberDanaList = new ArrayList<>();

        // Konversi ModelSumberDana menjadi SumberDanaWithSaldo dan tambahkan ke sumberDanaList
        for (SumberDanaWithSaldo modelSumberDana : sumberDanaList) {
            SumberDanaWithSaldo sumberDanaWithSaldo = new SumberDanaWithSaldo(modelSumberDana.getNamaSumberDana(), modelSumberDana.getTotalSaldo());
            this.sumberDanaList.add(sumberDanaWithSaldo);
        }
    }

    @NonNull
    @Override
    public SumberDanaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saldo, parent, false);
        return new SumberDanaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SumberDanaViewHolder holder, int position) {
        SumberDanaWithSaldo sumberDana = sumberDanaList.get(position);
        holder.namaSumberDanaTextView.setText(sumberDana.getNamaSumberDana());
        holder.totalSaldoTextView.setText(String.valueOf(sumberDana.getTotalSaldo()));
    }

    @Override
    public int getItemCount() {
        return sumberDanaList.size();
    }

    public void updateData(ArrayList<SumberDanaWithSaldo> newData) {
        sumberDanaList.clear();
        for (SumberDanaWithSaldo modelSumberDana : newData) {
            SumberDanaWithSaldo sumberDanaWithSaldo = new SumberDanaWithSaldo(modelSumberDana.getNamaSumberDana(), modelSumberDana.getTotalSaldo());
            sumberDanaList.add(sumberDanaWithSaldo);
        }
        notifyDataSetChanged();
    }

    public static class SumberDanaViewHolder extends RecyclerView.ViewHolder {
        TextView namaSumberDanaTextView;
        TextView totalSaldoTextView;

        public SumberDanaViewHolder(@NonNull View itemView) {
            super(itemView);
            namaSumberDanaTextView = itemView.findViewById(R.id.nama_sumber_dana);
            totalSaldoTextView = itemView.findViewById(R.id.saldo_total);
        }
    }
}
