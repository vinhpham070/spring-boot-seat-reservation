package com.example.quanly.repository;

import com.example.quanly.model.GheNgoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GheNgoiRepository extends  JpaRepository<GheNgoi, Long> {

}
