package com.example.quanly.repository;

import com.example.quanly.model.GheNgoi;
import com.example.quanly.model.PhienDatGhe;
import com.example.quanly.model.Phong;
import com.example.quanly.model.TrangThai;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PhienDatGheRepository extends JpaRepository<PhienDatGhe, Long> {
    Optional<PhienDatGhe> findFirstByGheNgoiAndTrangThaiAndThoiGianHetHanAfter(GheNgoi gheNgoi, TrangThai trangThai, LocalDateTime thoiGianHetHan);
    boolean existsByGheNgoiAndTrangThaiAndThoiGianHetHanAfter(GheNgoi gheNgoi, TrangThai trangThai, LocalDateTime thoiGianHetHan);
    boolean existsByGheNgoiAndTrangThai(GheNgoi gheNgoi, TrangThai trangThai);
    Optional<PhienDatGhe> findFirstByGheNgoiAndTrangThai(GheNgoi gheNgoi, TrangThai trangThai);
    Long countByGheNgoi_PhongAndTrangThai(Phong phong, TrangThai trangThai);
    Long countByGheNgoi_PhongAndTrangThaiAndThoiGianHetHanAfter(Phong phong, TrangThai trangThai, LocalDateTime thoiGian);
}
