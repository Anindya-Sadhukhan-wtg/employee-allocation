package org.example.employeeallocation.repository;

import jakarta.transaction.Transactional;
import org.example.employeeallocation.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByMandatoryTrue();

    Optional<Department> findByName(String name);

    @Modifying
    @Query(value = "DELETE FROM MAP_EMPLOYEE_DEPARTMENT WHERE id_department = :departmentId", nativeQuery = true)
    void deleteEmployeeDepartmentAssociations(@Param("departmentId") Long departmentId);
}
