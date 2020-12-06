package com.example.demo.queryzs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Collection;

enum WhereType {
  LIKE("Like"),
  START_WITH("StartWith"),
  END_WITH("EndWith"),
  GTE("Gte"),
  GT("Gt"),
  LTE("Lte"),
  LT("Lt"),
  IS_NULL("IsNull"), // Boolean,
  NOT_NULL("IsNotNull", "NotNull"), // Boolean,
  IN("In", "InList"),
  EQUAL("Equal", ""),
  ;
  
  private String[] suffix;
  WhereType(String ...suffix) {
    this.suffix = suffix;
  }
  
  public static WhereType getType(String propName) {
    for (WhereType type : values()) {
      for (String suffix : type.suffix) {
        if (propName.endsWith(suffix)) {
          return type;
        }
      }
    }
    // 由于endWith("")会被匹配到EQUAL，所以不会执行到这一行
    throw new IllegalArgumentException("枚举中不存在的后缀" + propName);
  }
  
  String removeSuffix(String propName) {
    for (String s : suffix) {
      if (propName.endsWith(s)) {
        return propName.substring(0, propName.length() - s.length());
      }
    }
    return propName;
  }
}

class PredicateBuilder {
  static Predicate build(Path<?> rootOrJoin, CriteriaBuilder cb, String nameWithSuffix, Object val) {
    if (val == null) return null;
    
    WhereType type = WhereType.getType(nameWithSuffix);
    String name = type.removeSuffix(nameWithSuffix);
    
    switch (type) {
    
      case EQUAL:
        return cb.equal(rootOrJoin.get(name), val);
        
      case LIKE:
        return cb.like(rootOrJoin.get(name), "%"+val+"%");
      case START_WITH:
        return cb.like(rootOrJoin.get(name), val+"%");
      case END_WITH:
        return cb.like(rootOrJoin.get(name), "%"+val);
       
      case GT:
        return cb.greaterThan(rootOrJoin.get(name), (Comparable) val);
      case GTE:
        return cb.greaterThanOrEqualTo(rootOrJoin.get(name), (Comparable) val);
      case LT:
        return cb.lessThan(rootOrJoin.get(name), (Comparable) val);
      case LTE:
        return cb.lessThanOrEqualTo(rootOrJoin.get(name), (Comparable) val);
  
      case IN:
        if (!(val instanceof Collection))
          throw new IllegalArgumentException("此属性请声明为Collection类型：" + nameWithSuffix);
        
        CriteriaBuilder.In in = cb.in(rootOrJoin.get(name));
        for (Object o : (Collection) val) {
          in.value(o);
        }
        return in;
        
      case IS_NULL:
        if (!(val instanceof Boolean))
          throw new IllegalArgumentException("此属性请声明为Boolean类型：" + nameWithSuffix);
        
        return cb.isNull(rootOrJoin.get(name));
        
      case NOT_NULL:
        if (!(val instanceof Boolean))
          throw new IllegalArgumentException("此属性请声明为Boolean类型：" + nameWithSuffix);
        
        return cb.isNotNull(rootOrJoin.get(name));
  
      default:
        throw new IllegalArgumentException("未处理的查询类型，请完善代码：" + type);
    }

  }
}
