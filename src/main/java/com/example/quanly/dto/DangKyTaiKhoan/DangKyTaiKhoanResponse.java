package com.example.quanly.dto.DangKyTaiKhoan;

public class DangKyTaiKhoanResponse {
    private String username;
    private String email;

    public DangKyTaiKhoanResponse() {
    }

    public DangKyTaiKhoanResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
