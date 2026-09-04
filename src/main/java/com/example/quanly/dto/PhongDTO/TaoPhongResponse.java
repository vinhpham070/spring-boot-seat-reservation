package com.example.quanly.dto.PhongDTO;

public class TaoPhongResponse {
    private String tenPhong;
    private String tenChuPhong;
    private Long soHang;
    private Long soCot;

    public TaoPhongResponse() {
    }

    public TaoPhongResponse(String tenPhong, String tenChuPhong, Long soHang, Long soCot) {
        this.tenPhong = tenPhong;
        this.tenChuPhong = tenChuPhong;
        this.soHang = soHang;
        this.soCot = soCot;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public String getTenChuPhong() {
        return tenChuPhong;
    }

    public Long getSoCot() {
        return soCot;
    }

    public Long getSoHang() {
        return soHang;
    }
}
