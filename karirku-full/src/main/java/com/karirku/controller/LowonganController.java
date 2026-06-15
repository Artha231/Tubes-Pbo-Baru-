package com.karirku.controller;

import com.karirku.model.Lowongan;
import com.karirku.service.LowonganService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lowongan")
@CrossOrigin(origins = "*")
public class LowonganController {

    private final LowonganService lowonganService;

    public LowonganController(LowonganService lowonganService) {
        this.lowonganService = lowonganService;
    }

    /**
     * GET /api/lowongan
     * Ambil semua lowongan aktif
     */
    @GetMapping
    public ResponseEntity<List<Lowongan>> getAllLowongan() {
        return ResponseEntity.ok(lowonganService.getAllLowongan());
    }

    /**
     * GET /api/lowongan/{id}
     * Detail satu lowongan
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getLowonganById(@PathVariable Long id) {
        return lowonganService.getLowonganById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/lowongan/cari?keyword=java&lokasi=jakarta&jenis=FULL_TIME&kategori=TEKNOLOGI&remote=false
     * Cari dan filter lowongan
     */
    @GetMapping("/cari")
    public ResponseEntity<List<Lowongan>> cariLowongan(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String lokasi,
            @RequestParam(required = false) String jenis,
            @RequestParam(required = false) String kategori,
            @RequestParam(required = false) Boolean remote) {
        List<Lowongan> hasil = lowonganService.cariLowongan(keyword, lokasi, jenis, kategori, remote);
        return ResponseEntity.ok(hasil);
    }

    /**
     * POST /api/lowongan
     * Tambah lowongan baru
     */
    @PostMapping
    public ResponseEntity<Lowongan> tambahLowongan(@Valid @RequestBody Lowongan lowongan) {
        Lowongan baru = lowonganService.tambahLowongan(lowongan);
        return ResponseEntity.status(HttpStatus.CREATED).body(baru);
    }

    /**
     * PUT /api/lowongan/{id}
     * Update lowongan
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLowongan(@PathVariable Long id,
                                             @Valid @RequestBody Lowongan lowongan) {
        return lowonganService.updateLowongan(id, lowongan)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/lowongan/{id}
     * Nonaktifkan lowongan (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> hapusLowongan(@PathVariable Long id) {
        boolean berhasil = lowonganService.hapusLowongan(id);
        if (berhasil) {
            return ResponseEntity.ok(Map.of("pesan", "Lowongan berhasil dinonaktifkan"));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/lowongan/stats
     * Statistik
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(Map.of(
                "totalLowonganAktif", lowonganService.totalLowonganAktif()
        ));
    }
}
