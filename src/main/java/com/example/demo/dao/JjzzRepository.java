package com.example.demo.dao;

import com.example.demo.entity.Jjzz;
import com.example.demo.entity.User;
import com.example.demo.entity.Xzqh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JjzzRepository extends JpaRepository<Jjzz, Integer>, JpaSpecificationExecutor<Jjzz>{
}

