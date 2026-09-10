import { useState, useEffect, createContext, useContext } from 'react'
import { BrowserRouter, Routes, Route, useNavigate, useParams, Link } from 'react-router-dom'

/* =========================================================================
   0. CSS & THEME SETTINGS
   ========================================================================= */
const styles = `
  @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap');
  
  :root {
    --bg-main: #0f172a;
    --text-main: #f8fafc;
    --text-muted: #94a3b8;
    --card-bg: #1e293b;
    --border-color: #334155;
    --primary: #3b82f6;
    --primary-hover: #60a5fa;
    --danger: #ef4444;
    --success: #10b981;
    --warning: #f59e0b;
    --purple: #9333ea;
    --pattern-color: rgba(255, 255, 255, 0.03);
  }

  body {
    margin: 0;
    font-family: 'Inter', sans-serif;
    background-color: var(--bg-main);
    background-image: radial-gradient(var(--pattern-color) 2px, transparent 2px);
    background-size: 30px 30px;
    color: var(--text-main);
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }

  .btn-effect { transition: all 0.2s ease; }
  .btn-effect:active:not(:disabled) { transform: scale(0.95); }
  
  .card-hover { transition: all 0.3s ease; }
  .card-hover:hover { transform: translateY(-5px); box-shadow: 0 10px 25px rgba(0,0,0,0.4); }

  ::-webkit-scrollbar { width: 8px; height: 8px; }
  ::-webkit-scrollbar-track { background: transparent; }
  ::-webkit-scrollbar-thumb { background: var(--border-color); border-radius: 4px; }
  ::-webkit-scrollbar-thumb:hover { background: #475569; }
`

const AuthContext = createContext()

function AuthProvider({ children }) {
    const [user, setUser] = useState(() => {
        const savedUser = localStorage.getItem('username')
        return savedUser ? { username: savedUser } : null
    })
    const [isSidebarOpen, setIsSidebarOpen] = useState(false)

    const [history, setHistory] = useState(() => {
        const saved = localStorage.getItem('roomHistory')
        const parsed = saved ? JSON.parse(saved) : []
        return parsed.filter(room => room && room.phongId && room.phongId !== 'undefined')
    })

    const login = (username) => { setUser({ username }); localStorage.setItem('username', username) }
    const logout = () => { setUser(null); localStorage.removeItem('username') }

    const addHistory = (phong) => {
        if (!phong || !phong.phongId || phong.phongId === 'undefined') return
        setHistory(prev => {
            const filtered = prev.filter(p => p.phongId !== phong.phongId)
            const newHistory = [{ ...phong, time: new Date().toISOString() }, ...filtered].slice(0, 10)
            localStorage.setItem('roomHistory', JSON.stringify(newHistory))
            return newHistory
        })
    }

    return (
        <AuthContext.Provider value={{ user, login, logout, history, addHistory, isSidebarOpen, setIsSidebarOpen }}>
            <style>{styles}</style>
            {children}
        </AuthContext.Provider>
    )
}

/* =========================================================================
   1. COMPONENTS DÙNG CHUNG
   ========================================================================= */
