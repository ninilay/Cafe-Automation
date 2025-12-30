package CafeOtomasyon;

public class Siparis {
	private Urun urun;
    private int adet;

    public Siparis(Urun urun, int adet) {
        this.urun = urun;
        this.adet = adet;
    }

    public Urun getUrun() { return urun; }
    public int getAdet() { return adet; }

}
