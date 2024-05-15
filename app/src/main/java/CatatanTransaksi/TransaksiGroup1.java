package CatatanTransaksi;

import java.util.ArrayList;

public class TransaksiGroup1 {
    private String tanggal;
    private String hari;
    private ArrayList<Transaksi1> transaksiList;

    public TransaksiGroup1(String tanggal, String hari) {
        this.tanggal = tanggal;
        this.hari = hari;
        transaksiList = new ArrayList<>();
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getHari() {
        return hari;
    }

    public void addTransaksi1(Transaksi1 transaksi1) {
        transaksiList.add(transaksi1);
    }
}

