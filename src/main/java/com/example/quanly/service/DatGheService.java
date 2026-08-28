package com.example.quanly.service;

import com.example.quanly.dto.GheDTO.GheResponse;
import com.example.quanly.dto.PhienResponse;
import com.example.quanly.exception.BadRequestException;
import com.example.quanly.model.*;
import com.example.quanly.repository.GheNgoiRepository;
import com.example.quanly.repository.PhienDatGheRepository;
import com.example.quanly.repository.PhongRepository;
import com.example.quanly.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DatGheService {
    private final PhienDatGheRepository phienDatGheRepository;
    private final PhongRepository phongRepository;
    private final UserRepository userRepository;
    private final GheNgoiRepository gheNgoiRepository;

    public DatGheService(PhienDatGheRepository phienDatGheRepository, PhongRepository phongRepository, UserRepository userRepository, GheNgoiRepository gheNgoiRepository) {
        this.phienDatGheRepository = phienDatGheRepository;
        this.phongRepository = phongRepository;
        this.userRepository = userRepository;
        this.gheNgoiRepository = gheNgoiRepository;
    }

    @Transactional
    public PhienResponse giuGhe(Long userId, Long gheNgoiId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User không tồn tại!"));
        GheNgoi gheNgoi = gheNgoiRepository.findById(gheNgoiId).orElseThrow(() -> new BadRequestException("Ghế ngồi không tồn tại!"));

        LocalDateTime now = LocalDateTime.now();
        boolean temp = phienDatGheRepository.existsByGheNgoiAndTrangThaiAndThoiGianHetHanAfter(gheNgoi, TrangThai.DANG_GIU , now);
        boolean temp2 = phienDatGheRepository.existsByGheNgoiAndTrangThai(gheNgoi, TrangThai.DA_DAT);
        if (!temp2 && !temp) {
            PhienDatGhe phienDatGhe = new PhienDatGhe(gheNgoi, user, TrangThai.DANG_GIU, now.plusMinutes(5));
            phienDatGhe = phienDatGheRepository.save(phienDatGhe);
            PhienResponse phienResponse = new PhienResponse(user.getId(), gheNgoi.getId(), user.getUsername(), gheNgoi.getSoGhe(), TrangThai.DANG_GIU, now.plusMinutes(5), phienDatGhe.getId());
            return phienResponse;
        }
        throw new BadRequestException("Ghế không còn trống!");
    }

    @Transactional
    public PhienResponse xacNhanDatGhe(Long phienDatGheId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User không tồn tại!"));
        PhienDatGhe phienDatGhe = phienDatGheRepository
                .findById(phienDatGheId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên của người dùng!"));
        LocalDateTime now = LocalDateTime.now();
        if (phienDatGhe.getTrangThai() == TrangThai.DANG_GIU && phienDatGhe.getUser().getId().equals((user.getId())) && phienDatGhe.getThoiGianHetHan().isAfter(now)) {
            phienDatGhe.setTrangThai(TrangThai.DA_DAT);
            phienDatGhe = phienDatGheRepository.save(phienDatGhe);
            PhienResponse phienResponse = new PhienResponse(user.getId(), phienDatGhe.getGheNgoi().getId() , user.getUsername(), phienDatGhe.getGheNgoi().getSoGhe(), TrangThai.DA_DAT, phienDatGhe.getThoiGianHetHan(), phienDatGhe.getId());
            return phienResponse;

        }
        throw new BadRequestException("Đã có lỗi xảy ra vui lòng thử lại!");
    }

    @Transactional
    public PhienResponse huyGiuGhe(Long phienDatGheId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User không tồn tại!"));
        PhienDatGhe phienDatGhe = phienDatGheRepository
                .findById(phienDatGheId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên của người dùng!"));
        LocalDateTime now = LocalDateTime.now();
        if (phienDatGhe.getTrangThai() == TrangThai.DANG_GIU && phienDatGhe.getUser().getId().equals((user.getId())) && phienDatGhe.getThoiGianHetHan().isAfter(now)) {
            phienDatGhe.setTrangThai(TrangThai.DA_HUY);
            phienDatGheRepository.save(phienDatGhe);
            PhienResponse phienResponse = new PhienResponse(user.getId(), phienDatGhe.getGheNgoi().getId() , user.getUsername(), phienDatGhe.getGheNgoi().getSoGhe(), TrangThai.DA_HUY, phienDatGhe.getThoiGianHetHan(), phienDatGhe.getId());
            return phienResponse;
        }
        throw new BadRequestException("Đã có lỗi xảy ra vui lòng thử lại!");
    }

    public List<GheResponse> laySoDoGheTheoPhong(Long phongId, Long userId) {
        Phong phong = phongRepository.findById(phongId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));

        List<GheNgoi> danhSachGhe = phong.getDanhSachGhe();
        List<GheResponse> ketQua = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (GheNgoi gheNgoi : danhSachGhe) {
            TrangThai trangThai = TrangThai.TRONG;

            Optional<PhienDatGhe> phienDaDat = phienDatGheRepository.findFirstByGheNgoiAndTrangThai(gheNgoi, TrangThai.DA_DAT);

            if (phienDaDat.isPresent()) {
                if (userId != null && phienDaDat.get().getUser().getId().equals(userId)) {
                    trangThai = TrangThai.GHE_CUA_TOI;
                } else {
                    trangThai = TrangThai.DA_DAT;
                }
            } else {
                Optional<PhienDatGhe> phienGiu = phienDatGheRepository.findFirstByGheNgoiAndTrangThaiAndThoiGianHetHanAfter(gheNgoi, TrangThai.DANG_GIU, now);

                if (phienGiu.isPresent()) {
                    if (userId != null && phienGiu.get().getUser().getId().equals(userId)) {
                        trangThai = TrangThai.PHIEN_CUA_TOI;
                    } else {
                        trangThai = TrangThai.DANG_GIU;
                    }
                }
            }

            GheResponse duLieuGhe = new GheResponse(gheNgoi.getId(), gheNgoi.getSoGhe(), gheNgoi.getHang(), gheNgoi.getCot(), trangThai);
            ketQua.add(duLieuGhe);
        }
        return ketQua;
    }
}