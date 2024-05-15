package CatatanTransaksi;

public class Transaksi {
    private String kategori;
    private String harga;

    public Transaksi(String kategori, String harga) {
        this.kategori = kategori;
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public String getHarga() {
        return harga;
    }
}



