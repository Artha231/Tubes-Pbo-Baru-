# KarirKu — Frontend

Website portal lowongan pekerjaan yang terhubung ke backend Spring Boot.

## Cara Pakai

### 1. Jalankan Backend dulu
```bash
cd karirku-backend
mvn spring-boot:run
# Pastikan berjalan di http://localhost:8080
```

### 2. Buka Frontend
Buka file `index.html` langsung di browser, **atau** pakai live server:

```bash
# Dengan VS Code Live Server — klik kanan index.html → Open with Live Server

# Atau dengan Python:
python -m http.server 5500
# Buka: http://localhost:5500
```

> ⚠️ Jika buka langsung via `file://`, browser mungkin blokir CORS.
> Gunakan Live Server atau Python HTTP server.

## Fitur

| Fitur | Keterangan |
|-------|-----------|
| Daftar lowongan | Fetch dari `GET /api/lowongan` |
| Cari & filter | Filter jenis, kategori, remote, kata kunci, lokasi |
| Lamar pekerjaan | `POST /api/lowongan/{id}/lamar` |
| Pasang lowongan | `POST /api/lowongan` |
| Loading skeleton | Tampilan loading elegan saat fetch |
| Error banner | Pesan jelas jika backend tidak berjalan |
| Toast notifikasi | Konfirmasi sukses/gagal |
| Responsive | Mobile-friendly |

## Struktur File

```
karirku-frontend/
├── index.html    ← Halaman utama
├── style.css     ← Semua styling
├── app.js        ← Logic fetch API & interaksi
└── README.md
```

## Ganti URL Backend

Jika backend berjalan di port atau host berbeda, edit baris pertama di `app.js`:

```javascript
const API = 'http://localhost:8080/api';
// Ganti sesuai host/port backend kamu
```
