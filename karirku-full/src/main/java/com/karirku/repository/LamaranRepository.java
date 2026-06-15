package com.karirku.repository;

import com.karirku.model.Lamaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LamaranRepository extends JpaRepository<Lamaran, Long> {

    List<Lamaran> findByLowonganIdOrderByTanggalDibuatDesc(Long lowonganId);

    boolean existsByEmailAndLowonganId(String email, Long lowonganId);

    long countByLowonganId(Long lowonganId);
}
