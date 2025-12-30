package CafeOtomasyon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class YoneticiPaneli extends JFrame {
    private JTable urunTablosu;
    private DefaultTableModel model;
    private VeriTabaniYonetici db = new VeriTabaniYonetici();

    public YoneticiPaneli() {
        // Pencere Temel Ayarları
        setTitle("Kafe Otomasyonu - Yönetici Ürün Yönetimi");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. Tablo Yapısı
        String[] sutunlar = {"ID", "Kategori ID", "Ürün Adı", "Fiyat", "Stok"};
        model = new DefaultTableModel(sutunlar, 0);
        urunTablosu = new JTable(model);
        
        // Verileri Veritabanından Çek ve Tabloya Bas
        tabloyuGuncelle();

        // 2. Arayüz Düzeni (Layout)
        setLayout(new BorderLayout());
        
        // Üst kısma başlık
        JLabel lblBaslik = new JLabel("Ürün Yönetim Paneli", SwingConstants.CENTER);
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblBaslik, BorderLayout.NORTH);

        // Orta kısma tabloyu koy (Kaydırma çubuğu ile)
        add(new JScrollPane(urunTablosu), BorderLayout.CENTER);

        // 3. Butonlar Paneli (Alt Kısım)
        JPanel butonPanel = new JPanel();
        JButton btnEkle = new JButton("Yeni Ürün Ekle");
        JButton btnSil = new JButton("Seçili Ürünü Sil");
        JButton btnGuncelle = new JButton("Fiyat Güncelle");
        JButton btnYenile = new JButton("Listeyi Yenile");

        butonPanel.add(btnEkle);
        butonPanel.add(btnSil);
        butonPanel.add(btnGuncelle);
        butonPanel.add(btnYenile);
        add(butonPanel, BorderLayout.SOUTH);

        // 4. Butonlara Basınca Ne Olacak? (Yenileme Butonu Örneği)
        btnYenile.addActionListener(e -> tabloyuGuncelle());
        btnSil.addActionListener(e -> {
            int seciliSatir = urunTablosu.getSelectedRow();
            if (seciliSatir != -1) {
                int id = (int) model.getValueAt(seciliSatir, 0); 
                int cevap = JOptionPane.showConfirmDialog(this, "Bu ürünü silmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
                
                if (cevap == JOptionPane.YES_OPTION) {
                    if (db.urunSil(id)) {
                        JOptionPane.showMessageDialog(this, "Ürün başarıyla silindi.");
                        tabloyuGuncelle(); 
                    } else {
                        JOptionPane.showMessageDialog(this, "Silme işlemi başarısız!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek bir ürünü tablodan seçin!");
            }
        });
        btnEkle.addActionListener(e -> {
            JTextField adAlan = new JTextField();
            JTextField fiyatAlan = new JTextField();
            JTextField stokAlan = new JTextField();
            JTextField katIdAlan = new JTextField();

            Object[] mesaj = {
                "Ürün Adı:", adAlan,
                "Fiyat:", fiyatAlan,
                "Stok Miktarı:", stokAlan,
                "Kategori ID:", katIdAlan
            };
            int secenek = JOptionPane.showConfirmDialog(this, mesaj, "Yeni Ürün Ekle", JOptionPane.OK_CANCEL_OPTION);
            if (secenek == JOptionPane.OK_OPTION) {
                try {
                    String ad = adAlan.getText();
                    double fiyat = Double.parseDouble(fiyatAlan.getText());
                    int stok = Integer.parseInt(stokAlan.getText());
                    int katId = Integer.parseInt(katIdAlan.getText());

                    if (db.urunEkle(ad, fiyat, stok, katId)) {
                        JOptionPane.showMessageDialog(this, "Ürün başarıyla eklendi.");
                        tabloyuGuncelle();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Lütfen fiyat ve stok alanlarına sayı girin!");
                }
            }
        });
        btnGuncelle.addActionListener(e -> {
            int seciliSatir = urunTablosu.getSelectedRow();
            if (seciliSatir != -1) {
                int id = (int) model.getValueAt(seciliSatir, 0);
                String suankiFiyat = model.getValueAt(seciliSatir, 3).toString();
                
                String yeniFiyatStr = JOptionPane.showInputDialog(this, "Yeni fiyatı girin:", suankiFiyat);
                
                if (yeniFiyatStr != null && !yeniFiyatStr.isEmpty()) {
                    try {
                        double yeniFiyat = Double.parseDouble(yeniFiyatStr);
                        if (db.fiyatGuncelle(id, yeniFiyat)) {
                            JOptionPane.showMessageDialog(this, "Fiyat güncellendi.");
                            tabloyuGuncelle();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Geçersiz fiyat girişi!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen fiyatı güncellenecek ürünü seçin!");
            }
        });
    }

    // Veritabanındaki verileri tabloya dolduran yardımcı metod
    public void tabloyuGuncelle() {
        model.setRowCount(0); // Önce mevcut tabloyu temizle
        List<Urun> urunler = db.tumUrunleriGetir();
        for (Urun u : urunler) {
            Object[] satir = {u.getId(), u.getKategoriId(), u.getUrunAdi(), u.getFiyat(), u.getStokMiktari()};
            model.addRow(satir);
        }
    }
    public int yeniSiparisAc(int garsonId, int masaId) {
        // Sorguya masa_id eklendi
        String sql = "INSERT INTO SIPARISLER (garson_id, masa_id, toplam_tutar, durum) VALUES (?, ?, 0.00, 'Hazırlanıyor')";
        try (Connection conn = baglan(); 
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, garsonId);
            pstmt.setInt(2, masaId); // Masa ID veritabanına kaydediliyor
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public static void main(String[] args) {
        // Arayüzü başlat
        SwingUtilities.invokeLater(() -> {
            new YoneticiPaneli().setVisible(true);
        });
    }
    
    
    
    
}