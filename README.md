# 🐯 KANTJA — Aplikasi Literasi Finansial Cerita Interaktif Anak Usia Dini


<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen?logo=android" />
  <img src="https://img.shields.io/badge/Language-Kotlin-blue?logo=kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-orange" />
  <img src="https://img.shields.io/badge/Backend-Supabase-3ECF8E?logo=supabase" />
  <img src="https://img.shields.io/badge/Architecture-MVVM-purple" />
  <img src="https://img.shields.io/badge/Min%20SDK-24-yellow" />
  <img src="https://img.shields.io/badge/Target%20SDK-36-red" />
  <img src="https://img.shields.io/badge/Version-1.0-lightgrey" />
</p>

---

**KANTJA** adalah aplikasi Android edukasi literasi finansial berbasis cerita interaktif yang dirancang untuk anak usia dini. Bersama maskot **Kimo** 🐯, anak-anak diajak membuat keputusan bijak di setiap petualangan — belajar nilai **menabung**, **kejujuran**, **berbagi**, dan **bijak belanja** dengan cara yang menyenangkan dan engaging.

Aplikasi ini juga dilengkapi **Mode Orang Tua** dengan asisten percakapan **KANCA**, sehingga orang tua dapat memantau perkembangan belajar anak secara real-time berdasarkan data aktivitas nyata.

---

## 🌟 Fitur Lengkap

### 🔐 1. Autentikasi (Login & Register)
- Registrasi akun baru dengan **nama, email, dan kata sandi**
- Login dengan validasi email & password langsung ke Supabase
- Deteksi email duplikat saat registrasi
- Sesi pengguna disimpan secara lokal via `SimpleSession` (SharedPreferences)
- Navigasi otomatis ke **Beranda** jika sudah login, atau ke **Login** jika belum

### 🏠 2. Beranda (HomeScreen)
- Sapaan personal berdasarkan nama pengguna yang tersimpan
- Menampilkan **total XP**, **cerita yang sudah selesai**, dan **badge terbaru**
- Daftar rekomendasi cerita dari library lokal
- Navigasi ke semua fitur utama via bottom navigation bar

### 📚 3. Pustaka Cerita (LibraryScreen)
- Menampilkan semua cerita yang tersedia dari `assets/stories/`
- Setiap kartu cerita menampilkan judul, kategori nilai moral, dan thumbnail karakter
- Tap kartu langsung navigasi ke `StoryScreen` dengan `storyFileName` sebagai argument

### 🎭 4. Alur Cerita Interaktif (StoryScreen)
- Cerita ditampilkan **scene per scene** dengan gambar latar yang berbeda per fase
- **Progress bar** visual (dot + garis) menunjukkan posisi scene saat ini
- Di setiap titik keputusan, anak memilih dari **2 pilihan** yang tersedia
- Setiap pilihan memunculkan panel **konsekuensi** beserta XP yang diperoleh/dikurangi
- Skor dihitung kumulatif dari setiap `scoreValue` pilihan yang diambil
- Setelah scene terakhir → otomatis navigasi ke `StoryResultScreen`

### 🏆 5. Layar Hasil Cerita (StoryResultScreen)
- Menampilkan **skor akhir** vs **skor maksimum** cerita
- Label pencapaian dinamis berdasarkan persentase:
  - ≥ 80% → *"Pahlawan Bijak!"* 🏆
  - ≥ 50% → *"Pejuang Baik!"* ⭐
  - < 50% → *"Tetap Semangat!"* 📖
- Nilai moral utama cerita disorot (Kejujuran, Menabung, Berbagi, Bijak)
- **Skor otomatis tersimpan** ke tabel `score_records` di Supabase
- `totalScore` dan `storiesCompleted` di `app_users` diperbarui secara otomatis

### 🎖️ 6. Prestasi (ScoreScreen)
- Menampilkan **level pengguna** dan progress XP menuju level berikutnya
- Badge dihitung berdasarkan jumlah **cerita unik** yang telah diselesaikan
- Tampilan badge terbuka (unlocked) dan terkunci (locked)

### 📊 7. Langkah / Statistik (LangkahScreen)
- **3 stat tile**: Total XP, Cerita Selesai, Tingkat Sukses
- **Grafik garis** progress skor dari 7 cerita terakhir (smooth bezier curve dengan fill area)
- **Tingkat kesuksesan** keseluruhan dengan progress bar
- Highlight **skor terbaik** yang pernah dicapai
- **Riwayat Cerita**: list lengkap semua cerita yang pernah dimainkan beserta tanggal, skor, persentase, dan badge emoji (🏆/⭐/📖)

