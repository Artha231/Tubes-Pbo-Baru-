package com.karirku.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "lowongan")
public class Lowongan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Judul posisi tidak boleh kosong")
    @Column(nullable = false)
    private String judul;

    @NotBlank(message = "Nama perusahaan tidak boleh kosong")
    @Column(nullable = false)
    private String perusahaan;

    @Column(nullable = false)
    private String lokasi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JenisPekerjaan jenis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Kategori kategori;

    private String gajiMin;
    private String gajiMax;

    @Column(nullable = false)
    private boolean remote = false;

    @Column(length = 2000)
    private String deskripsi;

    @Column(length = 1000)
    private String kualifikasi;

    @Column(nullable = false)
    private LocalDateTime tanggalDibuat;

    @Column(nullable = false)
    private boolean aktif = true;

    @Column(nullable = false)
    private boolean featured = false;

    @PrePersist
    protected void onCreate() {
        this.tanggalDibuat = LocalDateTime.now();
    }

    public enum JenisPekerjaan {
        FULL_TIME, PART_TIME, FREELANCE, INTERNSHIP
    }

    public enum Kategori {
        TEKNOLOGI, DESAIN, MARKETING, KEUANGAN, OPERASIONAL, LAINNYA
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getPerusahaan() { return perusahaan; }
    public void setPerusahaan(String perusahaan) { this.perusahaan = perusahaan; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public JenisPekerjaan getJenis() { return jenis; }
    public void setJenis(JenisPekerjaan jenis) { this.jenis = jenis; }

    public Kategori getKategori() { return kategori; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }

    public String getGajiMin() { return gajiMin; }
    public void setGajiMin(String gajiMin) { this.gajiMin = gajiMin; }

    public String getGajiMax() { return gajiMax; }
    public void setGajiMax(String gajiMax) { this.gajiMax = gajiMax; }

    public boolean isRemote() { return remote; }
    public void setRemote(boolean remote) { this.remote = remote; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getKualifikasi() { return kualifikasi; }
    public void setKualifikasi(String kualifikasi) { this.kualifikasi = kualifikasi; }

    public LocalDateTime getTanggalDibuat() { return tanggalDibuat; }
    public void setTanggalDibuat(LocalDateTime v) { this.tanggalDibuat = v; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
}
