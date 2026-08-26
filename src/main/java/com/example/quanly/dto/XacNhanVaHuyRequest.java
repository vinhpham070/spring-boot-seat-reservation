package com.example.quanly.dto;

import jakarta.validation.constraints.NotNull;

public class XacNhanVaHuyRequest {
    @NotNull(message = "ID người dùng không được để trống!")
    private Long userId;

    public XacNhanVaHuyRequest() {}

    public XacNhanVaHuyRequest(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
