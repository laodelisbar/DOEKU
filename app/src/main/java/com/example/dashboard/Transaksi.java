package com.example.dashboard;

public class Transaksi {

    private int id;
    private String jenis;
    private int nominal;

    public Transaksi(int id, String jenis, int nominal) {
        this.id = id;
        this.jenis = jenis;
        this.nominal = nominal;
    }

    public int getId() {
        return id;
    }

    public String getJenis() {
        return jenis;
    }

    public int getNominal() {
        return nominal;
    }
}
