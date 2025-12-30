package CafeOtomasyon;
import java.util.ArrayList;
import java.util.List;

public class Masa {
	private int masaNo;
    private String durum;
    private List<Siparis> adisyon;
    
    public Masa(int masaNo) {
        this.masaNo = masaNo;
        this.durum = "BOS"; // Yeni oluşturulan masa varsayılan olarak boştur
        this.adisyon = new ArrayList<>();
    }

    // Masaya sipariş ekleme metodu
    public void siparisEkle(Siparis yeniSiparis) {
        this.adisyon.add(yeniSiparis);
        this.durum = "DOLU"; // Sipariş eklenince durum otomatik güncellenir
    }
 // Masanın toplam hesabını hesaplayan metod
    public double toplamHesapla() {
        double toplam = 0;
        for (Siparis s : adisyon) {
            toplam += s.getUrun().getFiyat() * s.getAdet();
        }
        return toplam;
    }

    // Masayı boşaltma (Ödeme alındıktan sonra)
    public void masayiSifirla() {
        this.adisyon.clear();
        this.durum = "BOS";
    }

    // Getter ve Setter Metodları
    public int getMasaNo() { return masaNo; }
    
    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }
    public List<Siparis> getAdisyon() { return adisyon; }


}
