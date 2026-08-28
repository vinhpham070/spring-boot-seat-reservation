package com.example.quanly.controller;

import com.example.quanly.config.JwtUtils;
import com.example.quanly.dto.DangKyTaiKhoan.DangKyTaiKhoanRequest;
import com.example.quanly.dto.DangKyTaiKhoan.DangKyTaiKhoanResponse;
import com.example.quanly.dto.DangNhapTaiKhoan.DangNhapTaiKhoanRequest;
import com.example.quanly.dto.JwtResponse;
import com.example.quanly.service.TaiKhoanService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tai-khoan")
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
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody DangNhapTaiKhoanRequest dangNhapTaiKhoanRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dangNhapTaiKhoanRequest.getUsername(),
                        dangNhapTaiKhoanRequest.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtils.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

}
