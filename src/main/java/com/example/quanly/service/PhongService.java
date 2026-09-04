package com.example.quanly.service;

import com.example.quanly.dto.GheDTO.GheResponse;
import com.example.quanly.dto.PhongDTO.PhongThongKeResponse;
import com.example.quanly.dto.PhongDTO.SoDoPhongResponse;
import com.example.quanly.dto.PhongDTO.TaoPhongResponse;
import com.example.quanly.dto.PhongDTO.XoaPhongResponse;
import com.example.quanly.dto.VaiTroDTO.PhanQuyenRequest;
import com.example.quanly.dto.VaiTroDTO.PhanQuyenResponse;
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
public class PhongService {
    private final PhongRepository phongRepository;
    private final UserRepository userRepository;
    private final GheNgoiRepository gheNgoiRepository;
    private final PhienDatGheRepository phienDatGheRepository;
    private final ThanhVienPhongRepository thanhVienPhongRepository;


    public PhongService(PhongRepository phongRepository, UserRepository userRepository, GheNgoiRepository gheNgoiRepository, PhienDatGheRepository phienDatGheRepository, ThanhVienPhongRepository thanhVienPhongRepository) {
        this.phienDatGheRepository = phienDatGheRepository;
        this.phongRepository = phongRepository;
        this.userRepository = userRepository;
        this.gheNgoiRepository = gheNgoiRepository;
        this.thanhVienPhongRepository = thanhVienPhongRepository;
    }

    @Transactional
    public TaoPhongResponse taoPhong(String tenPhong, Long cot, Long hang, String username) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User không tồn tại!"));

        List<GheNgoi> danhSachGhe = new ArrayList<>();

        Phong phong = new Phong(tenPhong, cot, hang, null);
        ThanhVienPhong thanhVienPhong = new ThanhVienPhong(VaiTro.OWNER, phong,  user);


        for (Long i = 0L; i < hang; i++) {
            char tenHang = (char) ('A' + i);
            for (Long j = 1L; j <= cot; j++) {
                String soGhe = "" + tenHang + j;
                danhSachGhe.add(new GheNgoi(soGhe, i + 1, j, phong));
            }
        }
        phong.setDanhSachGhe(danhSachGhe);
        phongRepository.save(phong);
        gheNgoiRepository.saveAll(danhSachGhe);
        thanhVienPhongRepository.save(thanhVienPhong);