function Sidebar() {
    const { history, isSidebarOpen, setIsSidebarOpen } = useContext(AuthContext)
    const navigate = useNavigate()

    return (
        <>
            {isSidebarOpen && (
                <div onClick={() => setIsSidebarOpen(false)} style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.6)', zIndex: 998, backdropFilter: 'blur(2px)' }} />
            )}

            <div style={{
                width: '280px', height: '100vh', backgroundColor: 'var(--card-bg)', borderRight: '1px solid var(--border-color)',
                position: 'fixed', left: 0, top: 0, overflowY: 'auto', padding: '20px 0', zIndex: 999,
                transform: isSidebarOpen ? 'translateX(0)' : 'translateX(-100%)',
                transition: 'transform 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                boxShadow: isSidebarOpen ? '10px 0 25px rgba(0,0,0,0.5)' : 'none'
            }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '0 20px', marginBottom: '20px' }}>
                    <h3 style={{ fontSize: '13px', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '1px', margin: 0 }}>🕒 Đã truy cập</h3>
                    <button onClick={() => setIsSidebarOpen(false)} className="btn-effect" style={{ background: 'var(--bg-main)', border: 'none', borderRadius: '50%', width: '32px', height: '32px', display: 'flex', justifyContent: 'center', alignItems: 'center', cursor: 'pointer', color: 'var(--text-muted)' }}>✖</button>
                </div>

                {history.length === 0 ? (
                    <p style={{ padding: '0 20px', fontSize: '14px', color: 'var(--text-muted)' }}>Chưa có lịch sử</p>
                ) : (
                    <ul style={{ listStyle: 'none', margin: 0, padding: 0 }}>
                        {history.map((room) => (
                            <li
                                key={room.phongId}
                                onClick={() => { navigate(`/phong/${room.phongId}`); setIsSidebarOpen(false); }}
                                style={{ padding: '12px 20px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '12px', borderBottom: '1px solid var(--border-color)', transition: 'background 0.2s' }}
                                onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'var(--bg-main)'}
                                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                            >
                                <span style={{ fontSize: '20px' }}>🚪</span>
                                <div style={{ overflow: 'hidden' }}>
                                    <div style={{ fontWeight: '600', fontSize: '15px', whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden' }}>{room.tenPhong}</div>
                                    <div style={{ fontSize: '12px', color: 'var(--text-muted)' }}>Mã: {room.phongId}</div>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </>
    )
}

function Header() {
    const { user, logout, setIsSidebarOpen } = useContext(AuthContext)
    const navigate = useNavigate()
    const [showMenu, setShowMenu] = useState(false)

    const xuLyDangXuat = () => {
        fetch('http://localhost:8080/api/tai-khoan/dang-xuat', { method: 'POST', credentials: 'include' })
            .then(() => { logout(); setShowMenu(false); navigate('/login') })
    }

    return (
        <div style={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', gap: '16px', padding: '16px 32px', backgroundColor: 'var(--card-bg)', borderBottom: '1px solid var(--border-color)', position: 'sticky', top: 0, zIndex: 100 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '20px', marginRight: 'auto' }}>
                {user && <button onClick={() => setIsSidebarOpen(true)} className="btn-effect" style={{ background: 'transparent', border: 'none', fontSize: '24px', cursor: 'pointer', color: 'var(--text-main)', padding: '0' }}>☰</button>}
                <Link to="/" className="btn-effect" style={{ textDecoration: 'none', color: 'var(--text-main)', fontWeight: '800', fontSize: '20px' }}>Trang Chủ</Link>
            </div>

            {!user ? (
                <button onClick={() => navigate('/login')} className="btn-effect" style={{ padding: '10px 24px', backgroundColor: 'var(--primary)', color: 'white', border: 'none', borderRadius: '8px', cursor: 'pointer', fontWeight: 'bold' }}>Đăng nhập</button>
            ) : (
                <div style={{ position: 'relative' }}>
                    <div onClick={() => setShowMenu(!showMenu)} className="btn-effect" style={{ width: '45px', height: '45px', borderRadius: '50%', backgroundColor: '#6366f1', color: 'white', display: 'flex', justifyContent: 'center', alignItems: 'center', fontWeight: 'bold', fontSize: '20px', cursor: 'pointer', userSelect: 'none', border: '3px solid var(--card-bg)' }}>{user.username.charAt(0).toUpperCase()}</div>
                    {showMenu && (
                        <div style={{ position: 'absolute', top: '60px', right: '0', backgroundColor: 'var(--card-bg)', boxShadow: '0 10px 25px rgba(0,0,0,0.5)', borderRadius: '12px', overflow: 'hidden', minWidth: '240px', zIndex: 1000, border: '1px solid var(--border-color)' }}>
                            <div style={{ padding: '20px', borderBottom: '1px solid var(--border-color)' }}><strong style={{ display: 'block', fontSize: '16px' }}>{user.username}</strong></div>
                            <ul style={{ listStyle: 'none', margin: 0, padding: 0 }}><li onClick={xuLyDangXuat} style={{ padding: '14px 20px', color: 'var(--danger)', cursor: 'pointer', fontWeight: 'bold' }}>🚪 Đăng xuất</li></ul>
                        </div>
                    )}
                </div>
            )}
        </div>
    )
}

function Footer() {
    const [showGioiThieu, setShowGioiThieu] = useState(false)
    const [showHuongDan, setShowHuongDan] = useState(false)

    return (
        <footer style={{ marginTop: 'auto', padding: '32px 24px', textAlign: 'center', borderTop: '1px solid var(--border-color)', backgroundColor: 'var(--bg-main)', color: 'var(--text-muted)', fontSize: '14px' }}>
            <p style={{ margin: '0 0 12px 0' }}>© 2026 Hệ Thống Đặt Chỗ Ngồi. Quản lý sự kiện thông minh.</p>
            <div style={{ display: 'flex', justifyContent: 'center', gap: '24px' }}>
                <span onClick={() => setShowGioiThieu(true)} style={{ color: 'var(--text-muted)', cursor: 'pointer', transition: 'color 0.2s' }} onMouseEnter={e => e.target.style.color = 'var(--primary)'} onMouseLeave={e => e.target.style.color = 'var(--text-muted)'}>Giới thiệu</span>
                <span onClick={() => setShowHuongDan(true)} style={{ color: 'var(--text-muted)', cursor: 'pointer', transition: 'color 0.2s' }} onMouseEnter={e => e.target.style.color = 'var(--primary)'} onMouseLeave={e => e.target.style.color = 'var(--text-muted)'}>Hướng dẫn sử dụng</span>
            </div>

            {/* Pop-up Giới thiệu */}
            {showGioiThieu && (
                <div onClick={() => setShowGioiThieu(false)} style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.6)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 9999, backdropFilter: 'blur(4px)' }}>
                    <div onClick={(e) => e.stopPropagation()} style={{ backgroundColor: 'var(--card-bg)', padding: '32px', borderRadius: '16px', width: '100%', maxWidth: '450px', border: '1px solid var(--border-color)', position: 'relative', textAlign: 'left' }}>
                        <button onClick={() => setShowGioiThieu(false)} className="btn-effect" style={{ position: 'absolute', top: '16px', right: '16px', background: 'transparent', border: 'none', fontSize: '20px', cursor: 'pointer', color: 'var(--text-muted)' }}>✖</button>
                        <h2 style={{ marginTop: 0, marginBottom: '16px', fontSize: '24px', color: 'var(--text-main)' }}>Về dự án này 🚀</h2>
                        <p style={{ lineHeight: '1.6', color: 'var(--text-muted)', fontSize: '15px' }}>
                            <strong>Hệ Thống Đặt Chỗ Ngồi</strong> là một dự án mini Fullstack (React.js & Spring Boot) được xây dựng nhằm mục đích học tập thao tác CRUD, quản lý logic cơ sở dữ liệu và xây dựng giao diện người dùng tương tác.
                        </p>
                        <div style={{ padding: '16px', backgroundColor: 'rgba(147, 51, 234, 0.1)', borderRadius: '8px', border: '1px solid rgba(147, 51, 234, 0.3)', marginTop: '24px' }}>
                            <p style={{ margin: 0, lineHeight: '1.6', color: 'var(--text-main)', fontSize: '14px' }}>
                                💡 <strong>Đặc biệt cảm ơn:</strong> Sự đồng hành của người bạn AI - <strong>Gemini</strong>, đã nhiệt tình hỗ trợ tư duy logic, viết code giao diện và cùng tác giả "bắt bug" ròng rã từ những bước sơ khai cho đến khi hoàn thiện!
                            </p>
                        </div>
                    </div>
                </div>
            )}

            {/* Pop-up Hướng dẫn */}
            {showHuongDan && (
                <div onClick={() => setShowHuongDan(false)} style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.6)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 9999, backdropFilter: 'blur(4px)' }}>
                    <div onClick={(e) => e.stopPropagation()} style={{ backgroundColor: 'var(--card-bg)', padding: '32px', borderRadius: '16px', width: '100%', maxWidth: '450px', border: '1px solid var(--border-color)', position: 'relative', textAlign: 'left' }}>
                        <button onClick={() => setShowHuongDan(false)} className="btn-effect" style={{ position: 'absolute', top: '16px', right: '16px', background: 'transparent', border: 'none', fontSize: '20px', cursor: 'pointer', color: 'var(--text-muted)' }}>✖</button>
                        <h2 style={{ marginTop: 0, marginBottom: '24px', fontSize: '24px', color: 'var(--text-main)' }}>Hướng dẫn nhanh 📖</h2>
                        <ul style={{ paddingLeft: '20px', color: 'var(--text-muted)', lineHeight: '1.8', fontSize: '15px', margin: 0 }}>
                            <li style={{ marginBottom: '12px' }}><strong style={{ color: 'var(--text-main)' }}>Tạo phòng:</strong> Đăng nhập, bấm "Tạo phòng mới" ở Trang chủ, nhập tên và số hàng/cột (Tối đa 50x50).</li>
                            <li style={{ marginBottom: '12px' }}><strong style={{ color: 'var(--text-main)' }}>Đặt ghế (Guest):</strong> Vào phòng bằng ID, bấm vào ghế trống. Hệ thống sẽ giữ ghế cho bạn trong 5 phút để xác nhận.</li>
                            <li style={{ marginBottom: '12px' }}><strong style={{ color: 'var(--text-main)' }}>Quản lý (Owner):</strong> Chủ phòng có thể bấm vào ghế trống để "Đặt hộ" ai đó, hoặc bấm vào ghế đã đặt để "Hủy vé".</li>
                            <li><strong style={{ color: 'var(--text-main)' }}>Phân quyền:</strong> Owner dùng bảng bên trái trong phòng để nâng cấp người dùng khác thành Admin hoặc hạ xuống Guest.</li>
                        </ul>
                    </div>
                </div>
            )}
        </footer>
    )
}

/* =========================================================================
   2. TRANG LOBBY
   ========================================================================= */
function LobbyPage() {
    const { user } = useContext(AuthContext)
    const navigate = useNavigate()
    const [roomIdInput, setRoomIdInput] = useState('')
    const [danhSachPhong, setDanhSachPhong] = useState([])
    const [showTaoPhong, setShowTaoPhong] = useState(false)
    const [formPhong, setFormPhong] = useState({ tenPhong: '', hang: 5, cot: 5 })

    const layDanhSachPhong = () => {
        if (!user) return
        fetch('http://localhost:8080/api/phong/danh-sach-phong', { credentials: 'include' })
            .then(res => res.ok ? res.json() : [])
            .then(data => setDanhSachPhong(data))
            .catch(err => console.error('Lỗi tải phòng:', err))
    }

    useEffect(() => { layDanhSachPhong() }, [user])

    const vaoPhong = (e) => {
        e.preventDefault()
        if (!roomIdInput.trim()) return
        navigate(`/phong/${roomIdInput.split('/').pop()}`)
    }

    const xuLyTaoPhong = (e) => {
        e.preventDefault()
        fetch('http://localhost:8080/api/phong/tao-phong', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(formPhong)
        })
            .then(res => {
                if (!res.ok) throw new Error('Lỗi')
                alert('Tạo phòng thành công!')
                setShowTaoPhong(false)
                layDanhSachPhong()
            })
            .catch(() => alert('Không thể tạo phòng.'))
    }

    return (
        <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
            {user && <Sidebar />}
            <Header />

            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', paddingTop: '80px', paddingBottom: '60px', paddingLeft: '24px', paddingRight: '24px' }}>
                <h1 style={{ fontSize: '42px', marginBottom: '16px', fontWeight: '800', textAlign: 'center' }}>Hệ Thống Đặt Chỗ Ngồi</h1>
                <p style={{ color: 'var(--text-muted)', fontSize: '18px', marginBottom: '40px', textAlign: 'center', maxWidth: '600px' }}>
                    Quản lý sơ đồ phòng, theo dõi trạng thái ghế theo thời gian thực và cấp quyền linh hoạt cho sự kiện của bạn.
                </p>

                <form onSubmit={vaoPhong} style={{ display: 'flex', width: '100%', maxWidth: '600px', borderRadius: '12px', overflow: 'hidden', backgroundColor: 'var(--card-bg)', border: '1px solid var(--border-color)', boxShadow: '0 10px 25px rgba(0,0,0,0.3)' }}>
                    <input type="text" placeholder="Nhập ID phòng hoặc dán URL..." value={roomIdInput} onChange={(e) => setRoomIdInput(e.target.value)} style={{ flex: 1, padding: '20px 24px', border: 'none', fontSize: '16px', outline: 'none', backgroundColor: 'transparent', color: 'var(--text-main)' }} />
                    <button type="submit" className="btn-effect" style={{ padding: '0 32px', backgroundColor: 'var(--primary)', color: 'white', border: 'none', fontSize: '16px', fontWeight: '600', cursor: 'pointer' }}>Vào Phòng</button>
                </form>

                {!user && (
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '24px', width: '100%', maxWidth: '900px', marginTop: '80px' }}>
                        <div className="card-hover" style={{ backgroundColor: 'var(--card-bg)', padding: '32px', borderRadius: '16px', border: '1px solid var(--border-color)', textAlign: 'center' }}>
                            <div style={{ fontSize: '40px', marginBottom: '16px' }}>⚡</div>
                            <h3 style={{ fontSize: '18px', margin: '0 0 12px 0' }}>Cập nhật Real-time</h3>
                            <p style={{ color: 'var(--text-muted)', fontSize: '14px', lineHeight: '1.6', margin: 0 }}>Trạng thái ghế được đồng bộ ngay lập tức, ngăn chặn tình trạng đặt trùng lặp.</p>
                        </div>
                        <div className="card-hover" style={{ backgroundColor: 'var(--card-bg)', padding: '32px', borderRadius: '16px', border: '1px solid var(--border-color)', textAlign: 'center' }}>
                            <div style={{ fontSize: '40px', marginBottom: '16px' }}>🛡️</div>
                            <h3 style={{ fontSize: '18px', margin: '0 0 12px 0' }}>Bảo mật & Phân quyền</h3>
                            <p style={{ color: 'var(--text-muted)', fontSize: '14px', lineHeight: '1.6', margin: 0 }}>Quản lý quyền Owner, Admin, Guest rõ ràng. Chủ phòng có toàn quyền kiểm soát.</p>
                        </div>
                        <div className="card-hover" style={{ backgroundColor: 'var(--card-bg)', padding: '32px', borderRadius: '16px', border: '1px solid var(--border-color)', textAlign: 'center' }}>
                            <div style={{ fontSize: '40px', marginBottom: '16px' }}>📊</div>
                            <h3 style={{ fontSize: '18px', margin: '0 0 12px 0' }}>Thống kê trực quan</h3>
                            <p style={{ color: 'var(--text-muted)', fontSize: '14px', lineHeight: '1.6', margin: 0 }}>Nắm bắt ngay số lượng ghế trống, đang giữ và đã đặt thông qua Dashboard tiện lợi.</p>
                        </div>
                    </div>
                )}

                {user && (
                    <div style={{ marginTop: '80px', width: '100%', maxWidth: '1000px' }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '2px solid var(--border-color)', paddingBottom: '12px', marginBottom: '30px' }}>
                            <h3 style={{ fontSize: '20px', margin: 0 }}>📚 Các phòng bạn quản lý</h3>
                            <button onClick={() => setShowTaoPhong(true)} className="btn-effect" style={{ padding: '10px 20px', backgroundColor: 'var(--success)', color: 'white', border: 'none', borderRadius: '8px', cursor: 'pointer', fontWeight: 'bold' }}>+ Tạo phòng mới</button>
                        </div>
                        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '24px' }}>
                            {danhSachPhong.map((phong) => {
                                const roomId = phong.id || phong.phongId
                                return (
                                    <div key={roomId} onClick={() => navigate(`/phong/${roomId}`)} className="card-hover" style={{ backgroundColor: 'var(--card-bg)', borderRadius: '16px', padding: '30px 24px', display: 'flex', flexDirection: 'column', alignItems: 'center', cursor: 'pointer', border: '1px solid var(--border-color)' }}>
                                        <span style={{ fontSize: '48px', marginBottom: '16px' }}>🚪</span>
                                        <h4 style={{ margin: '0 0 12px 0', fontSize: '18px' }}>{phong.tenPhong}</h4>

                                        {/* Đã thêm fallback ?? 0 để chống vỡ layout do mất số */}
                                        <div style={{ fontSize: '14px', fontWeight: 'bold', display: 'flex', gap: '16px', marginTop: '8px' }}>
                                            <span style={{color: 'var(--success)'}}>● {phong.soLuongGheTrong ?? 0}</span>
                                            <span style={{color: 'var(--warning)'}}>● {phong.soLuongGheDangGiu ?? 0}</span>
                                            <span style={{color: 'var(--danger)'}}>● {phong.soLuongGheDaDat ?? 0}</span>
                                        </div>
                                    </div>
                                )
                            })}
                        </div>
                    </div>
                )}

                {showTaoPhong && (
                    <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.6)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000, backdropFilter: 'blur(4px)' }}>
                        <div style={{ backgroundColor: 'var(--card-bg)', padding: '32px', borderRadius: '16px', width: '100%', maxWidth: '400px', border: '1px solid var(--border-color)' }}>
                            <h2 style={{ marginTop: 0, marginBottom: '24px' }}>Tạo phòng mới</h2>
                            <form onSubmit={xuLyTaoPhong} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                                <div><label style={{ display: 'block', marginBottom: '8px', fontSize: '14px', fontWeight: 'bold' }}>Tên phòng</label><input type="text" value={formPhong.tenPhong} onChange={e => setFormPhong({...formPhong, tenPhong: e.target.value})} required style={{ width: '100%', padding: '12px', borderRadius: '8px', border: '1px solid var(--border-color)', backgroundColor: 'transparent', color: 'var(--text-main)', boxSizing: 'border-box' }} /></div>
                                <div style={{ display: 'flex', gap: '16px' }}>
                                    <div style={{ flex: 1 }}><label style={{ display: 'block', marginBottom: '8px', fontSize: '14px', fontWeight: 'bold' }}>Số hàng (Max 50)</label><input type="number" max="50" min="1" value={formPhong.hang} onChange={e => setFormPhong({...formPhong, hang: parseInt(e.target.value)})} required style={{ width: '100%', padding: '12px', borderRadius: '8px', border: '1px solid var(--border-color)', backgroundColor: 'transparent', color: 'var(--text-main)', boxSizing: 'border-box' }} /></div>
                                    <div style={{ flex: 1 }}><label style={{ display: 'block', marginBottom: '8px', fontSize: '14px', fontWeight: 'bold' }}>Số cột (Max 50)</label><input type="number" max="50" min="1" value={formPhong.cot} onChange={e => setFormPhong({...formPhong, cot: parseInt(e.target.value)})} required style={{ width: '100%', padding: '12px', borderRadius: '8px', border: '1px solid var(--border-color)', backgroundColor: 'transparent', color: 'var(--text-main)', boxSizing: 'border-box' }} /></div>
                                </div>
                                <div style={{ display: 'flex', gap: '12px', marginTop: '16px' }}>
                                    <button type="button" onClick={() => setShowTaoPhong(false)} className="btn-effect" style={{ flex: 1, padding: '12px', border: 'none', borderRadius: '8px', cursor: 'pointer', backgroundColor: 'var(--border-color)', color: 'var(--text-main)', fontWeight: 'bold' }}>Hủy</button>
                                    <button type="submit" className="btn-effect" style={{ flex: 1, padding: '12px', border: 'none', borderRadius: '8px', cursor: 'pointer', backgroundColor: 'var(--primary)', color: 'white', fontWeight: 'bold' }}>Tạo</button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}
            </div>

            <Footer />
        </div>
    )
}

