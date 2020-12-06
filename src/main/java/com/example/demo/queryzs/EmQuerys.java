package com.example.demo.queryzs;

import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 需要控制结果集时，使用EntityManager的查询方法
 */
public class EmQuerys {
  
  public static <T, R> List<T> emSelectList(Class<T> resClass, Class<R> rootClass, EntityManager em, QueryCondition... queryConditions) {
    return emSelectList(resClass, rootClass, em, null, queryConditions);
  }
  
  public static <T, R> List<T> emSelectList(Class<T> resClass, Class<R> rootClass, EntityManager em, Sort sort, QueryCondition... queryConditions) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> query = cb.createQuery(resClass);
    Root<R> root = query.from(rootClass);
    
    Map<String, From> map = SpecificationBuilder.buildConditionName2FromMap(root, queryConditions);
    
    Predicate predicate = SpecificationBuilder.build(rootClass, map, queryConditions).toPredicate(root, query, cb);
    query.where(predicate);
    
    Selection[] selects = getSelects(resClass, map);
    query.select(cb.construct(resClass, selects));
  
    if (sort != null) {
      List<Order> orders = springDataSort2HibernateOrders(sort, map);
      query.orderBy(orders);
    }
    
    TypedQuery<T> typedQuery = em.createQuery(query);
    
    return typedQuery.getResultList();
  }
  
  public static <T, R> Page<T> emSelectPage(
      Class<T> resClass, Class<R> rootClass,
      EntityManager em,
      int pageNum, int pageSize,
      QueryCondition... queryConditions)
  {
    return emSelectPage(resClass, rootClass, em, pageNum, pageSize, null, queryConditions);
  }
  
  public static <T, R> Page<T> emSelectPage(
      Class<T> resClass, Class<R> rootClass,
      EntityManager em,
      int pageNum, int pageSize, Sort sort,
      QueryCondition... queryConditions)
  {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> query = cb.createQuery(resClass);
    Root<R> root = query.from(rootClass);
  
    Map<String, From> map = SpecificationBuilder.buildConditionName2FromMap(root, queryConditions);
  
    Predicate predicate = SpecificationBuilder.build(rootClass, map, queryConditions).toPredicate(root, query, cb);
    query.where(predicate);
  
    Selection[] selects = getSelects(resClass, map);
    query.select(cb.construct(resClass, selects));
  
    if (sort != null) {
      List<Order> orders = springDataSort2HibernateOrders(sort, map);
      query.orderBy(orders);
    }
    
    TypedQuery<T> typedQuery = em
        .createQuery(query)
        .setFirstResult((pageNum - 1) * pageSize)
        .setMaxResults(pageSize);
    List<T> list = typedQuery.getResultList();
    
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    countQuery.where(predicate);
    countQuery.select(cb.count(countQuery.from(rootClass)));
    Long total = em.createQuery(countQuery).getSingleResult();
  
    PageRequest pr = PageRequest.of(pageNum, pageSize);
    return new PageImpl<>(list, pr, total);
  }
  
  private static Selection[] getSelects(Class<?> resClass, Map<String, From> conditionNameMap) {
    return PropUtils
        .getVoPropDefs(resClass)
        .stream()
        .map(p -> {
          String conditionName = p.getConditionName();
          From from = conditionNameMap.get(conditionName);
          return from.get(p.getSourcePropName()).alias(p.getAlias());
        })
        .toArray(Selection[]::new);
  }
  
  private static List<Order> springDataSort2HibernateOrders(Sort sort, Map<String, From> conditionNameMap) {
    return sort.get().map(o -> {
      String property = o.getProperty();
      String conditionName;
      int lastDotIndex = property.lastIndexOf('.');
      if (lastDotIndex == -1) {
        conditionName = "";
      } else {
        conditionName = property.substring(0, lastDotIndex);
      }
      String propName = property.substring(lastDotIndex + 1);
      From from = conditionNameMap.get(conditionName);
      return new OrderImpl(from.get(propName), o.isAscending());
    }).collect(Collectors.toList());
  }
  
}
