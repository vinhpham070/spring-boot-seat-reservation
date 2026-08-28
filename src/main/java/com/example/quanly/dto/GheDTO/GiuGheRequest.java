package com.example.quanly.dto.GheDTO;


import jakarta.validation.constraints.NotNull;

public class GiuGheRequest {
    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;

    @NotNull(message = "ID ghế ngồi không được để trống")
    private Long gheNgoiId;

    public GiuGheRequest() {}

    public GiuGheRequest(Long userId, Long gheNgoiId) {
        this.userId = userId;
        this.gheNgoiId = gheNgoiId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGheNgoiId() {
        return gheNgoiId;
    }

    public void setGheNgoiId(Long gheNgoiId) {
        this.gheNgoiId = gheNgoiId;
    }
}
