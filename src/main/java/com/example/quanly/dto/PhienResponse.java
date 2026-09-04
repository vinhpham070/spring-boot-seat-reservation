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
    private String ghiChu;

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

    public String getGhiChu() {
        return ghiChu;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGheNgoiId(Long gheNgoiId) {
        this.gheNgoiId = gheNgoiId;
    }

    public void setSoGhe(String soGhe) {
        this.soGhe = soGhe;
    }

    public void setTrangThai(TrangThai trangThai) {
        this.trangThai = trangThai;
    }

    public void setThoiGianHetHan(LocalDateTime thoiGianHetHan) {
        this.thoiGianHetHan = thoiGianHetHan;
    }

    public void setPhienId(Long phienId) {
        this.phienId = phienId;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
