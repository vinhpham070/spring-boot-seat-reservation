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
    private int soCot;
    private int soHang;
    @OneToMany(mappedBy = "phong")
    private List<GheNgoi> danhSachGhe;
    @ManyToOne
    @JoinColumn(name = "chu_phong_id")
    private User chuPhong;

    public Phong() {
    }

    public Phong(Long id, String tenPhong, int soCot, int soHang, List<GheNgoi> danhSachGhe) {
        this.id = id;
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

    public int getSoCot() {
        return soCot;
    }

    public int getSoHang() {
        return soHang;
    }

    public List<GheNgoi> getDanhSachGhe() {
        return danhSachGhe;
    }

    // SETTER ----------------------------------------------

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public void setSoCot(int soCot) {
        this.soCot = soCot;
    }

    public void setSoHang(int soHang) {
        this.soHang = soHang;
    }

    public void setDanhSachGhe(List<GheNgoi> danhSachGhe) {
        this.danhSachGhe = danhSachGhe;
    }

    public void setChuPhong(User user) { this.chuPhong = user; }
}
