package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Data
@ToString
@Accessors(chain = true)
@Proxy(lazy = false)
public class Jjzz {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer jjzzId;
  
  @Column
  private String name;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "xzqh_id")
  private Xzqh xzqh;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "new_xzqh_id")
  private Xzqh newXzqh;
}
