package com.example.demo.queryzs.vo;

import com.example.demo.queryzs.Col;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {
  
  private Integer userId;
  
  @Col(sourceProp = "name")
  private String nnaammee;
  
  @Col(sourceProp = "jjzz.name")
  private String jjzzName;

  @Col(sourceProp = "jjzz.xzqh.name")
  private String xzqhName;
}
