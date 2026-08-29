package com.example.quanly.dto.DangNhapTaiKhoan;

public class DangNhapTaiKhoanResponse {
    private String message;
    private String username;

    public DangNhapTaiKhoanResponse() {
    }

    public DangNhapTaiKhoanResponse(String message, String username) {
        this.message = message;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
