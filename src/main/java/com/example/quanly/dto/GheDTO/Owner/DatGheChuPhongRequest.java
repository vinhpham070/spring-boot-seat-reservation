package com.example.quanly.dto.GheDTO.Owner;

public class DatGheChuPhongRequest {
    private Long phongId;
    private Long gheId;
    private String datHoUsername;
    private String ghiChu;

    public DatGheChuPhongRequest() {
    }

    public DatGheChuPhongRequest(Long phongId, Long gheId, String datHoUsername, String ghiChu) {
        this.phongId = phongId;
        this.gheId = gheId;
        this.datHoUsername = datHoUsername;
        this.ghiChu = ghiChu;
    }

    public Long getPhongId() {
        return phongId;
    }

    public void setPhongId(Long phongId) {
        this.phongId = phongId;
    }

    public Long getGheId() {
        return gheId;
    }

    public void setGheId(Long gheId) {
        this.gheId = gheId;
    }

    public String getDatHoUsername() {
        return datHoUsername;
    }

    public void setDatHoUsername(String datHoUsername) {
        this.datHoUsername = datHoUsername;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
