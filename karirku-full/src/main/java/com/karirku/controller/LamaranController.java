package com.karirku.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karirku.model.Lamaran;
import com.karirku.service.LamaranService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LamaranController {

    private final LamaranService lamaranService;

    public LamaranController(LamaranService lamaranService) {
        this.lamaranService = lamaranService;
    }

    /**
     * POST /api/lowongan/{lowonganId}/lamar
     * Kirim lamaran untuk sebuah lowongan
     */
    @PostMapping("/lowongan/{lowonganId}/la mar")
    public ResponseEntity<?> kirimLamaran(@PathVariable Long lowonganId,
                                           @Valid @RequestBody Lamaran lamaran) {
        try {
            Lamaran hasil = lamaranService.kirimLamaran(lowonganId, lamaran);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "pesan", "Lamaran berhasil dikirim! Tim rekrutmen akan menghubungi kamu.",
                    "lamaranId", hasil.getId(),
                    "status", hasil.getStatus().name()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/lowongan/{lowonganId}/lamaran
     * Lihat semua lamaran untuk satu lowongan (untuk admin)
     */
    @GetMapping("/lowongan/{lowonganId}/lamaran")
    public ResponseEntity<List<Lamaran>> getLamaranByLowongan(@PathVariable Long lowonganId) {
        return ResponseEntity.ok(lamaranService.getLamaranByLowongan(lowonganId));
    }

    /**
     * PATCH /api/lamaran/{id}/status
     * Update status lamaran
     * Body: { "status": "DITERIMA" }
     */
    @PatchMapping("/lamaran/{id}/status")
    public ResponseEntity<?> updateStatusLamaran(@PathVariable Long id,
                                                  @RequestBody Map<String, String> body) {
        try {
            String statusStr = body.get("status");
            if (statusStr == null || statusStr.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Field 'status' wajib diisi"));
            }
            Lamaran.StatusLamaran status = Lamaran.StatusLamaran.valueOf(statusStr.toUpperCase());
            return lamaranService.updateStatus(id, status)
                    .map(l -> ResponseEntity.ok(Map.of(
                            "pesan", "Status lamaran berhasil diperbarui",
                            "status", l.getStatus().name()
                    )))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Status tidak valid. Pilihan: MENUNGGU, DITINJAU, DITERIMA, DITOLAK")
            );
        }
    }

    /**
     * GET /api/lowongan/{lowonganId}/lamaran/count
     * Jumlah pelamar untuk satu lowongan
     */
    @GetMapping("/lowongan/{lowonganId}/lamaran/count")
    public ResponseEntity<Map<String, Object>> countPelamar(@PathVariable Long lowonganId) {
        return ResponseEntity.ok(Map.of(
                "lowonganId", lowonganId,
                "totalPelamar", lamaranService.totalPelamar(lowonganId)
        ));
    }
}
