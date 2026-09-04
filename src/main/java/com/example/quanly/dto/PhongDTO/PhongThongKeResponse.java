package com.example.quanly.dto.PhongDTO;

public class PhongThongKeResponse {
    private Long phongId;
    private String tenPhong;
    private Long soLuongTong;
    private Long soLuongGheTrong;
    private Long soLuongGheDat;
    private Long soLuongGheDangGiu;

    public PhongThongKeResponse() {
    }

    public PhongThongKeResponse(Long phongId, Long soLuongTong, String tenPhong, Long soLuongGheTrong, Long soLuongGheDat, Long soLuongGheDangGiu) {
        this.phongId = phongId;
        this.soLuongTong = soLuongTong;
        this.tenPhong = tenPhong;
        this.soLuongGheTrong = soLuongGheTrong;
        this.soLuongGheDat = soLuongGheDat;
        this.soLuongGheDangGiu = soLuongGheDangGiu;
    }

    public Long getPhongId() {
        return phongId;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public Long getSoLuongTong() {
        return soLuongTong;
    }

    public Long getSoLuongGheTrong() {
        return soLuongGheTrong;
    }

    public Long getSoLuongGheDat() {
        return soLuongGheDat;
    }

    public Long getSoLuongGheDangGiu() {
        return soLuongGheDangGiu;
    }
}
