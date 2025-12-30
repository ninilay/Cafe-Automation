package CafeOtomasyon;

public class Kategori {
	private int id;
    private String kategoriAdi;

    public Kategori(int id, String kategoriAdi) {
        this.id = id;
        this.kategoriAdi = kategoriAdi;
    }

    // Getter ve Setter Metodları
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getKategoriAdi() { return kategoriAdi; }
    public void setKategoriAdi(String kategoriAdi) { this.kategoriAdi = kategoriAdi; }

    // GUI'de (Örn: ComboBox) sadece ismin görünmesi için toString'i override ediyoruz
    @Override
    public String toString() {
        return kategoriAdi;
    }

}
