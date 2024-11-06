package org.example.employeeallocation.repository;

import org.example.employeeallocation.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
