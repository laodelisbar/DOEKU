package Catatan;

public class AnggaranModel {
    private int id;
    private String kategori;
    private int nominal;

    public AnggaranModel(int id, String kategori, int nominal) {
        this.id = id;
        this.kategori = kategori;
        this.nominal = nominal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }
}
