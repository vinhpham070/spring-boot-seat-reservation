package com.example.quanly.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "chuPhong")
    List<Phong> danhSachPhong;

    public User() {};

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    // GETTER ----------------------------------------------
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Phong> getDanhSachPhong() { return danhSachPhong; }
    // SETTER ----------------------------------------------

    public void setUsername(String name) {
        this.username = name;
    }

    public void setMail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDanhSachPhong(List<Phong> danhSachPhong) { this.danhSachPhong = danhSachPhong; };
}
