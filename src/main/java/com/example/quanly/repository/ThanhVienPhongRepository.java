package com.example.quanly.repository;

import com.example.quanly.model.Phong;
import com.example.quanly.model.ThanhVienPhong;
import com.example.quanly.model.User;
import com.example.quanly.model.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThanhVienPhongRepository extends JpaRepository<ThanhVienPhong, Long> {
    boolean existsByPhongAndUserAndVaiTroIn(Phong phong, User user, List<VaiTro> vaiTro);
    boolean existsByPhongAndUserAndVaiTro(Phong phong, User user, VaiTro vaiTro);
    Optional<ThanhVienPhong> findByUserAndPhong(User user, Phong phong);
}
