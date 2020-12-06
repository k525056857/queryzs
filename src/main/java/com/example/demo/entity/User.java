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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Data
//@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Proxy(lazy = false)
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer userId;
  
  @Column
  private String name;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "jjzz_id")
  private Jjzz jjzz;
  
  @Transient
  private String xzqhName;
  
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_cz",joinColumns =
  @JoinColumn(name="user_id",referencedColumnName = "userId"),
      inverseJoinColumns = @JoinColumn(name = "cz_id",referencedColumnName = "czId"))
  private List<Cz> czs;
}
