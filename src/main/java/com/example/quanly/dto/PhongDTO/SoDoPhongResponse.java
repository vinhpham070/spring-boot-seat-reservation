package com.example.quanly.dto.PhongDTO;

import com.example.quanly.dto.GheDTO.GheResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class SoDoPhongResponse {
    private Long phongId;
    private String tenPhong;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isOwner;
    private List<GheResponse> danhSachGhe;

    public SoDoPhongResponse() {
    }

    public SoDoPhongResponse(Long phongId, String tenPhong, boolean isOwner, List<GheResponse> danhSachGhe) {
        this.phongId = phongId;
        this.tenPhong = tenPhong;
        this.isOwner = isOwner;
        this.danhSachGhe = danhSachGhe;
    }

    public Long getPhongId() {
        return phongId;
    }

    public void setPhongId(Long phongId) {
        this.phongId = phongId;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public List<GheResponse> getDanhSachGhe() {
        return danhSachGhe;
    }

    public void setDanhSachGhe(List<GheResponse> danhSachGhe) {
        this.danhSachGhe = danhSachGhe;
    }
}
