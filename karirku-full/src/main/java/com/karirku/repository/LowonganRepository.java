package com.karirku.repository;

import com.karirku.model.Lowongan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LowonganRepository extends JpaRepository<Lowongan, Long> {

    List<Lowongan> findByAktifTrueOrderByTanggalDibuatDesc();

    @Query("SELECT l FROM Lowongan l WHERE l.aktif = true AND " +
           "(:keyword IS NULL OR LOWER(l.judul) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.perusahaan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.deskripsi) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:lokasi IS NULL OR LOWER(l.lokasi) LIKE LOWER(CONCAT('%', :lokasi, '%'))) AND " +
           "(:jenis IS NULL OR l.jenis = :jenis) AND " +
           "(:kategori IS NULL OR l.kategori = :kategori) AND " +
           "(:remote IS NULL OR l.remote = :remote) " +
           "ORDER BY l.featured DESC, l.tanggalDibuat DESC")
    List<Lowongan> cariLowongan(
            @Param("keyword") String keyword,
            @Param("lokasi") String lokasi,
            @Param("jenis") Lowongan.JenisPekerjaan jenis,
            @Param("kategori") Lowongan.Kategori kategori,
            @Param("remote") Boolean remote
    );

    long countByAktifTrue();
}
