package com.example.simplerestspec.repository;

import com.example.simplerestspec.entity.ApiSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiSpecRepository extends JpaRepository<ApiSpec, Long> {
    List<ApiSpec> findByContext_Id(Long contextId);
}
