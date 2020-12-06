package com.example.demo.dao;

import com.example.demo.entity.Xzqh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface XzqhRepository extends JpaRepository<Xzqh, Integer>, JpaSpecificationExecutor<Xzqh> {
}
