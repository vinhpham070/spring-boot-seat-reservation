package com.example.quanly.dto.DangNhapTaiKhoan;

import jakarta.validation.constraints.NotBlank;

public class DangNhapTaiKhoanRequest {
    @NotBlank(message = "Tên không đươc để trống!")
    private String username;
    @NotBlank(message = "Mật khẩu không được để trống!")
    private String password;

    public DangNhapTaiKhoanRequest() {
    }

    public DangNhapTaiKhoanRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
