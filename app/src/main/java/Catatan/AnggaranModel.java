package Catatan;

public class AnggaranModel {
    private int id;
    private String kategori;
    private int nominal;
    private int progress;


    public AnggaranModel(int id, String kategori, int nominal, int progress) {
        this.id = id;
        this.kategori = kategori;
        this.nominal = nominal;
        this.progress = progress;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