### 👤 8. Profil (ProfileScreen)
- Menampilkan nama, level, total XP, jumlah cerita selesai, dan badge count
- **Edit nama** langsung dari dialog tanpa keluar layar
- Pengaturan Notifikasi & Suara (dialog placeholder, siap dikembangkan)
- Data profil di-refresh dari Supabase setiap kali layar dibuka
- Tombol **Keluar** yang clear session dan kembali ke Login

### 💬 9. KANCA — Asisten Orang Tua (KancaScreen)
- Chatbot percakapan yang memberikan informasi berbasis data aktivitas anak nyata
- Sapaan otomatis saat pertama dibuka berdasarkan nama anak
- **3 Quick Reply** yang bisa dipilih orang tua:
  - 🎯 **Target Hari Ini** — status penyelesaian cerita & skor terakhir
  - 📊 **Lihat Perkembangan** — ringkasan: cerita selesai, total XP, level, rata-rata skor
  - 📚 **Rekomendasi Cerita** — saran cerita berikutnya yang belum pernah dimainkan
- Respons dinamis berdasarkan data real dari Supabase

---

## 📖 Daftar Cerita

| File | Judul | Kategori | Skor Maks |
|---|---|---|---|
| `story_kejujuran_ani.json` | Kejujuran Ani | Kejujuran | 70 |
| `story_menabung_zaki.json` | Tabungan Impian Zaki | Menabung | 70 |
| `story_berbagi_rafa.json` | Berbagi Itu Indah | Berbagi | 70 |
| `story_bijak_dina.json` | Dina yang Bijak | Bijak | 70 |
| `story_uang_saku_budi.json` | Uang Saku Budi | Menabung | 70 |

Setiap cerita memiliki **3 scene** dengan **2 pilihan** per scene. Skor total ditentukan dari kombinasi pilihan yang diambil anak.

---

## 🗂️ Struktur Proyek

```
PAMprojectAkhir/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/
│       │   └── stories/
│       │       ├── story_kejujuran_ani.json
│       │       ├── story_menabung_zaki.json
│       │       ├── story_berbagi_rafa.json
│       │       ├── story_bijak_dina.json
│       │       └── story_uang_saku_budi.json
│       ├── res/
│       │   └── drawable-nodpi/
│       │       ├── story_ani.png / story_ani_phase2.png / story_ani_phase3.png
│       │       ├── story_budi.png / story_budi_phase2.png / story_budi_phase3.png
│       │       ├── story_dina.png / story_dina_phase2.png / story_dina_phase3.png
│       │       ├── story_rafa.png / story_rafa_phase2.png / story_rafa_phase3.png
│       │       └── story_zaki.png / story_zaki_phase2.png / story_zaki_phase3.png / story_zaki_family_help.png
│       └── java/com/example/finalprojectpam/
│           ├── MainActivity.kt                    # NavHost & routing semua screen
│           ├── DrawableHelper.kt                  # Helper resource drawable
│           ├── data/
│           │   ├── local/session/
│           │   │   └── SimpleSession.kt           # Manajemen sesi login (SharedPreferences)
│           │   ├── model/
│           │   │   ├── Choice.kt                  # Model pilihan dalam cerita
│           │   │   ├── Scene.kt                   # Model scene cerita
│           │   │   ├── ScoreRecord.kt             # Model rekaman skor
│           │   │   ├── Story.kt                   # Model cerita
│           │   │   └── UserProfile.kt             # Model profil pengguna
│           │   ├── remote/
│           │   │   └── SupabaseClient.kt          # Konfigurasi koneksi Supabase
│           │   └── repository/
│           │       ├── AuthRepository.kt          # Login, register, logout
│           │       ├── StoryRepository.kt         # Muat cerita dari JSON (assets/)
│           │       └── UserRepository.kt          # CRUD profil & skor ke Supabase
│           ├── ui/
│           │   ├── auth/
│           │   │   ├── AuthViewModel.kt
│           │   │   ├── LoginScreen.kt
│           │   │   └── RegisterScreen.kt
│           │   ├── home/
│           │   │   ├── HomeScreen.kt
│           │   │   └── HomeViewModel.kt
│           │   ├── kanca/
│           │   │   ├── KancaScreen.kt
│           │   │   └── KancaViewModel.kt          # ChatMessage, QuickReply, response builder
│           │   ├── langkah/
│           │   │   ├── LangkahScreen.kt
│           │   │   └── LangkahViewModel.kt
│           │   ├── library/
│           │   │   ├── LibraryScreen.kt
│           │   │   └── LibraryViewModel.kt
│           │   ├── navigation/
│           │   │   └── Screen.kt                  # Sealed class semua route
│           │   ├── profile/
│           │   │   ├── ProfileScreen.kt
│           │   │   └── ProfileViewModel.kt
│           │   ├── score/
│           │   │   ├── ScoreScreen.kt
│           │   │   └── ScoreViewModel.kt
│           │   ├── story/
│           │   │   ├── StoryScreen.kt
│           │   │   └── StoryViewModel.kt          # ConsequenceState, loadStory, saveScore
│           │   └── theme/
│           │       ├── Color.kt
│           │       ├── Theme.kt                   # KancaTheme
│           │       └── Type.kt
│           └── utils/
│               └── JsonStoryLoader.kt
```

