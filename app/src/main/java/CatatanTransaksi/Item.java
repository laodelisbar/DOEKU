package CatatanTransaksi;

public class Item {
    private String kategori;
    private String harga;

    public Item(String kategori, String harga) {
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

