package com.example.quanly.model;

import jakarta.persistence.*;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;

@Entity
@Table(name = "thanh_vien_phong")
public class ThanhVienPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "phong_id")
    private Phong phong;
    @Enumerated(EnumType.STRING)
    private VaiTro vaiTro;

    public ThanhVienPhong() {};

    public ThanhVienPhong(VaiTro vaiTro, Phong phong, User user) {
        this.vaiTro = vaiTro;
        this.phong = phong;
        this.user = user;
    }
    // GETTER ----------------------------------------------

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public VaiTro getVaiTro() {
        return vaiTro;
    }

    public Phong getPhong() {
        return phong;
    }

    // SETTER ----------------------------------------------


    public void setPhong(Phong phong) {
        this.phong = phong;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVaiTro(VaiTro vaiTro) {
        this.vaiTro = vaiTro;
    }
}
