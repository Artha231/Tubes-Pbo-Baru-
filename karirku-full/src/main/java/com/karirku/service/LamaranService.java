package com.karirku.service;

import com.karirku.model.Lamaran;
import com.karirku.model.Lowongan;
import com.karirku.repository.LamaranRepository;
import com.karirku.repository.LowonganRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LamaranService {

    private final LamaranRepository lamaranRepository;
    private final LowonganRepository lowonganRepository;

    public LamaranService(LamaranRepository lamaranRepository,
                          LowonganRepository lowonganRepository) {
        this.lamaranRepository = lamaranRepository;
        this.lowonganRepository = lowonganRepository;
    }

    public Lamaran kirimLamaran(Long lowonganId, Lamaran lamaran) {
        Lowongan lowongan = lowonganRepository.findById(lowonganId)
                .orElseThrow(() -> new RuntimeException("Lowongan tidak ditemukan: " + lowonganId));

        if (!lowongan.isAktif()) {
            throw new RuntimeException("Lowongan ini sudah tidak aktif");
        }

        if (lamaranRepository.existsByEmailAndLowonganId(lamaran.getEmail(), lowonganId)) {
            throw new RuntimeException("Kamu sudah pernah melamar posisi ini sebelumnya");
        }

        lamaran.setLowongan(lowongan);
        lamaran.setStatus(Lamaran.StatusLamaran.MENUNGGU);
        return lamaranRepository.save(lamaran);
    }

    public List<Lamaran> getLamaranByLowongan(Long lowonganId) {
        return lamaranRepository.findByLowonganIdOrderByTanggalDibuatDesc(lowonganId);
    }

    public Optional<Lamaran> updateStatus(Long lamaranId, Lamaran.StatusLamaran status) {
        return lamaranRepository.findById(lamaranId).map(l -> {
            l.setStatus(status);
            return lamaranRepository.save(l);
        });
    }

    public long totalPelamar(Long lowonganId) {
        return lamaranRepository.countByLowonganId(lowonganId);
    }
}
