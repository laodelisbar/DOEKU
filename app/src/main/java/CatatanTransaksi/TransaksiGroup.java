package CatatanTransaksi;

import java.util.ArrayList;

public class TransaksiGroup {
    private String tanggal;
    private String hari;

    private ArrayList<Transaksi> transaksiList;

    public TransaksiGroup(String tanggal, String hari) {
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



    public void addTransaksi(Transaksi transaksi) {
        transaksiList.add(transaksi);
    }
}

