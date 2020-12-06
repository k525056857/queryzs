package com.example.demo.queryzs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Getter
class ConditionProp {
  private String nameWithSuffix;
  private Object value;
}

@AllArgsConstructor
class VoPropDef {
  @Getter // vo中的属性名
  private String alias;
  private Col colAnno;
  
  public String getConditionName() {
    // 与原属性同名
    if (colAnno == null)
      return "";
    
    String value = colAnno.sourceProp();
    // 与原属性不同名
    if (!value.contains("."))
      return "";
      // 是某个属性的属性。例："jjzz.code", "jjzz.xzqh.code"
    else
      return value.substring(0, value.lastIndexOf('.'));
  }
  
  public String getSourcePropName() {
    if (colAnno == null)
      return getAlias();
    
    String value = colAnno.sourceProp();
    return value.substring(value.lastIndexOf('.') + 1);
  }
}

public class PropUtils {
  
  static List<ConditionProp> getProps(Object condition) {
    return Arrays
        .stream(condition.getClass().getDeclaredFields())
        .map(f -> {
          f.setAccessible(true);
          try {
            return new ConditionProp(f.getName(), f.get(condition));
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
          return new ConditionProp(f.getName(), null);
        })
        .collect(Collectors.toList());
  }
  
  static List<VoPropDef> getVoPropDefs(Class<?> voType) {
    return Arrays
        .stream(voType.getDeclaredFields())
        .map(f -> new VoPropDef(f.getName(), f.getAnnotation(Col.class)))
        .collect(Collectors.toList());
  }
}
