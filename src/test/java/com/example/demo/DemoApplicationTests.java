package com.example.demo;

import com.example.demo.dao.JjzzRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.dao.XzqhRepository;
import com.example.demo.entity.User;
import com.example.demo.queryzs.AbstractCondition;
import com.example.demo.queryzs.EmQuerys;
import com.example.demo.queryzs.SpecificationBuilder;
import com.example.demo.queryzs.cdtions.UserCondition;
import com.example.demo.queryzs.vo.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@SpringBootTest
class DemoApplicationTests {
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private JjzzRepository jjzzRepository;
  @Autowired
  private XzqhRepository xzqhRepository;
  
  @Autowired
  private EntityManager entityManager;
  
  
  
  @Test
//  @Transactional
  void contextLoads() {
  
//    Jjzz jjzz = new Jjzz();
//    jjzzRepository.save(jjzz.setName("经济1"));
//
//    userRepository.save(new User().setName("张三").setJjzz(jjzz));

//    System.out.println(userRepository.findAll());
//
//    System.out.println(entityManager);
//
//    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//    CriteriaQuery<User> query = builder.createQuery(User.class);
//    Root<User> root = query.from(User.class);
//
//    query.multiselect(
//        root.get("userId")
//        , root.get("name")
//        , root.join("jjzz", JoinType.LEFT).as(Jjzz.class)
////        ")
//    );
//    root.fetch("jjzz", JoinType.LEFT);
//    List<User> resultList = entityManager.createQuery(query).getResultList();
//    System.out.println(resultList.get(0).getJjzz());

//    System.out.println(xzqhRepository.findAll());
//    System.out.println(jjzzRepository.findAll());
  
    
    
    
    Specification<User> userSpecification = (root, query, cb) -> {
      Join<Object, Object> join = (Join<Object, Object>) root.fetch("jjzz", JoinType.LEFT);
      join.fetch("xzqh", JoinType.LEFT);
      Join<Object, Object> newXzqh = (Join<Object, Object>)join.fetch("newXzqh", JoinType.LEFT);
      cb.sum(newXzqh.get("xzqhId")).alias("xzqhName");
//      query.select(root.get("name"));
//      return cb.and(cb.equal(newXzqh.get("xzqhId"), 3));
      
      return query.getRestriction();
    };
    
    
    Specification<User> minSpecification = (root, query, cb) -> {
      Join<Object, Object> join = (Join<Object, Object>) root.fetch("jjzz", JoinType.LEFT);
      join.fetch("xzqh", JoinType.LEFT);
      Join<Object, Object> newXzqh = (Join<Object, Object>)join.fetch("newXzqh", JoinType.LEFT);
      cb.sum(newXzqh.get("xzqhId")).alias("xzqhName");
//      query.select(root.get("name"));
//      return cb.and(cb.equal(newXzqh.get("xzqhId"), 3));
//      query.select(cb.min(root.get("userId").as(Integer.class)));
//      query.select(root.get("name"));
      return cb.and(cb.equal(newXzqh.get("xzqhId"), 3));
    };
  
    
    
//    Specification<User> s1 = PropUtils.toSpecification(userCondition, User.class);
//    List<User> list1 = userRepository.findAll(s1);
//    System.out.println(list1);
//    Specification<User> s2 = PropUtils.toSpecification(new Condition().setNameLike("3"), User.class);
//    List<User> list2 = userRepository.findAll(s2);
//    System.out.println(list2);
  }
  
  
  @Test
  void testUserCondition() {
    UserCondition uc = new UserCondition();
    uc.setUserIdGte(1);
//    uc.setUserIdInList(Arrays.asList(1,2,3,4,5,6));
//    uc.nameStartWithAOrEndWithB("a", "z");
    Specification<User> specification = SpecificationBuilder.build(
        User.class,
        // todo 添加更多静态方法
        uc.root(false),
        AbstractCondition.justFetch("jjzz", JoinType.LEFT), // 不能父节点join再子节点fetch，会报错
        AbstractCondition.justFetch("jjzz.newXzqh", JoinType.LEFT),
        AbstractCondition.justFetch("czs", JoinType.LEFT),
        AbstractCondition.justFetch("jjzz.xzqh", JoinType.LEFT)
    );
    userRepository.findAll(specification, Sort.by("jjzz.xzqh.name").descending()).forEach(System.out::println);
  }
  
  
  @Test
  void testEmQuery() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
    Root<User> root = query.from(User.class);
    
    UserCondition uc = new UserCondition();
    uc.setUserIdGte(1);
    Specification<User> specification = SpecificationBuilder.build(
        User.class,
        uc.root(),
        AbstractCondition.justJoin("jjzz", JoinType.LEFT) // 不能父节点join再子节点fetch，会报错
//        AbstractCondition.justFetch("jjzz.newXzqh", JoinType.LEFT),
//        AbstractCondition.justFetch("czs", JoinType.LEFT),
//        AbstractCondition.justFetch("jjzz.xzqh", JoinType.LEFT)
    );
    specification.toPredicate(root, query, cb);
    query.multiselect(root.get("userId"));
    entityManager.createQuery(query).getResultList().forEach(System.out::println);
  }
  @Test
  void testEmQuery1() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserDto> query = cb.createQuery(UserDto.class);
    Root<User> root = query.from(User.class);
    UserCondition uc = new UserCondition();
    uc.setUserIdGte(3);
    Specification<User> specification = SpecificationBuilder.build(
        User.class,
        uc.root(),
        AbstractCondition.justJoin("jjzz", JoinType.LEFT) // 不能父节点join再子节点fetch，会报错
//        AbstractCondition.justFetch("jjzz.newXzqh", JoinType.LEFT),
//        AbstractCondition.justFetch("czs", JoinType.LEFT),
//        AbstractCondition.justFetch("jjzz.xzqh", JoinType.LEFT)
    );
    Predicate predicate = specification.toPredicate(root, query, cb);
    CompoundSelection<UserDto> selection = cb.construct(UserDto.class, root.get("userId"), root.get("name"));
    query.select(selection).distinct(true);
    query.where(predicate);
    TypedQuery<UserDto> typedQuery1 = entityManager.createQuery(query);
    typedQuery1.setFirstResult(1).setMaxResults(3);
    typedQuery1.getResultList().forEach(System.out::println);
  
   
    
    cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    countQuery.select(cb.count(countQuery.from(User.class)));
    Long count = entityManager.createQuery(countQuery).getSingleResult();
    System.out.println(count);
  }
  
  @Test
  void testEmSelect() {
    UserCondition uc = new UserCondition();
    uc.setUserIdGte(1);
//
//    EmQuery
//        .emSelectList(UserDto.class, User.class,
//            entityManager,
//            uc.root(),
//            AbstractCondition.justLeftJoin("jjzz"),
//            AbstractCondition.justLeftJoin("jjzz-xzqh")
//        )
//        .forEach(System.out::println);
//
    EmQuerys
        .emSelectPage(UserDto.class, User.class,
            entityManager,
            1, 6, Sort.by("name").descending().and(Sort.by("jjzz.name")),
            uc.root(),
            AbstractCondition.justLeftJoin("jjzz"),
            AbstractCondition.justLeftJoin("jjzz.xzqh")
        )
        .forEach(System.out::println);
  }
}

