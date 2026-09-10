package com.example.quanly.controller;

import com.example.quanly.dto.GheDTO.Owner.DatGheChuPhongRequest;
import com.example.quanly.dto.GheDTO.Owner.HuyGheChuPhongRequest;
import com.example.quanly.dto.PhienResponse;
import com.example.quanly.dto.PhongDTO.SoDoPhongResponse;
import com.example.quanly.dto.VaiTroDTO.PhanQuyenRequest;
import com.example.quanly.dto.VaiTroDTO.PhanQuyenResponse;
import com.example.quanly.repository.PhongRepository;
import com.example.quanly.repository.UserRepository;
import com.example.quanly.service.DatGheService;
import com.example.quanly.service.PhongService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/quan-ly")
@CrossOrigin(origins = "*")
public class QuanLyController {

    private final DatGheService datGheService;

    private final PhongService phongService;


    public QuanLyController(DatGheService datGheService, PhongService phongService) {
        this.datGheService = datGheService;

        this.phongService = phongService;
    }


    @PostMapping("/dat-ghe")
    public ResponseEntity<PhienResponse> datGheChuPhong(@RequestBody DatGheChuPhongRequest request, Principal principal) {
        String usernameChuPhong = principal.getName();
        PhienResponse phienResponse = datGheService.datGheBoiQuanLy(request.getPhongId(), request.getGheId(), request.getDatHoUsername(), request.getGhiChu(), usernameChuPhong);
        return ResponseEntity.ok(phienResponse);
    }

    @PostMapping("/huy-ghe")
    public ResponseEntity<?> huyGheChuPhong(@RequestBody HuyGheChuPhongRequest request, Principal principal) {
        String usernameChuPhong = principal.getName();
        PhienResponse phienResponse = datGheService.huyGheBoiQuanLy(request.getPhongId(), request.getGheId(), usernameChuPhong);
        return ResponseEntity.ok(phienResponse);
    }

    @PostMapping("/phan-quyen/admin")
    public ResponseEntity<PhanQuyenResponse> phanQuyenAdmin(@RequestBody PhanQuyenRequest request, Principal principal) {
        String username = principal.getName();

        PhanQuyenResponse response = phongService.phanQuyenAdmin(request.getUsername(), request.getPhongId(), username);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/phan-quyen/guest")
    public ResponseEntity<PhanQuyenResponse> phanQuyenGuest(@RequestBody PhanQuyenRequest request, Principal principal) {
        String username = principal.getName();

        PhanQuyenResponse response = phongService.phanQuyenGuest(request.getUsername(), request.getPhongId(), username);

        return ResponseEntity.ok(response);
    }


}
