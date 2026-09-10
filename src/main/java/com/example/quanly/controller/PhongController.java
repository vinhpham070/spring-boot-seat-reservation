package com.example.quanly.controller;

import com.example.quanly.dto.PhongDTO.PhongThongKeResponse;
import com.example.quanly.dto.PhongDTO.SoDoPhongResponse;
import com.example.quanly.dto.PhongDTO.TaoPhongRequest;
import com.example.quanly.dto.PhongDTO.TaoPhongResponse;

import com.example.quanly.service.PhongService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/phong")
@CrossOrigin(origins = "*")
public class PhongController {
    PhongService phongService;

    public PhongController(PhongService phongService) {
        this.phongService = phongService;
    }

    @PostMapping("/tao-phong")
    public ResponseEntity<TaoPhongResponse> taoPhong(@Valid @RequestBody TaoPhongRequest request, Principal principal) {
        String username = principal.getName();
        TaoPhongResponse taoPhongResponse = phongService.taoPhong(request.getTenPhong(), request.getCot(), request.getHang(), username);
        return ResponseEntity.ok(taoPhongResponse);

    }
    @GetMapping("/{phongId}/so-do")
    public ResponseEntity<SoDoPhongResponse> laySoDo(@PathVariable Long phongId, Principal principal) {
        String username = null;
        if (principal != null) {
            username = principal.getName();
        }
        SoDoPhongResponse soDo = phongService
                .laySoDoGheTheoPhong(phongId, username);

        return ResponseEntity.ok(soDo);
    }
    @GetMapping("/danh-sach-phong")
    public ResponseEntity<List<PhongThongKeResponse>> danhSachPhong(Principal principal) {

        String username = principal.getName();

        List<PhongThongKeResponse> danhSachPhong = phongService.getDanhSachPhong(username);
        return ResponseEntity.ok(danhSachPhong);
    }







}