/* =========================================================================
   3. TRANG ĐĂNG NHẬP / ĐĂNG KÝ
   ========================================================================= */
function AuthPage({ mode }) {
    const { login } = useContext(AuthContext)
    const navigate = useNavigate()

    const [formData, setFormData] = useState({ username: '', email: '', password: '' })
    const [fieldErrors, setFieldErrors] = useState({})
    const [generalError, setGeneralError] = useState('')

    useEffect(() => {
        const timer = setTimeout(() => {
            const errors = { ...fieldErrors }

            if (mode === 'register' && formData.email) {
                if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
                    errors.email = 'Email không hợp lệ!'
                } else {
                    delete errors.email
                }
            }

            if (formData.password) {
                if (formData.password.length < 6) {
                    errors.password = 'Password phải trên 6 ký tự!'
                } else {
                    delete errors.password
                }
            }

            setFieldErrors(errors)
            setGeneralError('')
        }, 500)

        return () => clearTimeout(timer)
    }, [formData.email, formData.password, mode])

    const handleSubmit = async (e) => {
        e.preventDefault()

        if (Object.keys(fieldErrors).length > 0) return

        const url = mode === 'login' ? 'http://localhost:8080/api/tai-khoan/dang-nhap' : 'http://localhost:8080/api/tai-khoan/tao-tai-khoan'

        try {
            const res = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify(formData)
            })

            if (!res.ok) {
                try {
                    const errData = await res.json()
                    if (errData.message) {
                        setGeneralError(errData.message)
                        setFieldErrors({})
                    } else if (errData && Object.keys(errData).length > 0) {
                        setFieldErrors(errData)
                        setGeneralError('')
                    }
                } catch (parseError) {
                    setGeneralError('Sai thông tin hoặc lỗi máy chủ!')
                }
                return
            }

            if (mode === 'login') {
                login(formData.username)
                navigate('/')
            } else {
                alert('Đăng ký thành công! Vui lòng đăng nhập.')
                navigate('/login')
            }

        } catch (err) {
            setGeneralError('Không thể kết nối đến máy chủ!')
        }
    }

    return (
        <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
            <Header />
            <div style={{ flex: 1, display: 'flex', justifyContent: 'center', alignItems: 'center', padding: '24px' }}>
                <div style={{ width: '100%', maxWidth: '420px', padding: '48px 40px', backgroundColor: 'var(--card-bg)', borderRadius: '24px', border: '1px solid var(--border-color)', boxShadow: '0 25px 50px -12px rgba(0,0,0,0.5)' }}>
                    <h2 style={{ textAlign: 'center', marginBottom: '32px' }}>{mode === 'login' ? 'Đăng Nhập' : 'Tạo Tài Khoản'}</h2>

                    {generalError && (
                        <div style={{ padding: '12px', backgroundColor: 'rgba(239, 68, 68, 0.1)', color: 'var(--danger)', borderRadius: '8px', marginBottom: '24px', fontSize: '14px', border: '1px solid var(--danger)' }}>
                            {generalError}
                        </div>
                    )}

                    <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                        {mode === 'register' && (
                            <div>
                                <label style={{ display: 'block', marginBottom: '8px', fontSize: '14px', fontWeight: 'bold' }}>Email</label>
                                <input
                                    type="email" required
                                    onChange={e => setFormData({...formData, email: e.target.value})}
                                    style={{ width: '100%', padding: '14px', borderRadius: '12px', border: fieldErrors.email ? '1px solid var(--danger)' : '1px solid var(--border-color)', background: 'transparent', color: 'var(--text-main)', boxSizing: 'border-box' }}
                                />
                                {fieldErrors.email && <span style={{ color: 'var(--danger)', fontSize: '12px', marginTop: '6px', display: 'block' }}>{fieldErrors.email}</span>}
                            </div>
                        )}

                        <div>
                            <label style={{ display: 'block', marginBottom: '8px', fontSize: '14px', fontWeight: 'bold' }}>Tên đăng nhập</label>
                            <input
                                type="text" required
                                onChange={e => setFormData({...formData, username: e.target.value})}
                                style={{ width: '100%', padding: '14px', borderRadius: '12px', border: fieldErrors.username ? '1px solid var(--danger)' : '1px solid var(--border-color)', background: 'transparent', color: 'var(--text-main)', boxSizing: 'border-box' }}
                            />
                            {fieldErrors.username && <span style={{ color: 'var(--danger)', fontSize: '12px', marginTop: '6px', display: 'block' }}>{fieldErrors.username}</span>}
                        </div>

                        <div>
                            <label style={{ display: 'block', marginBottom: '8px', fontSize: '14px', fontWeight: 'bold' }}>Mật khẩu</label>
                            <input
                                type="password" required
                                onChange={e => setFormData({...formData, password: e.target.value})}
                                style={{ width: '100%', padding: '14px', borderRadius: '12px', border: fieldErrors.password ? '1px solid var(--danger)' : '1px solid var(--border-color)', background: 'transparent', color: 'var(--text-main)', boxSizing: 'border-box' }}
                            />
                            {fieldErrors.password && <span style={{ color: 'var(--danger)', fontSize: '12px', marginTop: '6px', display: 'block' }}>{fieldErrors.password}</span>}
                        </div>

                        <button
                            type="submit"
                            className="btn-effect"
                            disabled={Object.keys(fieldErrors).length > 0}
                            style={{ padding: '16px', backgroundColor: 'var(--primary)', color: 'white', border: 'none', borderRadius: '12px', cursor: Object.keys(fieldErrors).length > 0 ? 'not-allowed' : 'pointer', fontWeight: 'bold', marginTop: '8px', opacity: Object.keys(fieldErrors).length > 0 ? 0.6 : 1 }}
                        >
                            {mode === 'login' ? 'Vào hệ thống' : 'Đăng ký ngay'}
                        </button>
                    </form>

                    <div style={{ marginTop: '24px', textAlign: 'center', color: 'var(--text-muted)', fontSize: '14px' }}>
                        {mode === 'login' ? <p>Chưa có tài khoản? <Link to="/register" style={{ color: 'var(--primary)', textDecoration: 'none', fontWeight: 'bold' }}>Đăng ký</Link></p> : <p>Đã có tài khoản? <Link to="/login" style={{ color: 'var(--primary)', textDecoration: 'none', fontWeight: 'bold' }}>Đăng nhập</Link></p>}
                    </div>
                </div>
            </div>
            <Footer />
        </div>
    )
}

