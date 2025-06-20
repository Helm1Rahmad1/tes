# Collect The Magical Gems

Sebuah game desktop bertema fantasi sihir modern yang dibangun dengan Java Swing, menampilkan desain UI canggih dan mekanisme gameplay yang menarik.

## Janji

Saya Muhammad Helmi Rahmadi dengan NIM 2311574 mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

## Fitur

### Gameplay
- **Kontrol Karakter**: Navigasi dan bidik karakter Anda secara strategis.
- **Mekanisme Lasso**: Tembakkan lasso untuk mengumpulkan bola skill.
- **Penghindaran Rintangan**: Hindari permata hitam berbahaya (bom).
- **Sistem Penilaian**: Dapatkan poin dengan mengumpulkan bola skill.

### Audio
**Musik Latar Belakang**: Musik yang imersif untuk menu dan gameplay.

## Kredit Aset
* **Aset Karakter** - [https://opengameart.org/content/witchmagicianmagemagi#](https://opengameart.org/content/witchmagicianmagemagi#)
* **Aset Bola Kristal** - [https://opengameart.org/content/gem-jewel-diamond-glass](https://opengameart.org/content/gem-jewel-diamond-glass)
* **Aset Latar Belakang** - [https://opengameart.org/content/fantasy-background](https://opengameart.org/content/fantasy-background)
* **Aset Musik Menu** - [https://opengameart.org/content/magic-space](https://opengameart.org/content/magic-space)
* **Aset Musik Game** - [https://opengameart.org/content/mystical-caverns](https://opengameart.org/content/mystical-caverns)

## Arsitektur

Proyek ini mengimplementasikan pola arsitektur **Model-View-ViewModel (MVVM)** untuk pemisahan tanggung jawab yang jelas:

-   **Model**: Penanganan data dan logika bisnis.
-   **View**: Presentasi antarmuka pengguna.
-   **ViewModel**: Jembatan antara Model dan View, mengelola status UI dan logika presentasi.

## Konfigurasi Database

Aplikasi ini menggunakan database MySQL bernama `game_scores_db`. Pada saat pertama kali dijalankan, aplikasi akan secara otomatis:

1.  Membuat database jika belum ada.
2.  Membuat tabel `thasil` dengan skema berikut:
    -   `username` (VARCHAR)
    -   `skor` (INT)
    -   `count` (INT)
    -   `created_at` (TIMESTAMP)
    -   `updated_at` (TIMESTAMP)
3.  Menyisipkan data contoh jika tabel kosong.

### Pengaturan Koneksi Database
Konfigurasi default (modifikasi di `src/model/Database.java` jika diperlukan):
-   **Host**: `localhost`
-   **Port**: `3306`
-   **Pengguna**: `root`
-   **Kata Sandi**: (kosong)
-   **Database**: `game_scores_db`

## Build & Jalankan

### Menggunakan Command Line

1.  **Buat direktori bin**:
    ```bash
    mkdir bin
    ```

2.  **Kompilasi kode sumber**:
    ```bash
    javac -d bin -cp "lib/*" src/**/*.java
    ```

3.  **Salin aset**:
    ```bash
    cp -r src/assets bin/assets
    ```

4.  **Jalankan aplikasi**:
    ```bash
    java -cp "bin:lib/*" main.Main
    ```

5.  **Bersihkan build sebelumnya** (opsional):
    ```bash
    rm -rf bin
    ```


### Skrip Mulai Cepat

```bash
mkdir bin
javac -d bin -cp "lib/*" src/**/*.java
cp -r src/assets bin/assets
java -cp "bin:lib/*" main.Main

rm -rf bin
