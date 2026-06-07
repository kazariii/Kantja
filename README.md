# 🐯 KANTJA — Aplikasi Literasi Finansial Cerita Interaktif Anak Usia Dini

---

**KANTJA** adalah aplikasi Android edukasi literasi finansial berbasis cerita interaktif yang dirancang untuk anak usia dini. Bersama maskot **Kimo** 🐯, anak-anak diajak membuat keputusan bijak dalam setiap petualangan cerita — belajar nilai menabung, kejujuran, berbagi, dan belanja cerdas dengan cara yang menyenangkan.

Aplikasi ini juga dilengkapi fitur **Mode Orang Tua** dengan asisten percakapan **KANCA**, sehingga orang tua dapat memantau perkembangan belajar anak secara real-time.

---

## 🌟 Fitur Lengkap

### 🔐 1. Autentikasi
- Login dan registrasi akun orang tua menggunakan **email & kata sandi**
- Sesi pengguna dikelola secara lokal via `SimpleSession`
- Navigasi otomatis ke halaman utama setelah login berhasil

### 🏠 2. Beranda (HomeScreen)
- Menyambut pengguna dengan sapa personal berdasarkan nama anak
- Menampilkan **streak harian**, **total XP**, dan **badge terbaru**
- Rekomendasi cerita yang dikurasi untuk dimainkan

### 📚 3. Pustaka Cerita (LibraryScreen)
- Daftar lengkap cerita interaktif yang tersimpan sebagai **file JSON lokal** di `assets/stories/`
- Setiap cerita memiliki thumbnail, kategori nilai moral, dan estimasi waktu baca
- Filter cerita berdasarkan kategori: Sekolah, Fantasi, Belanja, Jelajah

### 🎭 4. Alur Cerita Interaktif (StoryScreen)
- Cerita ditampilkan scene per scene dengan **latar gambar** yang sesuai
- Progress bar visual menunjukkan posisi scene saat ini
- Di setiap **titik keputusan**, anak memilih dari 2 pilihan yang tersedia
- Setiap pilihan memunculkan **konsekuensi** beserta XP yang diperoleh/dikurangi
- Skor dihitung secara kumulatif dari setiap pilihan yang diambil

### 🏆 5. Layar Hasil (StoryResultScreen)
- Menampilkan **skor akhir** dan **persentase** dari skor maksimum cerita
- Label pencapaian dinamis: *Pahlawan Bijak*, *Pejuang Baik*, atau *Tetap Semangat*
- Nilai moral utama cerita disorot (Kejujuran, Menabung, Berbagi, Bijak Belanja)
- **Skor otomatis tersimpan ke Supabase** dan `storiesCompleted` diperbarui

### 🎖️ 6. Prestasi (ScoreScreen)
- Menampilkan semua badge yang sudah diraih dan yang masih terkunci
- Level pengguna dan progress XP menuju level berikutnya
- Badge dihitung berdasarkan jumlah cerita unik yang telah diselesaikan

### 📊 7. Langkah / Statistik (LangkahScreen)
- **3 stat tile**: Total XP, Cerita Selesai, Tingkat Sukses
- **Grafik garis** progress skor dari 7 cerita terakhir (smooth bezier curve)
- **Tingkat kesuksesan** keseluruhan dan skor terbaik yang pernah dicapai
- **Riwayat cerita** lengkap: judul, tanggal, skor, persentase, dan badge emoji

### 👤 8. Profil (ProfileScreen)
- Menampilkan nama, level, total XP, jumlah cerita, dan badge count
- Edit nama anak langsung dari dialog
- Pengaturan: Notifikasi, Suara & Musik (placeholder, siap dikembangkan)
- Tombol **Keluar** dengan navigasi kembali ke halaman login

### 💬 9. KANCA — Asisten Orang Tua (KancaScreen)
- Chatbot percakapan berbasis AI yang dapat menjawab pertanyaan orang tua
- Memberikan ringkasan aktivitas anak: cerita yang dimainkan, badge yang diraih, nilai moral yang dipelajari
- Quick replies untuk navigasi cepat: Target, Perkembangan, Rekomendasi

---

## 🗂️ Struktur Proyek

