package com.example.quanly.dto.VaiTroDTO;

import com.example.quanly.model.VaiTro;

public class PhanQuyenResponse {
    String username;
    Long phanQuyenId;
    Long phongId;
    VaiTro vaiTro;

    public PhanQuyenResponse() {
    }

    public PhanQuyenResponse(String username, VaiTro vaiTro, Long phongId, Long phanQuyenId) {
        this.username = username;
        this.vaiTro = vaiTro;
        this.phanQuyenId = phanQuyenId;
        this.phongId = phongId;
    }

    public Long getPhongId() {
        return phongId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPhanQuyenId() {
        return phanQuyenId;
    }

    public void setPhanQuyenId(Long phanQuyenId) {
        this.phanQuyenId = phanQuyenId;
    }

    public VaiTro getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(VaiTro vaiTro) {
        this.vaiTro = vaiTro;
    }

    public void setPhongId(Long phongId) {
        this.phongId = phongId;
    }
}
