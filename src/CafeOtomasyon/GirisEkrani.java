package CafeOtomasyon;

import javax.swing.*;
import java.awt.*;

public class GirisEkrani extends JFrame {
    private JTextField txtKullanici;
    private JPasswordField txtSifre;
    private VeriTabaniYonetici db = new VeriTabaniYonetici();

    public GirisEkrani() {
        setTitle("Kafe Otomasyonu - Giriş");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Tasarım ayarları
        JPanel anaPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        anaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        anaPanel.add(new JLabel("Kullanıcı Adı:"));
        txtKullanici = new JTextField();
        anaPanel.add(txtKullanici);

        anaPanel.add(new JLabel("Şifre:"));
        txtSifre = new JPasswordField();
        anaPanel.add(txtSifre);

        JButton btnGiris = new JButton("Giriş Yap");
        anaPanel.add(new JLabel("")); // Boşluk için
        anaPanel.add(btnGiris);

        add(anaPanel);

        // Giriş Butonu Olayı
        btnGiris.addActionListener(e -> {
            String kullanici = txtKullanici.getText();
            String sifre = new String(txtSifre.getPassword());
            
            String[] sonuc = db.girisBilgisiGetir(kullanici, sifre);
            
            if (sonuc != null) {
                String rol = sonuc[0];
                String personelIdStr = sonuc[1];

                if (rol.equals("YONETICI")) {
                    JOptionPane.showMessageDialog(this, "Yönetici Girişi Başarılı!");
                    new YoneticiPaneli().setVisible(true);
                } else {
                    int pId = (personelIdStr != null) ? Integer.parseInt(personelIdStr) : 0;
                    JOptionPane.showMessageDialog(this, "Hoş geldin Garson!");
                    new AnaPanel(pId).setVisible(true); // Garson ID'sini AnaPanel'e gönderdik
                }
                this.dispose(); // Giriş ekranını kapat
            } else {
                JOptionPane.showMessageDialog(this, "Kullanıcı adı veya şifre hatalı!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}