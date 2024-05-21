package CatatanTransaksi;

public class Item1 {
    private int idTransaksi;
    private String kategori;
    private String harga;

    public Item1(int idTransaksi, String kategori, String harga) {
        this.idTransaksi = idTransaksi;
        this.kategori = kategori;
        this.harga = harga;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public String getKategori() {
        return kategori;
    }

    public String getHarga() {
        return harga;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