```
app/src/main/java/com/example/finalprojectpam/
│
├── data/
│   ├── local/
│   │   └── session/
│   │       └── SimpleSession.kt          # Manajemen sesi login lokal
│   ├── model/
│   │   ├── Choice.kt                     # Model pilihan dalam cerita
│   │   ├── Scene.kt                      # Model scene cerita
│   │   ├── ScoreRecord.kt                # Model rekaman skor
│   │   ├── Story.kt                      # Model cerita
│   │   └── UserProfile.kt                # Model profil pengguna
│   ├── remote/
│   │   └── SupabaseClient.kt             # Konfigurasi koneksi Supabase
│   └── repository/
│       ├── AuthRepository.kt             # Login, register, logout
│       ├── StoryRepository.kt            # Muat cerita dari JSON lokal
│       └── UserRepository.kt             # CRUD profil & skor ke Supabase
│
├── ui/
│   ├── auth/                             # Login & Register
│   ├── home/                             # Beranda
│   ├── kanca/                            # Asisten orang tua (KANCA)
│   ├── langkah/                          # Statistik & riwayat
│   ├── library/                          # Pustaka cerita
│   ├── navigation/                       # NavGraph & Screen routes
│   ├── profile/                          # Profil pengguna
│   ├── score/                            # Prestasi & badge
│   ├── story/                            # Alur cerita interaktif
│   └── theme/                            # Color, Type, Theme tokens
│
└── assets/
    └── stories/
        ├── pisang_kimo.json
        ├── tabungan_poki.json
        └── ...                           # File JSON setiap cerita
```

---

## 🗄️ Skema Database (Supabase / PostgreSQL)

### Tabel `app_users`
| Kolom | Tipe | Keterangan |
|---|---|---|
| `id` | `uuid` | Primary key, sesuai user session |
| `name` | `text` | Nama anak |
| `avatar_res` | `text` | Nama resource avatar |
| `total_score` | `int4` | Akumulasi total XP |
| `stories_completed` | `int4` | Jumlah cerita yang selesai |

### Tabel `score_records`
| Kolom | Tipe | Keterangan |
|---|---|---|
| `id` | `uuid` | Primary key |
| `user_id` | `uuid` | Foreign key ke `app_users` |
| `story_id` | `text` | ID cerita |
| `story_title` | `text` | Judul cerita |
| `score` | `int4` | Skor yang diperoleh |
| `max_score` | `int4` | Skor maksimum cerita |
| `completed_at` | `timestamp` | Waktu penyelesaian (UTC) |

---

## 🛠️ Teknologi yang Digunakan

| Kategori | Teknologi |
|---|---|
| Bahasa | Kotlin |
| UI Framework | Jetpack Compose (100% Declarative) |
| Arsitektur | MVVM + StateFlow |
| Navigasi | Jetpack Navigation Compose |
| Backend | Supabase (PostgreSQL) |
| ORM / Query | supabase-kt / Postgrest |
| Data Lokal | JSON di `assets/` + Gson |
| Session | SimpleSession (custom in-memory) |
| Animasi | Compose Animation + InfiniteTransition |
| Minimum SDK | API 26 (Android 8.0) |
| Target SDK | API 34 (Android 14) |

---

## 🚀 Cara Menjalankan Aplikasi

### Prasyarat
- **Android Studio** Hedgehog (2023.1.1) atau lebih baru
- **JDK 17**
- Emulator atau perangkat fisik dengan **Android 8.0 (API 26)** ke atas
- Koneksi internet (untuk Supabase)

### Langkah-langkah

```bash
# 1. Clone repositori ini
git clone https://github.com/kazariii/Kantja.git

# 2. Buka di Android Studio
# File → Open → pilih folder Kantja

# 3. Tunggu Gradle sync selesai

# 4. Jalankan aplikasi
# Klik tombol Run ▶ atau tekan Shift+F10
```

> **Catatan:** Konfigurasi Supabase (`SUPABASE_URL` dan `SUPABASE_KEY`) sudah tersedia di `SupabaseClient.kt`. Tidak perlu konfigurasi tambahan untuk menjalankan aplikasi.

---

## 📐 Desain & Panduan Warna

| Token | Warna | Hex |
|---|---|---|
| Background | Cream | `#FBF3E0` |
| Primary | Orange | `#F5A623` |
| Primary Dark | Orange Deep | `#ED8A0F` |
| Text Primary | Brown | `#5C3A1E` |
| Text Secondary | Text Soft | `#8A6A4F` |
| Muted | Muted | `#B89A7C` |

Font utama: **System Default Bold / ExtraBold** (Jetpack Compose default)

---

## 👨‍💻 Tim Pengembang

Dikembangkan sebagai **Final Project** mata kuliah Pemrograman Aplikasi Mobile  
**Universitas Brawijaya** — Juni 2026

---

## 📄 Lisensi

Proyek ini dibuat untuk keperluan akademis.  
