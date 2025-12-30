package CafeOtomasyon;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
public class VeriTabaniYonetici {
	// Kendi veritabanı bilgilerine göre buraları düzenle
	 static final String DB_URL="jdbc:mysql://localhost:3306/product?useSSL=false&serverTimezone=UTC";
	 static final String USER="root";
	 static final String PASS="Nly.9506-nb.";

    // Bağlantı nesnesini döndüren metod
    public Connection baglan() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // ÜRÜNLERİ LİSTELEME TESTİ
    public List<Urun> tumUrunleriGetir() {
        List<Urun> urunListesi = new ArrayList<>();
        String sql = "SELECT * FROM URUNLER";

        try (Connection conn = baglan();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Veritabanından verileri çekiyoruz
                int id = rs.getInt("id");
                int katId = rs.getInt("kategori_id");
                String ad = rs.getString("urun_adi");
                double fiyat = rs.getDouble("fiyat");
                int stok = rs.getInt("stok_miktari");

                // Java nesnesine dönüştürüp listeye ekliyoruz
                urunListesi.add(new Urun(id, katId, ad, fiyat, stok));
            }
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
        }
        return urunListesi;
    }
    public boolean urunSil(int id) {
        String sql = "DELETE FROM URUNLER WHERE id = ?";
        try (Connection conn = baglan();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean urunEkle(String ad, double fiyat, int stok, int katId) {
        String sql = "INSERT INTO URUNLER (kategori_id, urun_adi, fiyat, stok_miktari) VALUES (?, ?, ?, ?)";
        try (Connection conn = baglan(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, katId);
            pstmt.setString(2, ad);
            pstmt.setDouble(3, fiyat);
            pstmt.setInt(4, stok);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public boolean fiyatGuncelle(int id, double yeniFiyat) {
        String sql = "UPDATE URUNLER SET fiyat = ? WHERE id = ?";
        try (Connection conn = baglan(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, yeniFiyat);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
 // Tüm masaların durumlarını çekmek için
    public List<Masa> tumMasalariGetir() {
        List<Masa> masaListesi = new ArrayList<>();
        String sql = "SELECT * FROM MASALAR";
        try (Connection conn = baglan(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Masa m = new Masa(rs.getInt("id"));
                m.setDurum(rs.getString("durum"));
                masaListesi.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return masaListesi;
    }

    // Masa durumunu güncellemek için (Örn: Boştan Doluya)
    public void masaDurumGuncelle(int id, String yeniDurum) {
        String sql = "UPDATE MASALAR SET durum = ? WHERE id = ?";
        try (Connection conn = baglan(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, yeniDurum);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
 // Yeni bir ana sipariş oluşturur ve oluşan siparişin ID'sini geri döndürür
    public int yeniSiparisAc(int garsonId, int masaId) { // masaId eklendi
        String sql = "INSERT INTO SIPARISLER (garson_id, masa_id, toplam_tutar, durum) VALUES (?, ?, 0.00, 'Hazırlanıyor')";
        try (Connection conn = baglan(); 
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, garsonId);
            pstmt.setInt(2, masaId);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    // Siparişe ürün ekler
    public boolean siparisDetayEkle(int siparisId, int urunId, int adet, double birimFiyat) {
        String sql = "INSERT INTO SIPARIS_DETAY (siparis_id, urun_id, adet, birim_fiyat) VALUES (?, ?, ?, ?)";
        try (Connection conn = baglan(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, siparisId);
            pstmt.setInt(2, urunId);
            pstmt.setInt(3, adet);
            pstmt.setDouble(4, birimFiyat);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public boolean siparisKapat(int siparisId, int masaId, double toplamTutar) {
        // 1. Siparişin durumunu ve toplam tutarını güncelle
        String sqlSiparis = "UPDATE SIPARISLER SET durum = 'Tamamlandı', toplam_tutar = ? WHERE id = ?";
        // 2. Masayı tekrar BOS durumuna getir
        String sqlMasa = "UPDATE MASALAR SET durum = 'BOS' WHERE id = ?";

        try (Connection conn = baglan()) {
            conn.setAutoCommit(false); // İşlemleri paket yapıyoruz (Transaction)

            try (PreparedStatement ps1 = conn.prepareStatement(sqlSiparis);
                 PreparedStatement ps2 = conn.prepareStatement(sqlMasa)) {
                
                ps1.setDouble(1, toplamTutar);
                ps1.setInt(2, siparisId);
                ps1.executeUpdate();

                ps2.setInt(1, masaId);
                ps2.executeUpdate();

                conn.commit(); // İki işlem de tamamsa veritabanına işle
                return true;
            } catch (SQLException e) {
                conn.rollback(); // Bir hata olursa hiçbirini yapma (Geri al)
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int aktifSiparisIdGetir(int masaId) {
        // Sadece bu masaya ait ve 'Hazırlanıyor' durumundaki siparişi getir
        String sql = "SELECT id FROM SIPARISLER WHERE masa_id = ? AND durum = 'Hazırlanıyor' ORDER BY id DESC LIMIT 1";
        try (Connection conn = baglan(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, masaId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }
   
    public String[] girisBilgisiGetir(String kullanici, String sifre) {
        String sql = "SELECT rol, personel_id FROM KULLANICILAR WHERE kullanici_adi = ? AND sifre = ?";
        try (Connection conn = baglan(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, kullanici);
            pstmt.setString(2, sifre);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Rol ve Personel ID'sini dizi olarak döndürüyoruz
                return new String[]{rs.getString("rol"), rs.getString("personel_id")};
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return null; // Kullanıcı bulunamazsa null döner
        
        
        
    }

}
