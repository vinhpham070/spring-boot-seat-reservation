package com.example.quanly.controller;

import com.example.quanly.dto.GheDTO.GheResponse;
import com.example.quanly.dto.GheDTO.GiuGheRequest;
import com.example.quanly.dto.PhienResponse;
import com.example.quanly.dto.GheDTO.XacNhanVaHuyRequest;
import com.example.quanly.service.DatGheService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dat-ghe")
@CrossOrigin(origins = "*")
public class DatGheController {

    private final DatGheService datGheService;


    public DatGheController(DatGheService datGheService) {
        this.datGheService = datGheService;
    }

    @PostMapping("/giu-ghe")
    public ResponseEntity<PhienResponse> giuGhe(@Valid @RequestBody GiuGheRequest request) {
        PhienResponse taoPhien = datGheService.giuGhe(request.getUserId(), request.getGheNgoiId());
        return ResponseEntity.ok(taoPhien);
    }

    @PostMapping("/xac-nhan-ghe/{phienId}")
    public ResponseEntity<PhienResponse> xacNhanGhe(@PathVariable Long phienId, @RequestBody XacNhanVaHuyRequest request) {
        PhienResponse xacNhanPhien = datGheService.xacNhanDatGhe(phienId, request.getUserId());
        return ResponseEntity.ok(xacNhanPhien);
    }

    @PostMapping("/huy-giu-ghe/{phienId}")
    public ResponseEntity<PhienResponse> huyGiuGhe(@PathVariable Long phienId, @Valid @RequestBody XacNhanVaHuyRequest request) {
        PhienResponse huyPhien = datGheService.huyGiuGhe(phienId, request.getUserId());
        return ResponseEntity.ok(huyPhien);
    }

    @GetMapping("/so-do/{phongId}")
    public ResponseEntity<List<GheResponse>> laySoDo(@PathVariable Long phongId, @RequestParam(required = false) Long userId) {

        List<GheResponse> soDo = datGheService.laySoDoGheTheoPhong(phongId, userId);
        return ResponseEntity.ok(soDo);
    }



}