---

## 🗄️ Skema Database (Supabase / PostgreSQL)

### Tabel `app_users`
| Kolom | Tipe | Keterangan |
|---|---|---|
| `id` | `uuid` | Primary key, dibuat saat register |
| `name` | `text` | Nama anak |
| `email` | `text` | Email untuk login |
| `password` | `text` | Kata sandi |
| `avatar_res` | `text` | Nama resource avatar (default: `avatar_default`) |
| `total_score` | `int4` | Akumulasi total XP dari semua cerita |
| `stories_completed` | `int4` | Jumlah cerita yang sudah selesai |

### Tabel `score_records`
| Kolom | Tipe | Keterangan |
|---|---|---|
| `id` | `uuid` | Primary key (random UUID) |
| `user_id` | `uuid` | Foreign key ke `app_users.id` |
| `story_id` | `text` | ID cerita (contoh: `story_kejujuran_ani`) |
| `story_title` | `text` | Judul cerita |
| `score` | `int4` | Skor yang diperoleh |
| `max_score` | `int4` | Skor maksimum cerita |
| `completed_at` | `timestamp` | Waktu penyelesaian (format UTC ISO 8601) |

---

## 🛠️ Teknologi yang Digunakan

| Kategori | Teknologi | Versi |
|---|---|---|
| Bahasa | Kotlin | — |
| UI Framework | Jetpack Compose | BOM terbaru |
| Arsitektur | MVVM + StateFlow | — |
| Navigasi | Jetpack Navigation Compose | — |
| Backend | Supabase | — |
| Query | supabase-kt / Postgrest | — |
| Auth Supabase | supabase-kt / Auth | — |
| HTTP Client | Ktor Client Android | — |
| Serialisasi | kotlinx.serialization | — |
| Data Lokal | JSON di `assets/` + Gson | — |
| Session | SimpleSession (SharedPreferences) | — |
| Animasi | Compose Animation + InfiniteTransition | — |
| Min SDK | API 24 (Android 7.0) | — |
| Target SDK | API 36 (Android 16) | — |

---

## 🚀 Cara Menjalankan Aplikasi

### Prasyarat
- **Android Studio** Hedgehog (2023.1.1) atau lebih baru
- **JDK 11** atau lebih baru
- Emulator atau perangkat fisik dengan **Android 7.0 (API 24)** ke atas
- Koneksi internet aktif (untuk Supabase)

### Langkah-langkah

```bash
# 1. Clone repositori ini
git clone https://github.com/nailakeisha/PAMprojectAkhir.git

# 2. Buka di Android Studio
# File → Open → pilih folder PAMprojectAkhir

# 3. Tunggu Gradle sync selesai

# 4. Jalankan aplikasi
# Klik tombol Run ▶ atau tekan Shift+F10
```

> **Catatan:** Konfigurasi Supabase sudah tersedia di `data/remote/SupabaseClient.kt`. Tidak perlu konfigurasi tambahan untuk menjalankan aplikasi.

---

## 🎨 Design Tokens

| Token | Hex | Penggunaan |
|---|---|---|
| `BgCream` | `#FBF3E0` | Background utama semua layar |
| `Orange` | `#F5A623` | Warna primer, tombol utama |
| `OrangeDeep` | `#ED8A0F` | Hover/active state, aksen |
| `Brown` | `#5C3A1E` | Teks utama / heading |
| `Brown2` | `#7A4A2A` | Teks sekunder |
| `TextSoft` | `#8A6A4F` | Label, subtitle |
| `Muted` | `#B89A7C` | Ikon non-aktif, divider |

---

## 👨‍👩‍👧‍👦 Tim Pengembang

Dikembangkan sebagai **Final Project** mata kuliah Pemrograman Aplikasi Mobile (PAM)  
**Universitas Brawijaya** — Mei 2026

| Nama | NIM | Github |
|---|---|---|
| Ahmad Muflih Azhari | 245150701111030 | [@kazariii](https://github.com/kazariii) |
| Sefina Ayudia Syauqi | 2451507071110 | [@sefinay](https://github.com/sefinaay) |
| Naila Keisha Sasongko | 245150707111054 | [@nailakeisha](https://github.com/nailakeisha) |
| Oase Bimasena Ilhamaziiz | 245150707111059 | [@wadedogs](https://github.com/wadedogs) |


---

## 📄 Lisensi

Proyek ini dibuat untuk keperluan akademis.  
