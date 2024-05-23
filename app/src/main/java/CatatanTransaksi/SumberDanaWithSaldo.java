package CatatanTransaksi;

public class SumberDanaWithSaldo {
    private String namaSumberDana;
    private int totalSaldo;

    public SumberDanaWithSaldo(String namaSumberDana, int totalSaldo) {
        this.namaSumberDana = namaSumberDana;
        this.totalSaldo = totalSaldo;
    }

    public String getNamaSumberDana() {
        return namaSumberDana;
    }

    public void setNamaSumberDana(String namaSumberDana) {
        this.namaSumberDana = namaSumberDana;
    }

    public int getTotalSaldo() {
        return totalSaldo;
    }

    public void setTotalSaldo(int totalSaldo) {
        this.totalSaldo = totalSaldo;
    }
}
