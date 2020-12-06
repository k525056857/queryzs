package com.example.demo.queryzs;

import lombok.AccessLevel;
import lombok.Getter;

import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractCondition {
  
  @Getter(AccessLevel.PACKAGE)
  private List<CustomPredicate> _customPredicates;
  protected void addCustomPredicate(CustomPredicate customPredicate) {
    if (_customPredicates == null) {
      _customPredicates = new ArrayList<>();
    }
    _customPredicates.add(customPredicate);
  }
  
  public QueryCondition root() { return root(true); }
  public QueryCondition root(boolean distinct) {
    return new QueryCondition(this, distinct);
  }
  
  public QueryCondition innerJoin(String conditionName) { return join(conditionName, JoinType.INNER); }
  public QueryCondition leftJoin(String conditionName) { return join(conditionName, JoinType.LEFT); }
  public QueryCondition rightJoin(String conditionName) { return join(conditionName, JoinType.RIGHT); }
  public QueryCondition join(String conditionName, JoinType joinType) {
    return new QueryCondition(conditionName, joinType, false, this);
  }
  
  public QueryCondition innerFetch(String conditionName) { return fetch(conditionName, JoinType.INNER); }
  public QueryCondition leftFetch(String conditionName) { return fetch(conditionName, JoinType.LEFT); }
  public QueryCondition rightFetch(String conditionName) { return fetch(conditionName, JoinType.RIGHT); }
  public QueryCondition fetch(String conditionName, JoinType joinType) {
    return new QueryCondition(conditionName, joinType, true, this);
  }
  
  public static QueryCondition justInnerJoin(String conditionName) { return justJoin(conditionName, JoinType.INNER); }
  public static QueryCondition justLeftJoin(String conditionName) { return justJoin(conditionName, JoinType.LEFT); }
  public static QueryCondition justRightJoin(String conditionName) { return justJoin(conditionName, JoinType.RIGHT); }
  public static QueryCondition justJoin(String conditionName, JoinType joinType) {
    return new QueryCondition(conditionName, joinType, false, null);
  }
  
  public static QueryCondition justLeftFetch(String conditionName) { return justFetch(conditionName, JoinType.LEFT); }
  public static QueryCondition justRightFetch(String conditionName) { return justFetch(conditionName, JoinType.RIGHT); }
  public static QueryCondition justInnerFetch(String conditionName) { return justFetch(conditionName, JoinType.INNER); }
  public static QueryCondition justFetch(String conditionName, JoinType joinType) {
    return new QueryCondition(conditionName, joinType, true, null);
  }
}
