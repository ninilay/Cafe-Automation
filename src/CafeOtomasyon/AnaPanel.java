package CafeOtomasyon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AnaPanel extends JFrame {
    private VeriTabaniYonetici db = new VeriTabaniYonetici();
    private JPanel masaPaneli;
    private int aktifGarsonId; // Giriş yapan garsonun ID'sini tutmak için

    // Constructor artık bir garsonId parametresi alıyor
    public AnaPanel(int garsonId) {
        this.aktifGarsonId = garsonId;
        
        setTitle("Kafe Otomasyonu - Masa Durumları (Garson ID: " + aktifGarsonId + ")");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        masaPaneli = new JPanel(new GridLayout(3, 5, 15, 15)); 
        masaPaneli.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        masalariYükle();

        add(masaPaneli, BorderLayout.CENTER);
        
        JLabel lblBilgi = new JLabel("Masa Durumu: Yeşil (Boş), Kırmızı (Dolu), Sarı (Rezerve)", SwingConstants.CENTER);
        add(lblBilgi, BorderLayout.NORTH);
    }

    public void masalariYükle() {
        masaPaneli.removeAll();
        List<Masa> masalar = db.tumMasalariGetir();

        for (Masa m : masalar) {
            JButton btnMasa = new JButton("Masa " + m.getMasaNo());
            btnMasa.setFont(new Font("Arial", Font.BOLD, 16));
            
            if (m.getDurum().equalsIgnoreCase("BOS")) {
                btnMasa.setBackground(Color.GREEN);
            } else if (m.getDurum().equalsIgnoreCase("DOLU")) {
                btnMasa.setBackground(Color.RED);
            } else if (m.getDurum().equalsIgnoreCase("REZERVE")) {
                btnMasa.setBackground(Color.YELLOW); 
            }
            btnMasa.addActionListener(e -> {
                SiparisEkrani siparisEkrani = new SiparisEkrani(m.getMasaNo(), aktifGarsonId);
                
                // ÖNEMLİ: Sipariş ekranı kapandığında masaları yeniden yükle
                siparisEkrani.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        masalariYükle(); // Bu metot veritabanına gidip güncel renkleri çeker
                    }
                });
                
                siparisEkrani.setVisible(true);
            });

            masaPaneli.add(btnMasa);
        }
        masaPaneli.revalidate();
        masaPaneli.repaint();
    }

    // Main metodu test amaçlıdır, gerçek kullanımda GirisEkrani'ndan çağrılacak
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnaPanel(1).setVisible(true)); // Örnek ID: 1
    }
}