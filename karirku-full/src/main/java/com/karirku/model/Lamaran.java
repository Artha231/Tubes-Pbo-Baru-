package com.karirku.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "lamaran")
public class Lamaran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lowongan_id", nullable = false)
    private Lowongan lowongan;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Column(nullable = false)
    private String namaLengkap;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Column(nullable = false)
    private String email;

    private String noTelepon;

    @Column(length = 2000)
    private String coverLetter;

    private String linkPortfolio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLamaran status = StatusLamaran.MENUNGGU;

    @Column(nullable = false)
    private LocalDateTime tanggalDibuat;

    @PrePersist
    protected void onCreate() {
        this.tanggalDibuat = LocalDateTime.now();
    }

    public enum StatusLamaran {
        MENUNGGU, DITINJAU, DITERIMA, DITOLAK
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Lowongan getLowongan() { return lowongan; }
    public void setLowongan(Lowongan lowongan) { this.lowongan = lowongan; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public String getLinkPortfolio() { return linkPortfolio; }
    public void setLinkPortfolio(String linkPortfolio) { this.linkPortfolio = linkPortfolio; }

    public StatusLamaran getStatus() { return status; }
    public void setStatus(StatusLamaran status) { this.status = status; }

    public LocalDateTime getTanggalDibuat() { return tanggalDibuat; }
    public void setTanggalDibuat(LocalDateTime tanggalDibuat) { this.tanggalDibuat = tanggalDibuat; }
}
