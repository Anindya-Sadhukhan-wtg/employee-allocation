package org.example.employeeallocation.repository;

import org.example.employeeallocation.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByMandatoryTrue();
}
