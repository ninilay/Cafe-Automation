package CafeOtomasyon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class myGUIMM extends JFrame {

    private JPanel contentPane;
    private JTextField txtYoneticiAd;
    private JPasswordField txtYoneticiSifre; // Şifre gizleme için JPasswordField yaptık
    private JTextField txtGarsonAd;
    private JPasswordField txtGarsonSifre;
    private VeriTabaniYonetici db = new VeriTabaniYonetici();

    public myGUIMM() {
        setTitle("CAFE OTOMASYONU - GİRİŞ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 600);
        setLocationRelativeTo(null);
        
        // Arkaplan Resmi
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    // Resim dosyanın src/CafeOtomasyon/ içinde olduğundan emin ol
                    ImageIcon icon = new ImageIcon(getClass().getResource("/cafe1.jpeg"));
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(new Color(40, 20, 10)); // Resim yoksa koyu kahve arkaplan
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblBaslik = new JLabel("CAFE LEZZET OTOMASYONU");
        lblBaslik.setForeground(new Color(245, 222, 179)); 
        lblBaslik.setHorizontalAlignment(SwingConstants.CENTER);
        lblBaslik.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 38));
        lblBaslik.setBounds(10, 40, 714, 55);
        contentPane.add(lblBaslik);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(125, 130, 500, 350);
        tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 13));
        contentPane.add(tabbedPane);

        Color panelRengi = new Color(60, 40, 30, 215); 
        Color yaziRengi = Color.WHITE;

        // --- YÖNETİCİ GİRİŞİ ---
        JPanel pnlYoneticiGiris = new JPanel(null);
        pnlYoneticiGiris.setBackground(panelRengi);
        tabbedPane.addTab("Yönetici Girişi", null, pnlYoneticiGiris, null);

        JLabel lblYoneticiAd = new JLabel("Kullanıcı Adı:");
        lblYoneticiAd.setForeground(yaziRengi);
        lblYoneticiAd.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblYoneticiAd.setBounds(40, 60, 130, 25);
        pnlYoneticiGiris.add(lblYoneticiAd);

        txtYoneticiAd = new JTextField();
        txtYoneticiAd.setBounds(180, 60, 240, 30);
        pnlYoneticiGiris.add(txtYoneticiAd);

        JLabel lblYoneticiSifre = new JLabel("Şifre:");
        lblYoneticiSifre.setForeground(yaziRengi);
        lblYoneticiSifre.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblYoneticiSifre.setBounds(40, 110, 130, 25);
        pnlYoneticiGiris.add(lblYoneticiSifre);

        txtYoneticiSifre = new JPasswordField();
        txtYoneticiSifre.setBounds(180, 110, 240, 30);
        pnlYoneticiGiris.add(txtYoneticiSifre);

        JButton btnYoneticiGiris = new JButton("Yönetici Olarak Giriş Yap");
        btnYoneticiGiris.setBackground(new Color(139, 69, 19)); 
        btnYoneticiGiris.setForeground(Color.WHITE);
        btnYoneticiGiris.setBounds(40, 190, 380, 45);
        pnlYoneticiGiris.add(btnYoneticiGiris);

        // --- GARSON GİRİŞİ ---
        JPanel pnlGarsonGiris = new JPanel(null);
        pnlGarsonGiris.setBackground(panelRengi);
        tabbedPane.addTab("Garson Girişi", null, pnlGarsonGiris, null);

        JLabel lblGarsonAd = new JLabel("Kullanıcı Adı:");
        lblGarsonAd.setForeground(yaziRengi);
        lblGarsonAd.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblGarsonAd.setBounds(40, 60, 130, 25);
        pnlGarsonGiris.add(lblGarsonAd);

        txtGarsonAd = new JTextField();
        txtGarsonAd.setBounds(180, 60, 240, 30);
        pnlGarsonGiris.add(txtGarsonAd);

        JLabel lblGarsonSifre = new JLabel("Şifre:");
        lblGarsonSifre.setForeground(yaziRengi);
        lblGarsonSifre.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblGarsonSifre.setBounds(40, 110, 130, 25);
        pnlGarsonGiris.add(lblGarsonSifre);

        txtGarsonSifre = new JPasswordField();
        txtGarsonSifre.setBounds(180, 110, 240, 30);
        pnlGarsonGiris.add(txtGarsonSifre);

        JButton btnGarsonGiris = new JButton("Garson Olarak Giriş Yap");
        btnGarsonGiris.setBackground(new Color(101, 67, 33));
        btnGarsonGiris.setForeground(Color.WHITE);
        btnGarsonGiris.setBounds(40, 190, 380, 45);
        pnlGarsonGiris.add(btnGarsonGiris);

        // --- AKSİYONLAR (BUTONLAR) ---

        // Yönetici Butonu
        btnYoneticiGiris.addActionListener(e -> {
            String user = txtYoneticiAd.getText();
            String pass = new String(txtYoneticiSifre.getPassword());
            girisKontrol(user, pass, "YONETICI");
        });

        // Garson Butonu
        btnGarsonGiris.addActionListener(e -> {
            String user = txtGarsonAd.getText();
            String pass = new String(txtGarsonSifre.getPassword());
            girisKontrol(user, pass, "GARSON");
        });
    }

    // Ortak Giriş Kontrol Metodu
    private void girisKontrol(String user, String pass, String beklenenRol) {
        String[] sonuc = db.girisBilgisiGetir(user, pass);
        
        if (sonuc != null) {
            String rol = sonuc[0];
            int personelId = (sonuc[1] != null) ? Integer.parseInt(sonuc[1]) : 0;

            if (rol.equals(beklenenRol)) {
                if (rol.equals("YONETICI")) {
                    new YoneticiPaneli().setVisible(true);
                } else {
                    new AnaPanel(personelId).setVisible(true);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Bu alana sadece " + beklenenRol + " girişi yapılabilir!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Hatalı Kullanıcı Adı veya Şifre!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new myGUIMM().setVisible(true));
    }
}