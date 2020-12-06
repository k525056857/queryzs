package com.example.demo.queryzs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

@FunctionalInterface
public interface CustomPredicate {
  Predicate to(Path root, CriteriaBuilder cb);
}
