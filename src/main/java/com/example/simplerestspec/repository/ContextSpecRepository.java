package com.example.simplerestspec.repository;

import com.example.simplerestspec.entity.ContextSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContextSpecRepository extends JpaRepository<ContextSpec, Long> {
}