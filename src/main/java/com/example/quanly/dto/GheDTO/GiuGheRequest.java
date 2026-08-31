package com.example.quanly.dto.GheDTO;


import jakarta.validation.constraints.NotNull;

public class GiuGheRequest {

    @NotNull(message = "ID ghế ngồi không được để trống")
    private Long gheNgoiId;

    public GiuGheRequest() {}

    public GiuGheRequest(Long gheNgoiId) {

        this.gheNgoiId = gheNgoiId;
    }





    public Long getGheNgoiId() {
        return gheNgoiId;
    }

    public void setGheNgoiId(Long gheNgoiId) {
        this.gheNgoiId = gheNgoiId;
    }
}
