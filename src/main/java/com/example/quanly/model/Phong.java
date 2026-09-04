package com.example.quanly.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "phong")
public class Phong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tenPhong;
    private Long soCot;
    private Long soHang;
    @OneToMany(mappedBy = "phong")
    private List<GheNgoi> danhSachGhe;


    public Phong() {
    }

    public Phong(String tenPhong, Long soCot, Long soHang, List<GheNgoi> danhSachGhe) {
        this.tenPhong = tenPhong;
        this.soCot = soCot;
        this.soHang = soHang;
        this.danhSachGhe = danhSachGhe;
    }
    // GETTER ----------------------------------------------
    public Long getId() {
        return id;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public Long getSoCot() {
        return soCot;
    }

    public Long getSoHang() {
        return soHang;
    }


    public List<GheNgoi> getDanhSachGhe() {
        return danhSachGhe;
    }

    // SETTER ----------------------------------------------

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public void setSoCot(Long soCot) {
        this.soCot = soCot;
    }

    public void setSoHang(Long soHang) {
        this.soHang = soHang;
    }

    public void setDanhSachGhe(List<GheNgoi> danhSachGhe) {
        this.danhSachGhe = danhSachGhe;
    }


}
