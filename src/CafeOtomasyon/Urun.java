package CafeOtomasyon;

public class Urun {
	private int id;
    private int kategoriId; // FOREIGN KEY karşılığı
    private String urunAdi;
    private double fiyat;
    private int stokMiktari;

    public Urun(int id, int kategoriId, String urunAdi, double fiyat, int stokMiktari) {
        this.id = id;
        this.kategoriId = kategoriId;
        this.urunAdi = urunAdi;
        this.fiyat = fiyat;
        this.stokMiktari = stokMiktari;
    }

    // Getter'lar
    public int getId() { return id; }
    public int getKategoriId() { return kategoriId; }
    public String getUrunAdi() { return urunAdi; }
    public double getFiyat() { return fiyat; }
    public int getStokMiktari() { return stokMiktari; }

}
