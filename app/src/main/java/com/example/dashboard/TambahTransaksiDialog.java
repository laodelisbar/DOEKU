package com.example.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TambahTransaksiDialog extends DialogFragment {

    private EditText etJenisTransaksi;
    private EditText etNominal;
    private DatabaseHelper dbHelper;

    public static TambahTransaksiDialog newInstance() {
        return new TambahTransaksiDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tambah_transaksi, container, false);

        etJenisTransaksi = view.findViewById(R.id.editTextText);
        etNominal = view.findViewById(R.id.editTextText2);
        dbHelper = new DatabaseHelper(getContext());

        Button btnSimpan = view.findViewById(R.id.button);
        btnSimpan.setOnClickListener(v -> {
            String jenis = etJenisTransaksi.getText().toString();
            int nominal = Integer.parseInt(etNominal.getText().toString());

            // Add transaction to database
            long newRowId = dbHelper.addTransaksi(jenis, nominal);

            if (newRowId != -1) {
                // Transaction added successfully
                Toast.makeText(getContext(), "Transaction added!", Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                // Handle error if transaction failed to add
                Toast.makeText(getContext(), "Error adding transaction!", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnBatal = view.findViewById(R.id.batal);
        btnBatal.setOnClickListener(v -> dismiss());



        return view;
    }
}
