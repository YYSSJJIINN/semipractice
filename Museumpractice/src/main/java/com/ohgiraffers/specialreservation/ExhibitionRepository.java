package com.ohgiraffers.specialreservation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Integer> {
    // 기본적인 CRUD 메소드가 제공됨
    // 예: save(), findById(), findAll(), deleteById() 등
}

