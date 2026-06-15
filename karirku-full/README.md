# KarirKu Backend — Spring Boot REST API

Portal lowongan pekerjaan sederhana dengan Java Spring Boot 3 + H2/PostgreSQL.

---

## Cara Menjalankan

### Prasyarat
- Java 17+ (cek: `java -version`)
- Maven 3.6+ (cek: `mvn -version`)

### Jalankan (development — pakai H2 in-memory, tidak perlu install database)
```bash
cd karirku-backend
mvn spring-boot:run
```

Server jalan di: **http://localhost:8080**
H2 Console:      **http://localhost:8080/h2-console**
  - JDBC URL: `jdbc:h2:mem:karirku_db`
  - User: `sa` | Password: (kosong)

---

## Endpoint API

### Lowongan

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| GET | `/api/lowongan` | Semua lowongan aktif |
| GET | `/api/lowongan/{id}` | Detail satu lowongan |
| GET | `/api/lowongan/cari` | Cari & filter lowongan |
| GET | `/api/lowongan/stats` | Statistik total lowongan |
| POST | `/api/lowongan` | Tambah lowongan baru |
| PUT | `/api/lowongan/{id}` | Update lowongan |
| DELETE | `/api/lowongan/{id}` | Nonaktifkan lowongan (soft delete) |

### Lamaran

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| POST | `/api/lowongan/{id}/lamar` | Kirim lamaran |
| GET | `/api/lowongan/{id}/lamaran` | Lihat semua lamaran (admin) |
| GET | `/api/lowongan/{id}/lamaran/count` | Jumlah pelamar |
| PATCH | `/api/lamaran/{id}/status` | Update status lamaran |

---

## Contoh Request & Response

### 1. Ambil Semua Lowongan
```
GET http://localhost:8080/api/lowongan
```

### 2. Cari Lowongan
```
GET http://localhost:8080/api/lowongan/cari?keyword=java&lokasi=jakarta&jenis=FULL_TIME&remote=false
```
Parameter opsional: `keyword`, `lokasi`, `jenis`, `kategori`, `remote`

### 3. Tambah Lowongan
```http
POST http://localhost:8080/api/lowongan
Content-Type: application/json

{
  "judul": "Backend Developer",
  "perusahaan": "PT Maju Bersama",
  "lokasi": "Jakarta",
  "jenis": "FULL_TIME",
  "kategori": "TEKNOLOGI",
  "gajiMin": "8000000",
  "gajiMax": "12000000",
  "remote": false,
  "deskripsi": "Mengembangkan API dengan Spring Boot",
  "kualifikasi": "Min. 2 tahun pengalaman Java"
}
```

### 4. Kirim Lamaran
```http
POST http://localhost:8080/api/lowongan/1/lamar
Content-Type: application/json

{
  "namaLengkap": "Budi Santoso",
  "email": "budi@email.com",
  "noTelepon": "0812-3456-7890",
  "coverLetter": "Saya tertarik melamar posisi ini karena...",
  "linkPortfolio": "https://github.com/budi"
}
```

### 5. Update Status Lamaran
```http
PATCH http://localhost:8080/api/lamaran/1/status
Content-Type: application/json

{ "status": "DITERIMA" }
```

---

## Enum yang Tersedia

**JenisPekerjaan:** `FULL_TIME` `PART_TIME` `FREELANCE` `INTERNSHIP`

**Kategori:** `TEKNOLOGI` `DESAIN` `MARKETING` `KEUANGAN` `OPERASIONAL` `LAINNYA`

**StatusLamaran:** `MENUNGGU` `DITINJAU` `DITERIMA` `DITOLAK`

---

## Ganti ke PostgreSQL (Produksi)

Edit `src/main/resources/application.properties`, comment baris H2 dan uncomment baris PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/karirku_db
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=rahasia
spring.jpa.hibernate.ddl-auto=update
```

Lalu buat database di PostgreSQL:
```sql
CREATE DATABASE karirku_db;
```

---

## Struktur Project

```
src/main/java/com/karirku/
├── KarirkuApplication.java       ← Entry point + data seeder
├── model/
│   ├── Lowongan.java             ← Entity lowongan pekerjaan
│   └── Lamaran.java              ← Entity lamaran pelamar
├── repository/
│   ├── LowonganRepository.java   ← Query database lowongan
│   └── LamaranRepository.java    ← Query database lamaran
├── service/
│   ├── LowonganService.java      ← Logika bisnis lowongan
│   └── LamaranService.java       ← Logika bisnis lamaran
├── controller/
│   ├── LowonganController.java   ← REST endpoint lowongan
│   └── LamaranController.java    ← REST endpoint lamaran
└── config/
    └── CorsConfig.java           ← Konfigurasi CORS
```
