package com.example.quanly;

import com.example.quanly.model.GheNgoi;
import com.example.quanly.model.Phong;
import com.example.quanly.model.User;
import com.example.quanly.repository.GheNgoiRepository;
import com.example.quanly.repository.PhongRepository;
import com.example.quanly.repository.UserRepository;
import com.example.quanly.service.PhongService;
import com.example.quanly.service.TaiKhoanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               PhongRepository phongRepository,
                               GheNgoiRepository gheNgoiRepository,
                               PhongService phongService,
                               TaiKhoanService taiKhoanService) {
        return args -> {
            System.out.println("====== BẮT ĐẦU NẠP DỮ LIỆU MỒI (H2) ======");

            // Chỉ nạp khi DB trống để tránh trùng lặp
            if (userRepository.count() == 0) {
                // 1. Tạo 5 User
                taiKhoanService.dangKyTaiKhoan("NguyenVanA", "a@example.com", "123456789");
                taiKhoanService.dangKyTaiKhoan("TranVanB", "b@example.com", "123456789");
                taiKhoanService.dangKyTaiKhoan("LeThiC", "c@example.com", "123456789");
                taiKhoanService.dangKyTaiKhoan("PhamVanD", "d@example.com", "123456789");
                taiKhoanService.dangKyTaiKhoan("VoThiE", "e@example.com", "123456789");
                System.out.println(">> Đã tạo 5 User.");

                // 2. Tạo 1 Phòng (Lấy User 1 làm chủ phòng)
                phongService.taoPhong("Mahattan meeting", 5L, 1L, "NguyenVanA" );

                // 3. Tạo 5 Ghế và gán vào Phòng vừa tạo


                System.out.println(">> Đã tạo 5 Ghế cho Phòng 1.");
            }

            System.out.println("====== NẠP DỮ LIỆU HOÀN TẤT ======");
        };
    }
}