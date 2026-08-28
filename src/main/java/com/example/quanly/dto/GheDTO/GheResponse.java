package com.example.quanly.dto.GheDTO;

import com.example.quanly.model.TrangThai;

public class GheResponse {
    private Long gheId;
    private String soGhe;
    private Long hang;
    private Long cot;
    private TrangThai trangThai;

    public GheResponse() {
    }

    public GheResponse(Long gheId, String soGhe, Long hang, Long cot, TrangThai trangThai) {
        this.gheId = gheId;
        this.soGhe = soGhe;
        this.hang = hang;
        this.cot = cot;
        this.trangThai = trangThai;
    }

    // GETTER ----------------------------------------------

    public Long getGheId() {
        return gheId;
    }

    public String getSoGhe() {
        return soGhe;
    }

    public Long getCot() {
        return cot;
    }

    public Long getHang() {
        return hang;
    }

    public TrangThai getTrangThai() {
        return trangThai;
    }

    // SETTER ----------------------------------------------


    public void setSoGhe(String soGhe) {
        this.soGhe = soGhe;
    }

    public void setHang(Long hang) {
        this.hang = hang;
    }

    public void setCot(Long cot) {
        this.cot = cot;
    }

    public void setTrangThai(TrangThai trangThai) {
        this.trangThai = trangThai;
    }
}
