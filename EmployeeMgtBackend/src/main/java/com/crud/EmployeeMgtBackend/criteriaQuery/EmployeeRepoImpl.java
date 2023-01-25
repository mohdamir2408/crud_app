package com.crud.EmployeeMgtBackend.criteriaQuery;

import com.crud.EmployeeMgtBackend.entities.Employee;
import com.crud.EmployeeMgtBackend.entities.Pagination;
import com.crud.EmployeeMgtBackend.entities.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.*;
import javax.transaction.*;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class  EmployeeRepoImpl implements EmployeeRepo {

    @PersistenceContext
    private EntityManager em;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public List<Employee> findByFirstName(String firstName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);

        Root<Employee> employee = cq.from(Employee.class);
        Predicate firstNamePredicate = cb.equal(employee.get("firstName"), firstName);

        cq.where(firstNamePredicate);

        TypedQuery query = em.createQuery(cq);

        return query.getResultList();
    }

//    @Override
    public List<Employee> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> employee = cq.from(Employee.class);
        cq.select(employee);

        TypedQuery query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public Employee findById(int id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);

        Root<Employee> employee = cq.from(Employee.class);
        Predicate idPredicate = cb.equal(employee.get("id"), id);

        cq.where(idPredicate);

        TypedQuery query = em.createQuery(cq);

        return (Employee) query.getSingleResult();
    }

    @Override
    public Employee save(Employee employee) {
//        employee.setPassword(getEncodedPassword(employee.getPassword()));
        em.persist(employee);
        return employee;
    }

//    private String getEncodedPassword(String password) {
//        return passwordEncoder.encode(password);
//    }


    @Override
    public void delete(Employee employee) {
        em.remove(employee);
    }

    @Override
    public Employee update(Employee employeeDetails) {
        return em.merge(employeeDetails);

    }

//    @Override
//    public List<Employee> findAll(int offset, int limit) {
//        return null;
//    }

    // Pagination Method

    //    @Override
//    public List<Employee> findAllByPage(Pagination pagination) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
//        Root<Employee> root = cq.from(Employee.class);
    @Override
    public ResponsePage paging(Pagination pagination) {

        Long count = null;
        int pageNumber = 0;
        boolean check = false;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        CriteriaQuery<Employee> select = cq.select(root);

        //Condition For Searching

        if (pagination.getSearchItem() != null && !pagination.getSearchItem().isEmpty()) {
            Predicate predicate1 = cb.like(root.get("firstName"), "%" + pagination.getSearchItem() + "%");
            Predicate predicate2 = cb.like(root.get("lastName"), "%" + pagination.getSearchItem() + "%");

            Predicate res = cb.or(predicate1, predicate2);
            cq.where(res);

//            CriteriaBuilder cb1 = em.getCriteriaBuilder();
//            CriteriaQuery<Long> cq1 = cb1.createQuery(Long.class);
//            cq1.select(cb1.count(cq1.from(Employee.class)));
//
//            em.createQuery(cq1);
//            cq1.where(predicate1);
//            count = em.createQuery(cq1).getSingleResult();
//            int pageNo = pagination.getLimit();
//
//            if(count!=pageNo)
//            {
//                pageNumber = (int) ((count / pageNo) + 1);
//                System.out.println(count);
//                System.out.println(pageNumber);
//            }
//            else
//            {
//                pageNumber = (int) ((count / pageNo));
//                System.out.println(count);
//                System.out.println(pageNumber);
//            }
//            check=true;
        }

        // Condition for sorting.
        Order order;
        if (pagination.getSortField() != null && !pagination.getSortField().isEmpty()) {
            if (pagination.getSortType().equals("desc")) {
                order = cb.desc(root.get(pagination.getSortField()));
            } else {
                order = cb.asc(root.get(pagination.getSortField()));
            }
        } else {
            order = cb.asc(root.get("id"));
        }
        cq.orderBy(order);

        TypedQuery<Employee> typedQuery = em.createQuery(select);
        typedQuery.setFirstResult((pagination.getPageNo() - 1) * pagination.getLimit());
        typedQuery.setMaxResults(pagination.getLimit());
        List<?> res = typedQuery.getResultList();

        if (check == false) {
            Query queryTotal = em.createQuery("Select count(id) from Employee");
            count = (long) queryTotal.getSingleResult();
            int pageNo = pagination.getLimit();
            if (count == pageNo) {
                pageNumber = (int) ((count / pageNo));
            } else {
                pageNumber = (int) ((count / pageNo) + 1);
                System.out.println(pageNumber);
            }
        }
//        List<Employee> result = em
//                .createQuery(cq)
//                .setFirstResult((pagination.getPageNo()-1)* pagination.getLimit())
//                .setMaxResults(pagination.getLimit())
//                .getResultList();
//
//        return result;
        return new ResponsePage(res, count.intValue(),
                pageNumber, pagination.getPageNo(),
                pagination.getLimit());
    }

    @Override
    public Employee findByEmail(String emailId) {
        CriteriaBuilder criteriaBuilder=em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery= criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root=criteriaQuery.from(Employee.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("emailId"),emailId));
        TypedQuery<Employee> query=em.createQuery(criteriaQuery);
        List<Employee> list = query.setMaxResults(1).getResultList();
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

}