package com.example.demo.queryzs;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SpecificationBuilder {
  
  public static <T> Specification<T> build(Class<T> clz, QueryCondition...conditions) {
    return (root, query, cb) -> {
      Map<String, From> conditionName2FromMap = buildConditionName2FromMap(root, conditions);
      List<Predicate> predicates = getPredicates(query, cb, conditionName2FromMap, conditions);
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
  
  public static <T> Specification<T> build(Class<T> clz, Map<String, From> conditionName2FromMap, QueryCondition...conditions) {
    return (root, query, cb) -> {
      List<Predicate> predicates = getPredicates(query, cb, conditionName2FromMap, conditions);
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
  
  private static List<Predicate> getPredicates(CriteriaQuery<?> query, CriteriaBuilder cb, Map<String, From> conditionName2FromMap, QueryCondition[] conditions) {
    List<Predicate> predicates = new ArrayList<>();
    
    for (QueryCondition queryCondition : conditions) {
      // 是否distinct
      if (queryCondition.isRoot() && queryCondition.isDistinct()) {
        query.distinct(true);
      }
      
      AbstractCondition condition = queryCondition.getCondition();
      if (condition == null) {
        continue;
      }
      
      From from = conditionName2FromMap.get(queryCondition.getConditionName());
      // 遍历条件包装类的所有属性
      for (ConditionProp prop : PropUtils.getProps(condition)) {
        // 以下划线开头的是AbstractQueryCondition的属性，不必转为查询条件
        if (prop.getNameWithSuffix().charAt(0) != '_') {
          // 将包装类的属性转为查询条件(即predicate)
          Predicate predicate = PredicateBuilder.build(from, cb, prop.getNameWithSuffix(), prop.getValue());
          if (predicate != null) {
            predicates.add(predicate);
          }
        }
      }

      // 将自定义的customPredicates加入查询条件
      List<CustomPredicate> customPredicates = condition.get_customPredicates();
      if (!CollectionUtils.isEmpty(customPredicates)) {
        for (CustomPredicate customPredicate : customPredicates) {
          predicates.add(customPredicate.to(from, cb));
        }
      }

    }
    return predicates;
  }
  
  /**
   * 创建map，同时构造好join关系(调用root.join(), root.fetch()方法)
   * key: conditionName, value: condition对应的表的From对象
   * @param root
   * @param queryConditions
   * @return
   */
  static Map<String, From> buildConditionName2FromMap(From root, QueryCondition... queryConditions) {
    Map<String, From> map = new HashMap<>();
    Arrays
        .stream(queryConditions)
        .sorted(Comparator.comparing(QueryCondition::getConditionName))
        .forEach(queryCondition -> {
          if (map.containsKey(queryCondition.getConditionName())) {
            throw new IllegalArgumentException("同名的QueryCondition只能有一个。重复名称：" + queryCondition.getConditionName());
          } else {
            if (queryCondition.isRoot()) {
              map.put("", root);
            } else {
              String pName = queryCondition.getParentName();
              String conditionName = queryCondition.getConditionName();
              From parentFrom = map.get(pName);
              Assert.notNull(parentFrom, "构造查询条件时此conditionName无父节点，请检查代码。" + conditionName);
              From from;
              // jjzz -> jjzz, jjzz.xzqh -> xzqh
              String name2Join = conditionName.substring(conditionName.lastIndexOf('.') + 1);
              if (queryCondition.isFetch()) {
                from = (From) parentFrom.fetch(name2Join, queryCondition.getJoinType());
              } else {
                from = parentFrom.join(name2Join, queryCondition.getJoinType());
              }
              map.put(queryCondition.getConditionName(), from);
            }
          }
        });
    return map;
  }
  

}
