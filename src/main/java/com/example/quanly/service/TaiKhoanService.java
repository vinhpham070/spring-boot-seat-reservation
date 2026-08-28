package com.example.quanly.service;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.quanly.dto.DangKyTaiKhoan.DangKyTaiKhoanResponse;
import com.example.quanly.exception.BadRequestException;
import com.example.quanly.model.User;
import com.example.quanly.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TaiKhoanService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TaiKhoanService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public DangKyTaiKhoanResponse dangKyTaiKhoan(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) { throw new BadRequestException("Tên đã tồn tại!"); }
        password = passwordEncoder.encode(password);
        User user = new User(username, email, password);
        userRepository.save(user);
        DangKyTaiKhoanResponse dangKyTaiKhoanResponse = new DangKyTaiKhoanResponse(username, email);
        return dangKyTaiKhoanResponse;

    }

}
