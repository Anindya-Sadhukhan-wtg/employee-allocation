package org.example.employeeallocation.repository;

import jakarta.transaction.Transactional;
import org.example.employeeallocation.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByMandatoryTrue();
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Employee_Department WHERE department_id = :departmentId", nativeQuery = true)
    void deleteEmployeeDepartmentAssociations(@Param("departmentId") Long departmentId);
}
