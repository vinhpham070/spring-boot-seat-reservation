package com.example.quanly.dto.GheDTO.Owner;

public class HuyGheChuPhongRequest {

    private Long phongId;
    private Long gheId;

    public HuyGheChuPhongRequest() {
    }

    public HuyGheChuPhongRequest(Long phongId, Long gheId) {
        this.phongId = phongId;
        this.gheId = gheId;
    }

    public Long getGheId() {
        return gheId;
    }

    public void setGheId(Long gheId) {
        this.gheId = gheId;
    }

    public Long getPhongId() {
        return phongId;
    }

    public void setPhongId(Long phongId) {
        this.phongId = phongId;
    }
}
