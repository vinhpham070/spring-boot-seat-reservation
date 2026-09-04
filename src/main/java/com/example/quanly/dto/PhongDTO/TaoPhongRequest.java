package com.example.quanly.dto.PhongDTO;

import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class TaoPhongRequest {
    @NotBlank(message = "Tên phòng không được để trống!")
    private String tenPhong;
    @Positive(message = "Sai định dạng cột!")
    private Long cot;
    @Positive(message = "Sai định dạng hàng!")
    private Long hang;

    public TaoPhongRequest() {
    }

    public TaoPhongRequest(String tenPhong, Long cot, Long hang) {
        this.tenPhong = tenPhong;
        this.cot = cot;
        this.hang = hang;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public Long getCot() {
        return cot;
    }

    public Long getHang() {
        return hang;
    }
}