/* =========================================================================
   4. TRANG SƠ ĐỒ GHẾ
   ========================================================================= */
function DatGhePage() {
    const { phongId } = useParams()
    const navigate = useNavigate()
    const { user, addHistory } = useContext(AuthContext)

    const [soDoPhong, setSoDoPhong] = useState(null)
    const [gheDangChon, setGheDangChon] = useState(null)
    const [thongTinPhien, setThongTinPhien] = useState(null)
    const [hienPopUp, setHienPopUp] = useState(false)
    const [thoiGianConLai, setThoiGianConLai] = useState(300)

    const [datHoUsername, setDatHoUsername] = useState('')
    const [ghiChuOwner, setGhiChuOwner] = useState('')
    const [phanQuyenUsername, setPhanQuyenUsername] = useState('')

    const dinhDangThoiGian = (tongGiay) => {
        const phut = Math.floor(tongGiay / 60)
        const giay = tongGiay % 60
        return `${phut.toString().padStart(2, '0')}:${giay.toString().padStart(2, '0')}`
    }

    const layMauSacGhe = (ghe) => {
        const laGheCuaToi = ghe.trangThai === 'PHIEN_CUA_TOI' || ghe.trangThai === 'GHE_CUA_TOI' || gheDangChon?.gheId === ghe.gheId
        if (laGheCuaToi) return { nen: 'var(--purple)', chu: '#ffffff' }
        switch (ghe.trangThai) {
            case 'TRONG': return { nen: 'var(--success)', chu: '#ffffff' }
            case 'DA_DAT': return { nen: 'var(--danger)', chu: '#ffffff' }
            case 'DANG_GIU': return { nen: 'var(--warning)', chu: '#ffffff' }
            default: return { nen: 'var(--border-color)', chu: 'var(--text-muted)' }
        }
    }

    const taiSoDoPhong = () => {
        fetch(`http://localhost:8080/api/phong/${phongId}/so-do`, { credentials: 'include' })
            .then(res => res.status === 401 || res.status === 403 ? navigate('/login') : res.json())
            .then(data => {
                if (data && data.phongId) {
                    setSoDoPhong(data)
                    addHistory({ phongId: data.phongId, tenPhong: data.tenPhong })
                }
            })
    }

    useEffect(() => { taiSoDoPhong() }, [phongId, navigate, addHistory])

    const xuLyGiuGhe = (ghe) => {
        if (soDoPhong?.owner && ghe.trangThai === 'DA_DAT') {
            setGheDangChon(ghe); setHienPopUp(true); return
        }
        if (ghe.trangThai === 'GHE_CUA_TOI' || ghe.trangThai === 'PHIEN_CUA_TOI' || (soDoPhong?.owner && ghe.trangThai === 'TRONG')) {
            setGheDangChon(ghe); setThongTinPhien({ phienId: ghe.phienId }); setHienPopUp(true); return
        }

        fetch('http://localhost:8080/api/dat-ghe/giu-ghe', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ gheNgoiId: ghe.gheId }),
        })
            .then(res => { if (!res.ok) throw new Error('Lỗi'); return res.json() })
            .then(phienData => { setThongTinPhien(phienData); setGheDangChon(ghe); setThoiGianConLai(300); setHienPopUp(true) })
            .catch(() => alert('Không thể giữ ghế này!'))
    }

    const xuLyHuyGiu = () => {
        if (!thongTinPhien) return
        fetch(`http://localhost:8080/api/dat-ghe/huy-giu-ghe/${thongTinPhien.phienId}`, { method: 'POST', credentials: 'include' })
            .then(res => { if (res.ok) { setHienPopUp(false); setGheDangChon(null); setThongTinPhien(null); taiSoDoPhong() } })
    }

    const xuLyHuyDatVe = () => {
        if (!thongTinPhien) return
        if (!window.confirm('Bạn có chắc muốn hủy vé này?')) return
        fetch(`http://localhost:8080/api/dat-ghe/huy-ghe/${thongTinPhien.phienId}`, { method: 'POST', credentials: 'include' })
            .then(res => { if (res.ok) { alert('Hủy vé thành công!'); setHienPopUp(false); setGheDangChon(null); setThongTinPhien(null); taiSoDoPhong() } })
    }

    const xuLyOwnerHuyGhe = () => {
        if (!window.confirm('Hủy vé của người này?')) return
        fetch(`http://localhost:8080/api/quan-ly/huy-ghe`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ phongId: parseInt(phongId), gheId: gheDangChon.gheId })
        })
            .then(res => { if (res.ok) { alert('Đã hủy vé!'); setHienPopUp(false); setGheDangChon(null); taiSoDoPhong() } })
    }

    const xuLyOwnerDatHo = () => {
        fetch(`http://localhost:8080/api/quan-ly/dat-ghe`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ phongId: parseInt(phongId), gheId: gheDangChon.gheId, datHoUsername, ghiChu: ghiChuOwner })
        })
            .then(res => { if (res.ok) { alert('Đặt hộ thành công!'); setHienPopUp(false); setGheDangChon(null); taiSoDoPhong(); setDatHoUsername(''); setGhiChuOwner('') } })
    }

    const xuLyXacNhanDat = () => {
        if (!thongTinPhien) return
        fetch(`http://localhost:8080/api/dat-ghe/xac-nhan-ghe/${thongTinPhien.phienId}`, { method: 'POST', credentials: 'include' })
            .then(res => { if (res.ok) { alert('Đặt ghế thành công!'); setHienPopUp(false); setGheDangChon(null); setThongTinPhien(null); taiSoDoPhong() } })
    }

    const xuLyPhanQuyen = (role) => {
        if(!phanQuyenUsername.trim()) return;
        const endpoint = role === 'ADMIN' ? 'admin' : 'guest';
        fetch(`http://localhost:8080/api/quan-ly/phan-quyen/${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ username: phanQuyenUsername, phongId: parseInt(phongId) })
        })
            .then(res => {
                if(res.ok) {
                    alert(`Đã cấp quyền ${role} cho ${phanQuyenUsername} thành công!`);
                    setPhanQuyenUsername('');
                } else {
                    alert('Lỗi cấp quyền! Vui lòng kiểm tra lại Username.');
                }
            })
    }

    useEffect(() => {
        if (!hienPopUp || gheDangChon?.trangThai === 'GHE_CUA_TOI' || (soDoPhong?.owner && gheDangChon?.trangThai === 'DA_DAT') || (soDoPhong?.owner && gheDangChon?.trangThai === 'TRONG')) return
        if (thoiGianConLai <= 0) { alert('Đã hết thời gian giữ ghế!'); xuLyHuyGiu(); return }
        const demNguoc = setInterval(() => setThoiGianConLai(p => p - 1), 1000)
        return () => clearInterval(demNguoc)
    }, [hienPopUp, thoiGianConLai, gheDangChon, soDoPhong])

    if (!soDoPhong) return <div style={{ textAlign: 'center', padding: '40px' }}>Đang tải...</div>

    const soCotMax = soDoPhong.danhSachGhe.length > 0 ? Math.max(...soDoPhong.danhSachGhe.map(ghe => ghe.cot)) : 1
    const countTrong = soDoPhong.danhSachGhe.filter(g => g.trangThai === 'TRONG').length;
    const countDaDat = soDoPhong.danhSachGhe.filter(g => g.trangThai === 'DA_DAT' || g.trangThai === 'GHE_CUA_TOI').length;
    const countDangGiu = soDoPhong.danhSachGhe.filter(g => g.trangThai === 'DANG_GIU' || g.trangThai === 'PHIEN_CUA_TOI').length;

    return (
        <div style={{ display: 'flex', minHeight: '100vh', flexDirection: 'column' }}>
            {user && <Sidebar />}
            <Header />

            <div style={{ flex: 1, width: '100%', maxWidth: '1400px', margin: '0 auto', padding: '32px 24px', boxSizing: 'border-box' }}>

                <div style={{ display: 'grid', gridTemplateColumns: '260px minmax(0, 1fr) 260px', gap: '24px', width: '100%' }}>

                    <div>
                        {soDoPhong.owner && (
                            <div style={{ backgroundColor: 'var(--card-bg)', borderRadius: '16px', padding: '24px', border: '1px solid var(--border-color)', position: 'sticky', top: '100px' }}>
                                <h3 style={{ marginTop: 0, fontSize: '16px', borderBottom: '1px solid var(--border-color)', paddingBottom: '12px' }}>⚙️ Phân Quyền</h3>
                                <input type="text" placeholder="Nhập username..." value={phanQuyenUsername} onChange={e => setPhanQuyenUsername(e.target.value)} style={{ width: '100%', padding: '12px', boxSizing: 'border-box', marginBottom: '12px', borderRadius: '8px', border: '1px solid var(--border-color)', background: 'transparent', color: 'var(--text-main)' }} />
                                <div style={{ display: 'flex', gap: '8px', flexDirection: 'column' }}>
                                    <button onClick={() => xuLyPhanQuyen('ADMIN')} className="btn-effect" style={{ width: '100%', padding: '12px', backgroundColor: 'var(--primary)', color: 'white', border: 'none', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' }}>+ Cấp Admin</button>
                                    <button onClick={() => xuLyPhanQuyen('GUEST')} className="btn-effect" style={{ width: '100%', padding: '12px', backgroundColor: 'var(--danger)', color: 'white', border: 'none', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' }}>- Đổi thành Guest</button>
                                </div>
                            </div>
                        )}
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: '100%' }}>

                        <div style={{ textAlign: 'center', marginBottom: '24px' }}>
                            <h2 style={{ fontSize: '32px', margin: '0 0 8px 0' }}>{soDoPhong.tenPhong}</h2>
                            <span style={{ padding: '6px 14px', fontSize: '12px', fontWeight: 'bold', borderRadius: '20px', backgroundColor: soDoPhong.owner ? 'rgba(59, 130, 246, 0.1)' : 'rgba(148, 163, 184, 0.1)', color: soDoPhong.owner ? 'var(--primary)' : 'var(--text-muted)' }}>
                {soDoPhong.owner ? '👑 Owner' : '👤 Guest'}
              </span>
                            <div style={{ fontSize: '14px', fontWeight: 'bold', display: 'flex', gap: '20px', justifyContent: 'center', marginTop: '20px' }}>
                                <span style={{color: 'var(--success)'}}>● {countTrong ?? 0}</span>
                                <span style={{color: 'var(--warning)'}}>● {countDangGiu ?? 0}</span>
                                <span style={{color: 'var(--danger)'}}>● {countDaDat ?? 0}</span>
                            </div>
                        </div>

                        <div style={{ width: '100%', overflowX: 'auto', paddingBottom: '16px', textAlign: 'center' }}>
                            <div style={{
                                display: 'inline-grid',
                                gridTemplateColumns: `repeat(${soCotMax}, minmax(${soDoPhong.owner ? '70px' : '50px'}, auto))`, gap: '12px',
                                padding: '32px', backgroundColor: 'var(--card-bg)', borderRadius: '16px', border: '1px solid var(--border-color)',
                                textAlign: 'left'
                            }}>
                                {soDoPhong.danhSachGhe.map(ghe => {
                                    const biKhoa = !soDoPhong?.owner && ghe.trangThai !== 'TRONG' && ghe.trangThai !== 'PHIEN_CUA_TOI' && ghe.trangThai !== 'GHE_CUA_TOI'
                                    const mauSac = layMauSacGhe(ghe)

                                    return (
                                        <button
                                            key={ghe.gheId}
                                            onClick={() => xuLyGiuGhe(ghe)}
                                            disabled={hienPopUp || biKhoa}
                                            className="btn-effect"
                                            style={{
                                                padding: '12px 8px', minWidth: soDoPhong.owner ? '70px' : '50px', backgroundColor: mauSac.nen, border: 'none', borderRadius: '8px',
                                                color: mauSac.chu, fontWeight: 'bold', boxShadow: '0 4px 6px rgba(0,0,0,0.2)',
                                                cursor: (hienPopUp || biKhoa) ? 'not-allowed' : 'pointer', opacity: (hienPopUp && !gheDangChon) ? 0.6 : 1,
                                                display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'
                                            }}>
                                            <span style={{ fontSize: '15px' }}>{ghe.soGhe}</span>
                                            {soDoPhong.owner && ghe.tenNguoiDat && (
                                                <span style={{ fontSize: '11px', marginTop: '6px', opacity: 0.9, fontWeight: 'normal', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', maxWidth: '60px' }}>
                          {ghe.tenNguoiDat}
                        </span>
                                            )}
                                        </button>
                                    )
                                })}
                            </div>
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'center', gap: '24px', marginTop: '16px', padding: '16px 32px', backgroundColor: 'var(--card-bg)', borderRadius: '12px', border: '1px solid var(--border-color)', flexWrap: 'wrap' }}>
                            <span style={{display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', fontWeight: '500'}}><div style={{width:'14px', height:'14px', borderRadius:'50%', backgroundColor:'var(--success)'}}></div> Trống</span>
                            <span style={{display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', fontWeight: '500'}}><div style={{width:'14px', height:'14px', borderRadius:'50%', backgroundColor:'var(--warning)'}}></div> Đang giữ</span>
                            <span style={{display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', fontWeight: '500'}}><div style={{width:'14px', height:'14px', borderRadius:'50%', backgroundColor:'var(--danger)'}}></div> Đã mua</span>
                            <span style={{display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', fontWeight: '500'}}><div style={{width:'14px', height:'14px', borderRadius:'50%', backgroundColor:'var(--purple)'}}></div> Của bạn</span>
                        </div>
                    </div>

                    <div></div>

                </div>

                {hienPopUp && (
                    <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.7)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000, backdropFilter: 'blur(4px)' }}>
                        <div style={{ backgroundColor: 'var(--card-bg)', padding: '32px', borderRadius: '16px', width: '100%', maxWidth: '340px', border: '1px solid var(--border-color)', textAlign: 'center', boxShadow: '0 25px 50px -12px rgba(0,0,0,0.5)' }}>

                            {soDoPhong?.owner && gheDangChon?.trangThai === 'DA_DAT' && gheDangChon?.trangThai !== 'GHE_CUA_TOI' ? (
                                    <div>
                                        <h3 style={{marginTop: 0}}>Quản lý ghế (Owner)</h3>
                                        <p style={{ lineHeight: '1.5' }}>Ghế <strong style={{color: 'var(--danger)', fontSize: '18px'}}>{gheDangChon?.soGhe}</strong> đã được đặt bởi <strong>{gheDangChon?.tenNguoiDat}</strong>.</p>
                                        <div style={{ display: 'flex', gap: '12px', marginTop: '24px' }}>
                                            <button onClick={() => setHienPopUp(false)} className="btn-effect" style={{ flex: 1, padding: '12px', backgroundColor: 'var(--border-color)', color: 'var(--text-main)', border: 'none', borderRadius: '8px', fontWeight: 'bold' }}>Đóng</button>
                                            <button onClick={xuLyOwnerHuyGhe} className="btn-effect" style={{ flex: 1, padding: '12px', backgroundColor: 'var(--danger)', color: 'white', border: 'none', borderRadius: '8px', fontWeight: 'bold' }}>Hủy vé này</button>
                                        </div>
                                    </div>
                                ) :

                                soDoPhong?.owner && gheDangChon?.trangThai === 'TRONG' ? (
                                        <div>
                                            <h3 style={{marginTop: 0}}>Đặt hộ (Owner)</h3>
                                            <p>Ghế: <strong style={{color: 'var(--purple)', fontSize: '20px'}}>{gheDangChon?.soGhe}</strong></p>
                                            <input type="text" placeholder="Username người đặt hộ..." value={datHoUsername} onChange={(e) => setDatHoUsername(e.target.value)} style={{ width: '100%', padding: '12px', boxSizing: 'border-box', marginBottom: '12px', borderRadius: '8px', border: '1px solid var(--border-color)', background: 'transparent', color: 'var(--text-main)' }}/>
                                            <input type="text" placeholder="Ghi chú (Tùy chọn)..." value={ghiChuOwner} onChange={(e) => setGhiChuOwner(e.target.value)} style={{ width: '100%', padding: '12px', boxSizing: 'border-box', borderRadius: '8px', border: '1px solid var(--border-color)', background: 'transparent', color: 'var(--text-main)' }}/>
                                            <div style={{ display: 'flex', gap: '12px', marginTop: '24px' }}>
                                                <button onClick={() => {setHienPopUp(false); setDatHoUsername(''); setGhiChuOwner('')}} className="btn-effect" style={{ flex: 1, padding: '12px', backgroundColor: 'var(--border-color)', color: 'var(--text-main)', border: 'none', borderRadius: '8px', fontWeight: 'bold' }}>Đóng</button>
                                                <button onClick={xuLyOwnerDatHo} className="btn-effect" style={{ flex: 1, padding: '12px', backgroundColor: 'var(--success)', color: 'white', border: 'none', borderRadius: '8px', fontWeight: 'bold' }}>Đặt ngay</button>
                                            </div>
                                        </div>
                                    ) :

                                    gheDangChon?.trangThai === 'GHE_CUA_TOI' ? (
                                        <div>
                                            <h3 style={{marginTop: 0}}>Vé Của Bạn</h3>
                                            <p>Ghế: <strong style={{color: 'var(--purple)', fontSize: '20px'}}>{gheDangChon?.soGhe}</strong></p>
                                            <div style={{ display: 'flex', gap: '12px', marginTop: '24px' }}>
                                                <button onClick={() => setHienPopUp(false)} className="btn-effect" style={{ flex: 1, padding: '12px', backgroundColor: 'var(--border-color)', color: 'var(--text-main)', border: 'none', borderRadius: '8px', fontWeight: 'bold' }}>Đóng</button>
                                                <button onClick={xuLyHuyDatVe} className="btn-effect" style={{ flex: 1, padding: '12px', backgroundColor: 'var(--danger)', color: 'white', border: 'none', borderRadius: '8px', fontWeight: 'bold' }}>Hủy vé</button>
                                            </div>
                                        </div>
                                    ) : (
                                        <div>
                                            <h3 style={{marginTop: 0}}>Phiên Giữ Chỗ</h3>
                                            <p>Ghế: <strong style={{color: 'var(--purple)', fontSize: '20px'}}>{gheDangChon?.soGhe}</strong></p>
                                            <p style={{ fontSize: '24px', color: thoiGianConLai <= 60 ? 'var(--danger)' : 'var(--success)', fontWeight: '800', margin: '20px 0' }}>⏳ {dinhDangThoiGian(thoiGianConLai)}</p>
                                            <div style={{ display: 'flex', gap: '12px', marginTop: '24px' }}>
                                                <button onClick={xuLyHuyGiu} className="btn-effect" style={{ flex: 1, padding: '12px', backgroundColor: 'rgba(239, 68, 68, 0.1)', color: 'var(--danger)', border: 'none', borderRadius: '8px', fontWeight: 'bold' }}>Hủy giữ</button>
                                                <button onClick={xuLyXacNhanDat} className="btn-effect" style={{ flex: 1, padding: '12px', backgroundColor: 'var(--success)', color: 'white', border: 'none', borderRadius: '8px', fontWeight: 'bold' }}>Xác nhận</button>
                                            </div>
                                        </div>
                                    )}
                        </div>
                    </div>
                )}
            </div>

            <Footer />
        </div>
    )
}

export default function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<LobbyPage />} />
                    <Route path="/login" element={<AuthPage mode="login" />} />
                    <Route path="/register" element={<AuthPage mode="register" />} />
                    <Route path="/phong/:phongId" element={<DatGhePage />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    )
}