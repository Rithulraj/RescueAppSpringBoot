package com.example.resueapp.rescue.repository;

import com.example.resueapp.rescue.entity.ResueDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RescueRepository extends JpaRepository<ResueDetail, Long> {
    Page<ResueDetail> findByRescuerId(Long rescuerId, Pageable pageable);
}
