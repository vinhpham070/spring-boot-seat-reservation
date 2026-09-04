package com.example.quanly.service;

import com.example.quanly.dto.GheDTO.GheResponse;
import com.example.quanly.dto.PhienResponse;
import com.example.quanly.exception.BadRequestException;
import com.example.quanly.exception.UnAthorizedException;
import com.example.quanly.model.*;
import com.example.quanly.repository.*;
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
    private final ThanhVienPhongRepository thanhVienPhongRepository;

    public DatGheService(PhienDatGheRepository phienDatGheRepository, PhongRepository phongRepository, UserRepository userRepository, GheNgoiRepository gheNgoiRepository, ThanhVienPhongRepository thanhVienPhongRepository) {
        this.phienDatGheRepository = phienDatGheRepository;
        this.phongRepository = phongRepository;
        this.userRepository = userRepository;
        this.gheNgoiRepository = gheNgoiRepository;
        this.thanhVienPhongRepository = thanhVienPhongRepository;
    }

    @Transactional
    public PhienResponse giuGhe(String username, Long gheNgoiId) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User không tồn tại!"));
        GheNgoi gheNgoi = gheNgoiRepository
                .findById(gheNgoiId)
                .orElseThrow(() -> new BadRequestException("Ghế ngồi không tồn tại!"));

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
    public PhienResponse xacNhanDatGhe(Long phienDatGheId, String username) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User không tồn tại!"));
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
        throw new UnAthorizedException("Bạn không ");
    }

    @Transactional
    public PhienResponse huyGiuGhe(Long phienDatGheId, String username) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User không tồn tại!"));
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

    @Transactional
    public PhienResponse huyGhe(Long phienDatGheId, String username) {
        PhienDatGhe phienDatGhe = phienDatGheRepository
                .findById(phienDatGheId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên của người dùng!"));
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User không tồn tại!"));
        if (phienDatGhe.getUser().getId().equals(user.getId()) && phienDatGhe.getTrangThai() == TrangThai.DA_DAT) {
            phienDatGhe.setTrangThai(TrangThai.DA_HUY);
            phienDatGheRepository.save(phienDatGhe);
            return new PhienResponse(user.getId(), phienDatGhe.getGheNgoi().getId(), user.getUsername(), phienDatGhe.getGheNgoi().getSoGhe(), TrangThai.DA_HUY, phienDatGhe.getThoiGianHetHan(), phienDatGhe.getId());
        }
        throw new BadRequestException("Đã có lỗi xảy ra vui lòng thử lại!");

    }


    // Owner function -----------------------
    @Transactional
    public PhienResponse huyGheBoiQuanLy(Long phongId, Long gheId, String chuPhongUsername) {
        Phong phong = phongRepository
                .findById(phongId)
                .orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        GheNgoi gheNgoi = gheNgoiRepository
                .findById(gheId)
                .orElseThrow(() -> new BadRequestException("Ghế ngồi không tồn tại!"));
        User chuPhong = userRepository
                .findByUsername(chuPhongUsername)
                .orElseThrow(() -> new BadRequestException("Lỗi tìm user!"));
        LocalDateTime now = LocalDateTime.now();
        if (!thanhVienPhongRepository.existsByPhongAndUserAndVaiTroIn(phong, chuPhong, List.of(VaiTro.OWNER, VaiTro.ADMIN))) {
            throw new UnAthorizedException("Truy cập trái phép!");
        }


            Optional<PhienDatGhe> phienDatGhe = phienDatGheRepository
                    .findFirstByGheNgoiAndTrangThai(gheNgoi, TrangThai.DA_DAT);
            if (phienDatGhe.isPresent()) {
                phienDatGhe.get().setTrangThai(TrangThai.DA_HUY);
                phienDatGheRepository.save(phienDatGhe.get());
            } else {
                phienDatGhe = phienDatGheRepository
                        .findFirstByGheNgoiAndTrangThaiAndThoiGianHetHanAfter(gheNgoi, TrangThai.DANG_GIU, now);
                if (phienDatGhe.isPresent()) {
                    phienDatGhe.get().setTrangThai(TrangThai.DA_HUY);
                    phienDatGheRepository.save(phienDatGhe.get());
                }
            }
            if (phienDatGhe.isPresent()) {
                return new PhienResponse(
                        chuPhong.getId(),
                        gheNgoi.getId(),
                        chuPhong.getUsername(),
                        gheNgoi.getSoGhe(),
                        TrangThai.DA_HUY,
                        phienDatGhe.get().getThoiGianHetHan(),
                        phienDatGhe.get().getId()
                );
            } else {
                return new PhienResponse(
                        chuPhong.getId(),
                        gheNgoi.getId(),
                        chuPhong.getUsername(),
                        gheNgoi.getSoGhe(),
                        TrangThai.TRONG,
                        null,
                        null
                );
            }


    }

    @Transactional
    public PhienResponse datGheBoiQuanLy(Long phongId, Long gheId, String datHoUsername, String ghiChu, String chuPhongUsername) {
        Phong phong = phongRepository
                .findById(phongId)
                .orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        GheNgoi gheNgoi = gheNgoiRepository
                .findById(gheId)
                .orElseThrow(() -> new BadRequestException("Ghế ngồi không tồn tại!"));
        User chuPhong = userRepository
                .findByUsername(chuPhongUsername)
                .orElseThrow(() -> new BadRequestException("Lỗi tìm user!"));
        User datHoUser = userRepository
                .findByUsername(datHoUsername)
                .orElse(null);

        if (!thanhVienPhongRepository.existsByPhongAndUserAndVaiTroIn(phong, chuPhong, List.of(VaiTro.OWNER, VaiTro.ADMIN))) {
            throw new UnAthorizedException("Truy cập trái phép!");
        }


        PhienResponse phienResponse = huyGheBoiQuanLy(phongId, gheId, chuPhongUsername);
        LocalDateTime now = LocalDateTime.now();
        PhienDatGhe phienDatGhe = new PhienDatGhe(gheNgoi, datHoUser, TrangThai.DA_DAT, now.plusMinutes(5));
        phienDatGhe.setGhiChu(ghiChu);
        phienDatGheRepository.save(phienDatGhe);

        Long targetUserId = (datHoUser != null) ? datHoUser.getId() : null;
        String targetUsername = (datHoUser != null) ? datHoUser.getUsername() : null;

        PhienResponse response = new PhienResponse(
                targetUserId,
                gheNgoi.getId(),
                targetUsername,
                gheNgoi.getSoGhe(),
                TrangThai.DA_DAT,
                phienDatGhe.getThoiGianHetHan(),
                phienDatGhe.getId()
        );

        response.setGhiChu(ghiChu);

        return response;



    }
}