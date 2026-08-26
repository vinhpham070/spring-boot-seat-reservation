package com.example.quanly;

import com.example.quanly.model.GheNgoi;
import com.example.quanly.model.Phong;
import com.example.quanly.model.User;
import com.example.quanly.repository.GheNgoiRepository;
import com.example.quanly.repository.PhongRepository;
import com.example.quanly.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               PhongRepository phongRepository,
                               GheNgoiRepository gheNgoiRepository) {
        return args -> {
            System.out.println("====== BẮT ĐẦU NẠP DỮ LIỆU MỒI (H2) ======");

            // Chỉ nạp khi DB trống để tránh trùng lặp
            if (userRepository.count() == 0) {
                // 1. Tạo 5 User
                User u1 = userRepository.save(new User("Nguyen Van A", "a@example.com", "123456789"));
                User u2 = userRepository.save(new User("Tran Van B", "b@example.com", "123456789"));
                User u3 = userRepository.save(new User("Le Thi C", "c@example.com", "123456789"));
                User u4 = userRepository.save(new User("Pham Van D", "d@example.com", "123456789"));
                User u5 = userRepository.save(new User("Vo Thi E", "e@example.com", "123456789"));
                System.out.println(">> Đã tạo 5 User.");

                // 2. Tạo 1 Phòng (Lấy User 1 làm chủ phòng)
                Phong phong = new Phong();
                phong.setTenPhong("Phòng VIP 1");
                phong.setSoHang(1);
                phong.setSoCot(5);
                phong.setChuPhong(u1);
                Phong savedPhong = phongRepository.save(phong);
                System.out.println(">> Đã tạo Phòng ID: " + savedPhong.getId());

                // 3. Tạo 5 Ghế và gán vào Phòng vừa tạo
                GheNgoi g1 = new GheNgoi("A1", 1L, 1L); g1.setPhong(savedPhong);
                GheNgoi g2 = new GheNgoi("A2", 1L, 2L); g2.setPhong(savedPhong);
                GheNgoi g3 = new GheNgoi("A3", 1L, 3L); g3.setPhong(savedPhong);
                GheNgoi g4 = new GheNgoi("A4", 1L, 4L); g4.setPhong(savedPhong);
                GheNgoi g5 = new GheNgoi("A5", 1L, 5L); g5.setPhong(savedPhong);

                gheNgoiRepository.saveAll(List.of(g1, g2, g3, g4, g5));
                System.out.println(">> Đã tạo 5 Ghế cho Phòng 1.");
            }

            System.out.println("====== NẠP DỮ LIỆU HOÀN TẤT ======");
        };
    }
}