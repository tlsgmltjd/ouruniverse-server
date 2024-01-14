package com.example.ouruniverse.domain.school.repository;

import com.example.ouruniverse.domain.school.entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<SchoolEntity, Long> {
}