        return new TaoPhongResponse(tenPhong, username, hang, cot);
    }

    public SoDoPhongResponse laySoDoGheTheoPhong(Long phongId, String username) {
        Phong phong = phongRepository.findById(phongId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));

        Long userId = null;
        User user = null;
        if (username != null) {
            user = userRepository.findByUsername(username).orElseThrow(() -> new BadRequestException("Lỗi tìm user!"));
            userId = user.getId();
        }
        boolean isOwner = userId != null && thanhVienPhongRepository.existsByPhongAndUserAndVaiTroIn(phong, user, List.of(VaiTro.ADMIN, VaiTro.OWNER));
        List<GheNgoi> danhSachGhe = phong.getDanhSachGhe();
        List<GheResponse> ketQua = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (GheNgoi gheNgoi : danhSachGhe) {
            TrangThai trangThai = TrangThai.TRONG;
            Long phienId = null;
            String tenNguoiDat = null;

            Optional<PhienDatGhe> phienDaDat = phienDatGheRepository.findFirstByGheNgoiAndTrangThai(gheNgoi, TrangThai.DA_DAT);

            if (phienDaDat.isPresent()) {
                if (userId != null && phienDaDat.get().getUser() != null && phienDaDat.get().getUser().getId().equals(userId)) {
                    trangThai = TrangThai.GHE_CUA_TOI;
                    phienId = phienDaDat.get().getId();
                } else {
                    trangThai = TrangThai.DA_DAT;
                    tenNguoiDat = isOwner ? phienDaDat.get().getTenNguoiDat() : null;
                }
            } else {
                Optional<PhienDatGhe> phienGiu = phienDatGheRepository.findFirstByGheNgoiAndTrangThaiAndThoiGianHetHanAfter(gheNgoi, TrangThai.DANG_GIU, now);

                if (phienGiu.isPresent()) {
                    if (userId != null && phienGiu.get().getUser().getId().equals(userId)) {
                        trangThai = TrangThai.PHIEN_CUA_TOI;
                        phienId = phienGiu.get().getId();

                    } else {
                        trangThai = TrangThai.DANG_GIU;
                        tenNguoiDat = isOwner ? phienGiu.get().getUser().getUsername() : null;
                    }
                }
            }

            GheResponse duLieuGhe = new GheResponse(gheNgoi.getId(), gheNgoi.getSoGhe(), gheNgoi.getHang(), gheNgoi.getCot(), trangThai, phienId);
            duLieuGhe.setTenNguoiDat(tenNguoiDat);
            ketQua.add(duLieuGhe);
        }

        return new SoDoPhongResponse(phongId, phong.getTenPhong(), isOwner, ketQua);
    }

    public List<PhongThongKeResponse> getDanhSachPhong(String username) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User không tồn tại!"));

        List<PhongThongKeResponse> danhSachPhongThongKe = new ArrayList<>();

        for (Phong phong : user.getDanhSachPhong()) {
            LocalDateTime now = LocalDateTime.now();
            Long soLuongGheDaDat = phienDatGheRepository.countByGheNgoi_PhongAndTrangThai(phong, TrangThai.DA_DAT);
            Long soLuongGheDangGiu = phienDatGheRepository.countByGheNgoi_PhongAndTrangThaiAndThoiGianHetHanAfter(phong, TrangThai.DANG_GIU, now);
            Long soLuongGheTrong = phong.getDanhSachGhe().size() - soLuongGheDaDat - soLuongGheDangGiu;
            danhSachPhongThongKe.add(new PhongThongKeResponse(phong.getId(), (long)  phong.getDanhSachGhe().size(), phong.getTenPhong(), soLuongGheTrong, soLuongGheDaDat, soLuongGheDangGiu));

        }

        return danhSachPhongThongKe;


    }





    public PhanQuyenResponse phanQuyenGuest(String username, Long phongId, String chuPhongUsername) {
        Phong phong = phongRepository
                .findById(phongId)
                .orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên của người dùng!"));
        User chuPhong = userRepository
                .findByUsername(chuPhongUsername)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên của người dùng!"));
        if (!thanhVienPhongRepository.existsByPhongAndUserAndVaiTro(phong, chuPhong, VaiTro.OWNER)) {
            throw new UnAthorizedException("Truy cập trái phép!");
        }
        if (user.getId().equals(chuPhong.getId())) {
            throw new BadRequestException("Không thể hạ cấp owner!");
        }

        Optional<ThanhVienPhong> vaiTroUser = thanhVienPhongRepository.findByUserAndPhong(user, phong);

        if (vaiTroUser.isPresent()) {
            vaiTroUser.get().setVaiTro(VaiTro.GUEST);
            return new PhanQuyenResponse(username, VaiTro.GUEST, phongId, vaiTroUser.get().getId());
        } else {
            ThanhVienPhong thanhVienPhong = new ThanhVienPhong(VaiTro.GUEST, phong, user);
            thanhVienPhongRepository.save(thanhVienPhong);
            return new PhanQuyenResponse(username, VaiTro.GUEST, phongId, thanhVienPhong.getId());
        }
    }

    public PhanQuyenResponse phanQuyenAdmin(String username, Long phongId, String chuPhongUsername) {
        Phong phong = phongRepository
                .findById(phongId)
                .orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên của người dùng!"));
        User chuPhong = userRepository
                .findByUsername(chuPhongUsername)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên của người dùng!"));

        if (!thanhVienPhongRepository.existsByPhongAndUserAndVaiTro(phong, chuPhong, VaiTro.OWNER)) {
            throw new UnAthorizedException("Truy cập trái phép!");
        }
        if (user.getId().equals(chuPhong.getId())) {
            throw new BadRequestException("Không thể hạ cấp owner!");
        }

        Optional<ThanhVienPhong> vaiTroUser = thanhVienPhongRepository.findByUserAndPhong(user, phong);

        if (vaiTroUser.isPresent()) {
            vaiTroUser.get().setVaiTro(VaiTro.ADMIN);
            return new PhanQuyenResponse(username, VaiTro.ADMIN, phongId, vaiTroUser.get().getId());
        } else {
            ThanhVienPhong thanhVienPhong = new ThanhVienPhong(VaiTro.ADMIN, phong, user);
            thanhVienPhongRepository.save(thanhVienPhong);
            return new PhanQuyenResponse(username, VaiTro.ADMIN, phongId, thanhVienPhong.getId());
        }



    }


}
