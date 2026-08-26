package com.example.quanly.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "phien_dat_ghe")
public class PhienDatGhe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ghe_id")
    private GheNgoi gheNgoi;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai;

    private LocalDateTime thoiGianHetHan;

    public PhienDatGhe() {};

    public PhienDatGhe(GheNgoi gheNgoi, User user, TrangThai trangThai, LocalDateTime thoiGianHetHan) {
        this.gheNgoi = gheNgoi;
        this.user = user;
        this.trangThai = trangThai;
        this.thoiGianHetHan = thoiGianHetHan;
    }
    // GETTER ----------------------------------------------


    public Long getId() {
        return id;
    }

    public GheNgoi getGheNgoi() {
        return gheNgoi;
    }

    public User getUser() {
        return user;
    }

    public TrangThai getTrangThai() {
        return trangThai;
    }

    public LocalDateTime getThoiGianHetHan() {
        return thoiGianHetHan;
    }
    // SETTER ----------------------------------------------

    public void setGheNgoi(GheNgoi gheNgoi) {
        this.gheNgoi = gheNgoi;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTrangThai(TrangThai trangThai) {
        this.trangThai = trangThai;
    }

    public void setThoiGianHetHan(LocalDateTime thoiGianHetHan) {
        this.thoiGianHetHan = thoiGianHetHan;
    }
}
