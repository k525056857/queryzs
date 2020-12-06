package com.example.demo.entity;

import lombok.Data;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Data
@ToString
@Accessors(chain = true)
@Proxy(lazy = false)
public class Cz {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer czId;
  
  @Column
  private String name;
  
}

