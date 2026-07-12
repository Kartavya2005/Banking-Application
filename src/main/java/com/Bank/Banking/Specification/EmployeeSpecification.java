package com.Bank.Banking.Specification;

import com.Bank.Banking.Entity.Employee;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> filterEmployees(
            String firstName,
            String email,
            String phoneNumber,
            String department,
            String address,
            Boolean isActive) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null && !firstName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
            }
            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), "%" + phoneNumber.toLowerCase() + "%"));
            }
            if (department != null && !department.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("department")), "%" + department.toLowerCase() + "%"));
            }
            if (address != null && !address.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + address.toLowerCase() + "%"));
            }
            if (isActive != null) {
                predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
