package com.karirku;

import com.karirku.model.Lowongan;
import com.karirku.repository.LowonganRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KarirkuApplication {

    public static void main(String[] args) {
        SpringApplication.run(KarirkuApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(LowonganRepository repo) {
        return args -> {
            if (repo.count() > 0) return;

            // --- Lowongan 1 ---
            Lowongan j1 = new Lowongan();
            j1.setJudul("Backend Developer (Java Spring Boot)");
            j1.setPerusahaan("Tokopedia");
            j1.setLokasi("Jakarta");
            j1.setJenis(Lowongan.JenisPekerjaan.FULL_TIME);
            j1.setKategori(Lowongan.Kategori.TEKNOLOGI);
            j1.setGajiMin("12000000");
            j1.setGajiMax("18000000");
            j1.setRemote(false);
            j1.setDeskripsi("Mengembangkan REST API dengan Spring Boot, PostgreSQL, dan microservices. Bertanggung jawab atas performa dan skalabilitas sistem backend.");
            j1.setKualifikasi("Min. 2 tahun pengalaman Java, paham Spring Boot, REST API, PostgreSQL, dan Git.");
            j1.setFeatured(true);
            repo.save(j1);

            // --- Lowongan 2 ---
            Lowongan j2 = new Lowongan();
            j2.setJudul("Frontend Developer (React)");
            j2.setPerusahaan("Bukalapak");
            j2.setLokasi("Bandung");
            j2.setJenis(Lowongan.JenisPekerjaan.FULL_TIME);
            j2.setKategori(Lowongan.Kategori.TEKNOLOGI);
            j2.setGajiMin("8000000");
            j2.setGajiMax("14000000");
            j2.setRemote(true);
            j2.setDeskripsi("Membangun antarmuka pengguna responsif dan performan menggunakan React dan TypeScript.");
            j2.setKualifikasi("Paham TypeScript, Redux, testing (Jest), dan pengalaman min. 1 tahun React.");
            j2.setFeatured(false);
            repo.save(j2);

            // --- Lowongan 3 ---
            Lowongan j3 = new Lowongan();
            j3.setJudul("UI/UX Designer");
            j3.setPerusahaan("Gojek");
            j3.setLokasi("Jakarta");
            j3.setJenis(Lowongan.JenisPekerjaan.FULL_TIME);
            j3.setKategori(Lowongan.Kategori.DESAIN);
            j3.setGajiMin("10000000");
            j3.setGajiMax("15000000");
            j3.setRemote(false);
            j3.setDeskripsi("Merancang pengalaman pengguna untuk aplikasi mobile dan web Gojek yang digunakan jutaan orang.");
            j3.setKualifikasi("Mahir Figma, pengalaman riset pengguna, portofolio desain mobile, min. 2 tahun.");
            j3.setFeatured(false);
            repo.save(j3);

            // --- Lowongan 4 ---
            Lowongan j4 = new Lowongan();
            j4.setJudul("Digital Marketing Specialist");
            j4.setPerusahaan("Shopee Indonesia");
            j4.setLokasi("Jakarta");
            j4.setJenis(Lowongan.JenisPekerjaan.FULL_TIME);
            j4.setKategori(Lowongan.Kategori.MARKETING);
            j4.setGajiMin("7000000");
            j4.setGajiMax("11000000");
            j4.setRemote(false);
            j4.setDeskripsi("Mengelola dan mengoptimalkan kampanye digital di Google Ads, Meta Ads, dan TikTok Ads.");
            j4.setKualifikasi("Pengalaman Google Ads, Meta Ads, analitik digital min. 1 tahun, sertifikasi Google Ads diutamakan.");
            j4.setFeatured(false);
            repo.save(j4);

            // --- Lowongan 5 ---
            Lowongan j5 = new Lowongan();
            j5.setJudul("Data Analyst");
            j5.setPerusahaan("Traveloka");
            j5.setLokasi("Jakarta");
            j5.setJenis(Lowongan.JenisPekerjaan.FULL_TIME);
            j5.setKategori(Lowongan.Kategori.TEKNOLOGI);
            j5.setGajiMin("9000000");
            j5.setGajiMax("14000000");
            j5.setRemote(true);
            j5.setDeskripsi("Menganalisis data bisnis, membangun dashboard laporan, dan memberikan insight berbasis data kepada tim produk.");
            j5.setKualifikasi("Paham SQL, Python (Pandas), Tableau atau Metabase, dan statistik dasar.");
            j5.setFeatured(false);
            repo.save(j5);

            System.out.println("✅ KarirKu: 5 data lowongan berhasil dimuat.");
            System.out.println("✅ Server berjalan di: http://localhost:8080");
            System.out.println("✅ H2 Console: http://localhost:8080/h2-console");
            System.out.println("✅ API Docs: http://localhost:8080/api/lowongan");
        };
    }
}
