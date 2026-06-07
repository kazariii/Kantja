# Kantja (Kimo) 🐯

Kantja adalah aplikasi edukasi interaktif berbasis cerita untuk anak-anak, yang dirancang untuk menemani mereka belajar dan berpetualang melalui kisah-kisah menarik. Aplikasi ini tidak hanya ditujukan untuk anak, tetapi juga dilengkapi dengan fitur pendampingan komprehensif bagi orang tua untuk memantau perkembangan anak. Bersama maskot lucu bernama **Kimo** 🐯, anak-anak diajak untuk membaca, berinteraksi, dan bermain sambil belajar dengan cara yang menyenangkan.

## 🌟 Fitur Utama

### 1. Autentikasi (Masuk & Daftar)
Sistem login dan registrasi yang aman. Orang tua dapat membuat akun dan mengatur profil nama anak sebelum memulai petualangan.

### 2. Beranda (Home)
Pusat eksplorasi aplikasi tempat anak bisa menemukan petualangan dan cerita-cerita baru yang siap dimainkan bersama Kimo.

### 3. Perpustakaan Cerita (Story & Library)
Kumpulan cerita interaktif yang dirancang khusus untuk mengedukasi dan menghibur. Setiap cerita memiliki tantangan dan interaksi yang dapat dimainkan anak.

### 4. Langkah (Statistik & Progres)
Fitur *dashboard* khusus yang sangat berguna bagi orang tua untuk memantau perkembangan belajar anak. Menampilkan:
* **Total XP**: Poin pengalaman yang dikumpulkan anak.
* **Cerita Selesai**: Jumlah cerita yang sudah berhasil diselesaikan.
* **Tingkat Kesuksesan**: Rata-rata persentase keberhasilan anak dalam menjawab/melewati interaksi cerita.
* **Grafik Progres (Chart)**: Memantau riwayat skor dari cerita-cerita terakhir yang dimainkan.
* **Riwayat Cerita**: Detail kapan cerita diselesaikan beserta skor dan lencana yang didapat.

### 5. Prestasi (Score/Achievements)
Halaman untuk melihat daftar pencapaian, piala, dan lencana (badge) yang berhasil diraih anak setelah menyelesaikan cerita dengan hasil yang baik.

### 6. KANCA (Asisten Orang Tua)
Fitur obrolan interaktif (chatbot) yang dirancang sebagai asisten pribadi bagi orang tua. KANCA dapat memberikan respons cepat (*Quick Replies*) terkait:
* 🎯 **Target**: Menentukan tujuan belajar anak.
* 📊 **Perkembangan**: Menanyakan statistik perkembangan anak.
* 📚 **Rekomendasi**: Meminta saran cerita atau aktivitas berikutnya yang cocok.

### 7. Profil
Halaman untuk mengatur informasi akun pengguna, melihat detail orang tua dan anak, serta melakukan *logout*.

## 🛠️ Teknologi yang Digunakan
* **Bahasa Pemrograman**: Kotlin
* **UI Framework**: Jetpack Compose (100% Declarative UI)
* **Arsitektur**: MVVM (Model-View-ViewModel) dengan `StateFlow`
* **Navigasi**: Jetpack Compose Navigation
* **Data Formatting**: Gson (untuk mengelola format cerita JSON)

## 🚀 Cara Menjalankan Aplikasi
1. Lakukan *clone* pada repositori ini ke komputer Anda.
2. Buka proyek menggunakan **Android Studio** versi terbaru yang mendesukung Jetpack Compose.
3. Tunggu hingga proses sinkronisasi Gradle selesai.
4. Tekan tombol **Run** untuk menjalankan aplikasi pada Emulator Android atau perangkat fisik Anda.
