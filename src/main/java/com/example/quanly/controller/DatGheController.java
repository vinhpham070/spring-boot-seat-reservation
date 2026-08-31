package com.example.quanly.controller;

import com.example.quanly.dto.GheDTO.GheResponse;
import com.example.quanly.dto.GheDTO.GiuGheRequest;
import com.example.quanly.dto.PhienResponse;
import com.example.quanly.dto.GheDTO.XacNhanVaHuyRequest;
import com.example.quanly.model.User;
import com.example.quanly.repository.UserRepository;
import com.example.quanly.service.DatGheService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/dat-ghe")
@CrossOrigin(origins = "*")
public class DatGheController {

    private final DatGheService datGheService;
    private final UserRepository userRepository;


    public DatGheController(DatGheService datGheService, UserRepository userRepository) {
        this.datGheService = datGheService;
        this.userRepository = userRepository;
    }

    @PostMapping("/giu-ghe")
    public ResponseEntity<PhienResponse> giuGhe(@Valid @RequestBody GiuGheRequest request, Principal principal) {
        String username = principal.getName();
        PhienResponse taoPhien = datGheService.giuGhe(username, request.getGheNgoiId());
        return ResponseEntity.ok(taoPhien);
    }

    @PostMapping("/xac-nhan-ghe/{phienId}")
    public ResponseEntity<PhienResponse> xacNhanGhe(@PathVariable Long phienId, Principal principal) {
        String username = principal.getName();
        PhienResponse xacNhanPhien = datGheService.xacNhanDatGhe(phienId, username);
        return ResponseEntity.ok(xacNhanPhien);
    }

    @PostMapping("/huy-giu-ghe/{phienId}")
    public ResponseEntity<PhienResponse> huyGiuGhe(@PathVariable Long phienId, Principal principal) {
        String username = principal.getName();
        PhienResponse huyPhien = datGheService.huyGiuGhe(phienId, username);
        return ResponseEntity.ok(huyPhien);
    }

    @GetMapping("/so-do/{phongId}")
    public ResponseEntity<List<GheResponse>> laySoDo(@PathVariable Long phongId, Principal principal) {
        String username = principal.getName();
        List<GheResponse> soDo = datGheService.laySoDoGheTheoPhong(phongId, username);
        return ResponseEntity.ok(soDo);
    }



}