package com.example.quanly.dto;

import com.example.quanly.model.TrangThai;

import java.time.LocalDateTime;

public class PhienResponse {
    private Long userId;
    private String username;
    private Long gheNgoiId;
    private String soGhe;
    private TrangThai trangThai;
    private LocalDateTime thoiGianHetHan;
    private Long phienId;

    public PhienResponse() {
    }

    public PhienResponse(Long userId, Long gheNgoiId, String username, String soGhe, TrangThai trangThai, LocalDateTime thoiGianHetHan, Long phienId) {
        this.userId = userId;
        this.gheNgoiId = gheNgoiId;
        this.username = username;
        this.soGhe = soGhe;
        this.trangThai = trangThai;
        this.thoiGianHetHan = thoiGianHetHan;
        this.phienId = phienId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGheNgoiId() {
        return gheNgoiId;
    }

    public String getUsername() {
        return username;
    }

    public String getSoGhe() {
        return soGhe;
    }

    public TrangThai getTrangThai() {
        return trangThai;
    }

    public LocalDateTime getThoiGianHetHan() {
        return thoiGianHetHan;
    }

    public Long getPhienId() {
        return phienId;
    }
}
