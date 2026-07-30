package com.Bank.Banking.Repository;

import com.Bank.Banking.Entity.Employee;
import com.Bank.Banking.Enum.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    // Add custom query methods here if needed
    Optional<Employee> findByEmployeeId(Long employeeId);
    Optional<Employee> findByUserEmail(String email);
    // Department
    List<Employee> findByDepartmentIgnoreCase(String department);

    // Designation
    List<Employee> findByDesignationIgnoreCase(String designation);

    // Branch
    List<Employee> findByBranch(Branch branch);

    // Active Employees
    List<Employee> findByIsActive(boolean isActive);

    // Search Employees
    @Query("""
            SELECT e FROM Employee e
            WHERE
            LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(e.department) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(e.designation) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Employee> searchEmployees(@Param("keyword") String keyword);
}
