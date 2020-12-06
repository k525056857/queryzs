package com.example.demo.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@ToString
@Proxy(lazy = false)
public class Xzqh {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer xzqhId;
  
  @Column
  private String name;
}
