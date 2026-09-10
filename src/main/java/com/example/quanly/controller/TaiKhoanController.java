package com.example.quanly.controller;

import com.example.quanly.config.JwtUtils;
import com.example.quanly.dto.DangKyTaiKhoan.DangKyTaiKhoanRequest;
import com.example.quanly.dto.DangKyTaiKhoan.DangKyTaiKhoanResponse;
import com.example.quanly.dto.DangNhapTaiKhoan.DangNhapTaiKhoanRequest;
import com.example.quanly.dto.DangNhapTaiKhoan.DangNhapTaiKhoanResponse;
import com.example.quanly.dto.DangNhapTaiKhoan.DangXuatTaiKhoanResponse;
import com.example.quanly.service.TaiKhoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tai-khoan")
@CrossOrigin(origins = "*")
public class TaiKhoanController {
    private final TaiKhoanService taiKhoanService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public TaiKhoanController(TaiKhoanService taiKhoanService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.taiKhoanService = taiKhoanService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/tao-tai-khoan")
    public ResponseEntity<DangKyTaiKhoanResponse> dangKyTaiKhoan(@Valid @RequestBody DangKyTaiKhoanRequest taiKhoan) {
        DangKyTaiKhoanResponse dangKyTaiKhoanResponse = taiKhoanService.dangKyTaiKhoan(taiKhoan.getUsername(), taiKhoan.getEmail(), taiKhoan.getPassword());
        return ResponseEntity.ok(dangKyTaiKhoanResponse);
    }

    @PostMapping("/dang-nhap")
    public ResponseEntity<DangNhapTaiKhoanResponse> authenticateUser(@Valid @RequestBody DangNhapTaiKhoanRequest dangNhapTaiKhoanRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dangNhapTaiKhoanRequest.getUsername(),
                        dangNhapTaiKhoanRequest.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtUtils.generateToken(userDetails.getUsername());

        ResponseCookie jwtCookie = ResponseCookie.from("jwt-cookie", token)
            .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24*60*60)
                .build();
        DangNhapTaiKhoanResponse dangNhapTaiKhoanResponse = new DangNhapTaiKhoanResponse("Đăng nhập thành công!", userDetails.getUsername());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(dangNhapTaiKhoanResponse);
    }

    @PostMapping("/dang-xuat")
    public ResponseEntity<DangXuatTaiKhoanResponse> dangXuat() {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt-cookie", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
        DangXuatTaiKhoanResponse dangXuatTaiKhoanResponse = new DangXuatTaiKhoanResponse("Đăng xuất thành công!");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(dangXuatTaiKhoanResponse);

    }

}
