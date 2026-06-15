package com.karirku.service;

import com.karirku.model.Lowongan;
import com.karirku.repository.LowonganRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LowonganService {

    private final LowonganRepository lowonganRepository;

    public LowonganService(LowonganRepository lowonganRepository) {
        this.lowonganRepository = lowonganRepository;
    }

    public List<Lowongan> getAllLowongan() {
        return lowonganRepository.findByAktifTrueOrderByTanggalDibuatDesc();
    }

    public Optional<Lowongan> getLowonganById(Long id) {
        return lowonganRepository.findById(id);
    }

    public List<Lowongan> cariLowongan(String keyword, String lokasi,
                                        String jenis, String kategori, Boolean remote) {
        Lowongan.JenisPekerjaan jenisEnum = null;
        if (jenis != null && !jenis.isBlank()) {
            try {
                jenisEnum = Lowongan.JenisPekerjaan.valueOf(jenis.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        Lowongan.Kategori kategoriEnum = null;
        if (kategori != null && !kategori.isBlank()) {
            try {
                kategoriEnum = Lowongan.Kategori.valueOf(kategori.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        String kw = (keyword == null || keyword.isBlank()) ? null : keyword;
        String lok = (lokasi == null || lokasi.isBlank()) ? null : lokasi;

        return lowonganRepository.cariLowongan(kw, lok, jenisEnum, kategoriEnum, remote);
    }

    public Lowongan tambahLowongan(Lowongan lowongan) {
        return lowonganRepository.save(lowongan);
    }

    public Optional<Lowongan> updateLowongan(Long id, Lowongan data) {
        return lowonganRepository.findById(id).map(existing -> {
            existing.setJudul(data.getJudul());
            existing.setPerusahaan(data.getPerusahaan());
            existing.setLokasi(data.getLokasi());
            existing.setJenis(data.getJenis());
            existing.setKategori(data.getKategori());
            existing.setGajiMin(data.getGajiMin());
            existing.setGajiMax(data.getGajiMax());
            existing.setRemote(data.isRemote());
            existing.setDeskripsi(data.getDeskripsi());
            existing.setKualifikasi(data.getKualifikasi());
            return lowonganRepository.save(existing);
        });
    }

    public boolean hapusLowongan(Long id) {
        return lowonganRepository.findById(id).map(l -> {
            l.setAktif(false);
            lowonganRepository.save(l);
            return true;
        }).orElse(false);
    }

    public long totalLowonganAktif() {
        return lowonganRepository.countByAktifTrue();
    }
}
