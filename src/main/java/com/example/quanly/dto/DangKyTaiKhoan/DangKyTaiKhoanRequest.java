package com.example.quanly.dto.DangKyTaiKhoan;

import jakarta.validation.constraints.*;

public class DangKyTaiKhoanRequest {
    @NotBlank(message = "Tên không được để trống!")
    private String username;
    @Email(message = "Email không hợp lệ!")
    private String email;
    @Size(min = 6, message =  "Password phải trên 6 ký tự!")
    private String password;

    public DangKyTaiKhoanRequest() {
    }

    public DangKyTaiKhoanRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
