package com.example.quanly.controller;

import com.example.quanly.dto.DangKyTaiKhoanRequest;
import com.example.quanly.dto.DangKyTaiKhoanResponse;
import com.example.quanly.service.TaiKhoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tai-khoan")
public class TaiKhoanController {
    private final TaiKhoanService taiKhoanService;

    public TaiKhoanController(TaiKhoanService taiKhoanService) {
        this.taiKhoanService = taiKhoanService;
    }

    @PostMapping("/tao-tai-khoan")
    public ResponseEntity<DangKyTaiKhoanResponse> dangKyTaiKhoan(@Valid @RequestBody DangKyTaiKhoanRequest taiKhoan) {
        DangKyTaiKhoanResponse dangKyTaiKhoanResponse = taiKhoanService.dangKyTaiKhoan(taiKhoan.getUsername(), taiKhoan.getEmail(), taiKhoan.getPassword());
        return ResponseEntity.ok(dangKyTaiKhoanResponse);

    }

}
