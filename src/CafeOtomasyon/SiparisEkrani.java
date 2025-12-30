package CafeOtomasyon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class SiparisEkrani extends JFrame {
    private int masaId;
    private int garsonId;
    private int aktifSiparisId = -1;
    private VeriTabaniYonetici db = new VeriTabaniYonetici();
    private JTable urunSecimTablosu, adisyonTablosu;
    private DefaultTableModel adisyonModel;

    public SiparisEkrani(int masaId, int garsonId) {
        this.masaId = masaId;
        this.garsonId = garsonId;
        
        setTitle("Masa " + masaId + " - Sipariş Ekranı (Garson ID: " + garsonId + ")");
        setSize(800, 600);
        setLayout(new GridLayout(1, 2));
        setLocationRelativeTo(null);
        // ÖNEMLİ: Programın tamamen kapanmaması için DISPOSE kullanıyoruz
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // --- SOL PANEL: ÜRÜN SEÇİMİ ---
        JPanel solPanel = new JPanel(new BorderLayout());
        String[] urunSutunlar = {"ID", "Ürün", "Fiyat"};
        DefaultTableModel urunModel = new DefaultTableModel(urunSutunlar, 0);
        urunSecimTablosu = new JTable(urunModel);
        
        List<Urun> urunler = db.tumUrunleriGetir();
        for (Urun u : urunler) urunModel.addRow(new Object[]{u.getId(), u.getUrunAdi(), u.getFiyat()});
        
        solPanel.add(new JLabel("Menü"), BorderLayout.NORTH);
        solPanel.add(new JScrollPane(urunSecimTablosu), BorderLayout.CENTER);
        JButton btnEkle = new JButton("Seçili Ürünü Masaya Ekle");
        solPanel.add(btnEkle, BorderLayout.SOUTH);

        // --- SAĞ PANEL: ADİSYON ---
        JPanel sagPanel = new JPanel(new BorderLayout());
        String[] adisyonSutunlar = {"Ürün", "Adet", "Fiyat"};
        adisyonModel = new DefaultTableModel(adisyonSutunlar, 0);
        adisyonTablosu = new JTable(adisyonModel);
        
        // REZERVASYON BUTONU (SAĞ ÜST)
        JButton btnRezerve = new JButton("Masayı Rezerve Et (Sarı)");
        btnRezerve.setBackground(Color.YELLOW);
        btnRezerve.setForeground(Color.BLACK);
        sagPanel.add(btnRezerve, BorderLayout.NORTH); 

        sagPanel.add(new JScrollPane(adisyonTablosu), BorderLayout.CENTER);
        
        JButton btnOdemeAl = new JButton("Ödeme Al ve Masayı Kapat");
        sagPanel.add(btnOdemeAl, BorderLayout.SOUTH);

        add(solPanel);
        add(sagPanel);

        // --- VERİLERİ YÜKLE ---
        this.aktifSiparisId = db.aktifSiparisIdGetir(masaId);
        if (this.aktifSiparisId != -1) {
            adisyonuYukle();
        }

        // --- BUTON İŞLEMİ: REZERVE ET ---
        btnRezerve.addActionListener(e -> {
            int onay = JOptionPane.showConfirmDialog(this, "Masa rezerve edilsin mi?", "Rezervasyon", JOptionPane.YES_NO_OPTION);
            if (onay == JOptionPane.YES_OPTION) {
                db.masaDurumGuncelle(this.masaId, "REZERVE");
                JOptionPane.showMessageDialog(this, "Masa Rezerve Edildi!");
                this.dispose(); 
            }
        });

        // --- BUTON İŞLEMİ: ÜRÜN EKLEME ---
        btnEkle.addActionListener(e -> {
            int row = urunSecimTablosu.getSelectedRow();
            if (row != -1) {
                int urunId = (int) urunModel.getValueAt(row, 0);
                String ad = (String) urunModel.getValueAt(row, 1);
                double fiyat = (double) urunModel.getValueAt(row, 2);
                
                if (aktifSiparisId == -1) {
                    aktifSiparisId = db.yeniSiparisAc(this.garsonId, this.masaId); 
                    db.masaDurumGuncelle(this.masaId, "DOLU"); 
                }
                
                if (db.siparisDetayEkle(aktifSiparisId, urunId, 1, fiyat)) {
                    adisyonModel.addRow(new Object[]{ad, 1, fiyat});
                }
            }
        });

        // --- BUTON İŞLEMİ: ÖDEME ALMA ---
        btnOdemeAl.addActionListener(e -> {
            if (aktifSiparisId == -1) {
                JOptionPane.showMessageDialog(this, "Ödeme alınacak aktif bir sipariş bulunamadı!");
                return;
            }

            double toplam = 0;
            for (int i = 0; i < adisyonModel.getRowCount(); i++) {
                toplam += (double) adisyonModel.getValueAt(i, 2);
            }

            int onay = JOptionPane.showConfirmDialog(this, 
                "Toplam Tutar: " + toplam + " TL\nÖdeme alındı mı?", 
                "Ödeme ve Kapatma", JOptionPane.YES_NO_OPTION);

            if (onay == JOptionPane.YES_OPTION) {
                if (db.siparisKapat(aktifSiparisId, masaId, toplam)) {
                    JOptionPane.showMessageDialog(this, "Ödeme Başarılı. Masa Kapatıldı.");
                    this.dispose(); 
                }
            }
        });
    } // Constructor sonu

    private void adisyonuYukle() {
        adisyonModel.setRowCount(0); 
        String sql = "SELECT u.urun_adi, sd.adet, sd.birim_fiyat FROM SIPARIS_DETAY sd " +
                     "JOIN URUNLER u ON sd.urun_id = u.id WHERE sd.siparis_id = ?";
                     
        try (Connection conn = db.baglan(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, aktifSiparisId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                adisyonModel.addRow(new Object[]{rs.getString("urun_adi"), rs.getInt("adet"), rs.getDouble("birim_fiyat")});
                
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    
    
    
    
}