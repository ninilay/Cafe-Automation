# Cafe-Automation
☕ Cafe Lezzet Otomasyon Sistemi
Bu proje, bir kafenin günlük operasyonlarını (giriş, masa yönetimi, sipariş ve ödeme) dijital ortamda yönetmek için geliştirilmiş Java Swing tabanlı ve MySQL veritabanı bağlantılı bir masaüstü uygulamasıdır.

Çoklu Kullanıcı Girişi: Yönetici ve Garson rolleri için ayrı giriş sekmeleri.

Dinamik Masa Yönetimi: Masaların anlık durumuna göre renk değişimi:

🟢 Yeşil: Boş Masa

🔴 Kırmızı: Dolu Masa (Aktif Sipariş Var)

🟡 Sarı: Rezerve Edilmiş Masa

Sipariş Takibi: Masaya özel adisyon açma, ürün ekleme ve toplam tutar hesaplama.

Veritabanı Entegrasyonu: Tüm işlemlerin (siparişler, personel, ürünler) MySQL üzerinde kalıcı olarak tutulması.

Şık Arayüz: Arkaplan görselleri ve şeffaf katmanlarla zenginleştirilmiş kullanıcı deneyimi.

🛠 Kullanılan Teknolojiler
Dil: Java (JDK 17+)

Arayüz: Java Swing & AWT

Veritabanı: MySQL

Kütüphaneler: JDBC (Java Database Connectivity)

📂 Proje Yapısı ve Mimari
Proje, N-Tier (Çok Katmanlı) mimariye yakın bir yapıda kurgulanmıştır:

VeriTabaniYonetici.java (Logic): Veritabanı bağlantısını kuran ve SQL sorgularını yöneten çekirdek sınıf.

myGUIMM.java (Login UI): Kafe temalı, sekmeli giriş ekranı.

AnaPanel.java (Main UI): Masaların durumunu görselleştiren ana dashboard.

SiparisEkrani.java (Order UI): Sipariş alma, rezervasyon ve ödeme işlemlerinin yapıldığı ekran.
