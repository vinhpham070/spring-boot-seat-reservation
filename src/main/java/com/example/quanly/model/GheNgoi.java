package com.example.quanly.model;



import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "ghe_ngoi")
public class GheNgoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String soGhe;
    private Long hang;
    private Long cot;

    @ManyToOne
    @JoinColumn(name = "phong_id")
    private Phong phong;


    public GheNgoi() {};

    public GheNgoi(String soGhe, Long hang, Long cot) {
        this.soGhe = soGhe;
        this.hang = hang;
        this.cot = cot;


    }
    // GETTER ----------------------------------------------

    public Long getId() {
        return id;
    }



    public String getSoGhe() {
        return soGhe;
    }

    public Long getHang() {
        return hang;
    }



    public Long getCot() {
        return cot;
    }

    // SETTER ----------------------------------------------

    public void setSoGhe(String soGhe) {
        this.soGhe = soGhe;
    }

    public void setHang(Long hang) {
        this.hang = hang;
    }

    public void setPhong(Phong phong) { this.phong = phong; }


    public void setCot(Long cot) {
        this.cot = cot;
    }
}
