package CafeOtomasyon;

public class Garson {
	private int id;
    private String garsonAdi;

    public Garson(int id, String garsonAdi) {
        this.id = id;
        this.garsonAdi = garsonAdi;
    }

    // Getter ve Setter Metodları
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getGarsonAdi() { return garsonAdi; }
    public void setGarsonAdi(String garsonAdi) { this.garsonAdi = garsonAdi; }

    @Override
    public String toString() {
        return garsonAdi;
    }

}
