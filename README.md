# 🎟️ Seat Booking System (Hệ Thống Quản Lý & Đặt Chỗ Ngồi)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Vite](https://img.shields.io/badge/Vite-646C9A?style=for-the-badge&logo=vite&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)

Ứng dụng web Fullstack hỗ trợ tạo phòng, quản lý sơ đồ ghế ma trận động (lên tới kích thước lớn 30x30 / 50x50), mô phỏng quy trình đặt chỗ theo thời gian thực với cơ chế giữ ghế có thời hạn và phân quyền đa tầng (Owner, Admin, Guest).

---

## 📸 Demo Giao Diện

- **Trang chủ & Quản lý phòng:** Danh sách phòng trực quan, thống kê nhanh số ghế trống, đang giữ và đã mua.
- **Sơ đồ ma trận linh hoạt:** Tự động ngắt dòng và chia lưới bằng CSS Grid, hỗ trợ cuộn ngang độc lập cho các phòng quy mô hội trường/rạp chiếu phim.
- **Dark Mode tối ưu:** Tối ưu hóa độ tương phản với phong cách Dark theme hiện đại, dot pattern nền và hiệu ứng phản hồi xúc giác UI.

---

## ✨ Tính Năng Nổi Bật

### 1. Phân quyền đa cấp bậc (RBAC)
- **Guest / Thành viên:** 
  - Xem sơ đồ ghế trực quan theo màu sắc trạng thái.
  - Chọn ghế trống để bắt đầu phiên giữ chỗ (Hold Session) trong vòng **5 phút**.
  - Xác nhận thanh toán/đặt chỗ hoặc chủ động hủy phiên giữ.
  - Xem và quản lý các ghế thuộc sở hữu của chính mình (`GHE_CUA_TOI`).
- **Owner (Chủ phòng):**
  - Toàn quyền cấu hình tên phòng, kích thước ma trận ghế ($Hàng \times Cột$).
  - Xem chi tiết danh tính người đang đặt (`tenNguoiDat`) trên từng ghế.
  - Can thiệp hủy vé đã đặt của bất kỳ người dùng nào trong phòng.
  - Đặt vé hộ người khác kèm ghi chú chi tiết.
  - Cấp/Hạ quyền quản trị viên (`Admin`) hoặc khách (`Guest`).

### 2. Xử lý Trạng thái & Nghiệp vụ ghế phức tạp
- **Hệ thống trạng thái 4 màu:**
  - 🟢 **Trống (Available):** Ghế sẵn sàng để chọn.
  - 🟡 **Đang giữ (Held):** Ghế đang nằm trong phiên giao dịch của một người dùng (hết hạn sau 300s).
  - 🔴 **Đã mua (Booked):** Ghế đã xác nhận đặt thành công.
  - 🟣 **Của bạn (User-owned):** Ghế bạn đang giữ hoặc đã sở hữu.
- **Đồng hồ đếm ngược phiên (Countdown Timer):** Cảnh báo trực tiếp trên UI khi phiên giữ chỗ sắp hết hạn.

### 3. Trải nghiệm người dùng (UX) & Kỹ thuật Frontend
- **Debounced Validation:** Tự động kiểm tra tính hợp lệ của dữ liệu (Email, độ dài Password) khi người dùng ngừng gõ $0.5s$, giảm tải logic thừa và báo lỗi trực tiếp dưới input.
- **Bắt lỗi RESTful toàn diện:** Tích hợp `GlobalExceptionHandler` phía Spring Boot để hiển thị chính xác lỗi nghiệp vụ (trùng username, sai định dạng, xung đột dữ liệu).
- **History Drawer:** Sidebar trượt mượt mà với hiệu ứng làm mờ nền (Overlay), lưu trữ lịch sử các phòng đã truy cập gần đây qua `localStorage`.

---

## 🛠️ Công Nghệ Sử Dụng

### Backend
- **Ngôn ngữ:** Java 17+
- **Framework:** Spring Boot 3.x (Spring MVC, Spring Data JPA, Spring Security)
- **Database:** MySQL / PostgreSQL
- **Xử lý ngoại lệ:** `@RestControllerAdvice`, Global Validation (`jakarta.validation`)

### Frontend
- **Thư viện chính:** React 18
- **Build tool:** Vite
- **Định tuyến:** React Router DOM (v6)
- **Quản lý trạng thái:** React Context API (`AuthProvider`)
- **Tạo kiểu:** Custom CSS Grid & Flexbox (Responsive, Smooth scrolling)

---

## 🚀 Hướng Dẫn Cài Đặt & Khởi Chạy

### 1. Yêu cầu hệ thống
- **JDK:** 17 trở lên
- **Node.js:** 18.x trở lên & `npm`
- **Database:** MySQL 8.x hoặc PostgreSQL

---

### 2. Cấu hình Backend (Spring Boot)

1. Di chuyển vào thư mục backend:
   ```bash
   cd backend
