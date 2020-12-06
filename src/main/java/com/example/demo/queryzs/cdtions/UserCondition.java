package com.example.demo.queryzs.cdtions;

import com.example.demo.queryzs.AbstractCondition;
import com.example.demo.queryzs.CustomPredicate;
import lombok.Setter;

import java.util.List;

@Setter
public class UserCondition extends AbstractCondition {
  
  private List<Integer> userIdInList;
  
  private String nameStartWith;
  
  private Integer userIdLte;
  
  private Integer userIdGte;
  
  public void nameStartWithAOrEndWithB(String A, String B) {
    CustomPredicate cp = (root, cb) -> {
      return cb.or(
          cb.like(root.get("name"), A+"%"),
          cb.like(root.get("name"), "%"+B)
      );
    };
    addCustomPredicate(cp);
  }
  
}
