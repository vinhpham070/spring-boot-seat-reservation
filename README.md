#  Seat Booking Web Application

Ứng dụng web Fullstack phục vụ khởi tạo phòng họp/hội trường, quản lý và đặt chỗ ngồi dạng ma trận động. Hệ thống xử lý luồng đặt chỗ có thời hạn (Hold Session), phân quyền người dùng (Owner/Admin/Guest) và tối ưu hiển thị sơ đồ kích thước lớn trên giao diện web.

---

##  Công Nghệ Sử Dụng

- **Backend:** Java 17, Spring Boot 3.x (Spring Data JPA, Spring Security, Hibernate, MySQL, Bean Validation).
- **Frontend:** React 18, Vite, React Router DOM (v6), React Context API (`AuthContext`), CSS Grid & Flexbox (Dark Mode).

---

##  Nghiệp Vụ & Tính Năng Chi Tiết

- **Phân quyền & Vai trò người dùng (RBAC):**
  - **Guest / Thành viên:** Nhập ID/URL để vào phòng; chọn ghế trống để kích hoạt phiên giữ chỗ trong **300 giây**; xác nhận đặt vé chính thức hoặc hủy giữ trước hạn; tra cứu và hủy vé do chính mình sở hữu (`GHE_CUA_TOI`).
  - **Owner (Chủ phòng):** Khởi tạo phòng với ma trận tùy biến (tối đa $50 \times 50$); xem trực quan tên người đặt (`tenNguoiDat`) trên từng ô ghế; can thiệp hủy vé của bất kỳ ai trong phòng; đặt vé hộ cho một `username` cụ thể kèm ghi chú; cấp quyền `Admin` hoặc hạ cấp xuống `Guest`.
- **Vòng đời & Trạng thái ghế:**
  - `TRONG` (Xanh lá): Ghế khả dụng cho mọi người dùng.
  - `DANG_GIU` (Vàng): Ghế đang nằm trong phiên giao dịch tạm thời của người khác.
  - `DA_DAT` (Đỏ): Ghế đã xác nhận đặt thành công.
  - `GHE_CUA_TOI` / `PHIEN_CUA_TOI` (Tím): Ghế thuộc quyền sở hữu hoặc phiên giữ của tài khoản hiện tại.

---

##  Điểm Nhấn Kỹ Thuật & Giải Pháp Thực Tế

- **Xử lý hiển thị ma trận lớn ($30 \times 30$):** Thay vì dùng `justify-content: center` khiến trình duyệt cắt mép trái (mất dải ghế A1–A9) khi zoom/thu nhỏ màn hình, hệ thống kết hợp `text-align: center` ở container ngoài cùng với `display: inline-grid` và `overflow-x: auto`. Cơ chế này giữ sơ đồ luôn ở giữa khi phòng nhỏ và hỗ trợ cuộn ngang chuẩn xác từ cột đầu tiên khi phòng lớn tràn màn hình.
- **Tối ưu Form Validation bằng Debounce (500ms):** Ứng dụng `useEffect` để kiểm tra format Email và độ dài Mật khẩu ngay khi người dùng ngừng gõ $0.5s$, hiển thị thông báo lỗi đỏ trực tiếp dưới ô input thay vì dùng pop-up `alert()` làm đứt đoạn trải nghiệm. Các lỗi từ Backend (như trùng Username) được gom từ `@RestControllerAdvice` và đồng bộ ngược về form.
- **Phòng vệ cơ sở dữ liệu trước lỗi Batch Insert:** Do cơ chế khóa `@GeneratedValue(strategy = IDENTITY)` của JPA làm vô hiệu hóa Batch Insert của Hibernate khi tạo lượng lớn bản ghi ghế, hệ thống chặn kích thước phòng từ 2 lớp: Frontend giới hạn form tối đa $50 \times 50$, Backend thẩm định nghiêm ngặt bằng `@Max(50)` và `@Valid` tại tầng Controller để bảo vệ CPU và Connection Pool.

---

##  Danh Sách REST API Endpoints

| Nhóm nghiệp vụ | Phương thức | Endpoint | Payload / Params | Mô tả |
| :--- | :--- | :--- | :--- | :--- |
| **Tài Khoản** | `POST` | `/api/tai-khoan/tao-tai-khoan` | `{ username, email, password }` | Đăng ký tài khoản mới |
| | `POST` | `/api/tai-khoan/dang-nhap` | `{ username, password }` | Đăng nhập hệ thống |
| | `POST` | `/api/tai-khoan/dang-xuat` | _None_ | Đăng xuất, hủy cookie phiên |
| **Phòng** | `POST` | `/api/phong/tao-phong` | `{ tenPhong, hang, cot }` | Tạo phòng mới (tối đa 50x50) |
| | `GET` | `/api/phong/{phongId}/so-do` | Path: `phongId` | Lấy sơ đồ ghế và quyền truy cập |
| | `GET` | `/api/phong/danh-sach-phong` | _None_ | Danh sách phòng kèm thống kê số ghế |
| **Đặt Ghế** | `POST` | `/api/dat-ghe/giu-ghe` | `{ gheNgoiId }` | Giữ ghế tạm thời (300s) |
| | `POST` | `/api/dat-ghe/huy-giu-ghe/{phienId}` | Path: `phienId` | Hủy phiên giữ ghế |
| | `POST` | `/api/dat-ghe/xac-nhan-ghe/{phienId}` | Path: `phienId` | Xác nhận đặt ghế chính thức |
| | `POST` | `/api/dat-ghe/huy-ghe/{phienId}` | Path: `phienId` | Hủy vé đã mua (chủ vé) |
| **Quản Lý** | `POST` | `/api/quan-ly/dat-ghe` | `{ phongId, gheId, datHoUsername, ghiChu }` | Owner đặt vé hộ |
| | `POST` | `/api/quan-ly/huy-ghe` | `{ phongId, gheId }` | Owner hủy vé bất kỳ |
| | `POST` | `/api/quan-ly/phan-quyen/admin` | `{ phongId, username }` | Cấp quyền Admin phòng |
| | `POST` | `/api/quan-ly/phan-quyen/guest` | `{ phongId, username }` | Hạ cấp quyền xuống Guest |

---

##  Hướng Dẫn Cài Đặt & Khởi Chạy

Yêu cầu môi trường: **JDK 17+**, **Node.js 18+ & npm**, **MySQL 8.x**.

```bash
# 1. Khởi chạy Backend (Spring Boot)
cd backend
# Cập nhật thông tin MySQL tại src/main/resources/application.properties trước khi chạy
mvn clean spring-boot:run
# Backend chạy tại: http://localhost:8080

# 2. Khởi chạy Frontend (React + Vite)
cd ../frontend
npm install
npm run dev
# Frontend chạy tại: http://localhost:5173
