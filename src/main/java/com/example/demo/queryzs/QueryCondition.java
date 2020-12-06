package com.example.demo.queryzs;

import lombok.AccessLevel;
import lombok.Getter;

import javax.persistence.criteria.JoinType;


@Getter(AccessLevel.PACKAGE)
public class QueryCondition {
  
  /**
   * 描述查询条件所在的表
   * root -> ""
   * root.jjzz -> "jjzz"
   * root.jjzz.xzqh -> "jjzz.xzqh"
   */
  private String conditionName;
  
  // left，inner，right
  private JoinType joinType;
  
  // true：join并将查询结果添加到查询方法返回值里，false：仅join
  private boolean fetch;
  
  // 封装查询条件的对象
  private AbstractCondition condition;
  
  // 是否去重，默认true。仅root节点需要使用这个属性
  private boolean distinct;

  
  /**
   * 用于构造join和fetch
   */
  QueryCondition(String conditionName, JoinType joinType, boolean fetch, AbstractCondition condition) {
    this.conditionName = conditionName;
    this.joinType = joinType;
    this.fetch = fetch;
    this.condition = condition;
  }
  
  /**
   * 用于构造root
   */
  QueryCondition(AbstractCondition condition, boolean distinct) {
    this.conditionName = "";
    this.condition = condition;
    this.distinct = distinct;
  }

  /**
   * 返回父节点的conditionName，
   * 根节点("") -> null
   * 一级节点("jjzz") -> ""
   * 二级节点("jjzz.xzqh") -> "jjzz"
   * @return
   */
  String getParentName() {
    if (conditionName.equals("")) // 根节点
      return null;
    else if (!conditionName.contains(".")) // 2级节点
      return "";
    else // 3~n级节点
      return conditionName.substring(0, conditionName.lastIndexOf('.'));
  }
  
  boolean isRoot() {
    return getParentName() == null;
  }
}
