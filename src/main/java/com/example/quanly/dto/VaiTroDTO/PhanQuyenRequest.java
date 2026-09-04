package com.example.quanly.dto.VaiTroDTO;

import com.example.quanly.model.VaiTro;

public class PhanQuyenRequest {
    String username;
    Long phongId;
    VaiTro vaiTro;

    public PhanQuyenRequest() {
    }

    public PhanQuyenRequest(String username, VaiTro vaiTro, Long phongId) {
        this.username = username;
        this.vaiTro = vaiTro;
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
