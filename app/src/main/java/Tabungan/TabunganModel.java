package Tabungan;

public class TabunganModel {
    private int id;
    private String nama;
    private int nominal;

    public TabunganModel(int id, String nama, int nominal) {
        this.id = id;
        this.nama = nama;
        this.nominal = nominal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }
}
