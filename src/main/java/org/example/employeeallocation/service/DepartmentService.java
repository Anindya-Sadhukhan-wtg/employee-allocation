package org.example.employeeallocation.service;

import org.example.employeeallocation.model.Department;

import java.util.List;

public interface DepartmentService {
     Department getDepartment(Long id);
     List<Department> getAllDepartments();
     Department addDepartment(Department department);
     Department updateDepartment(Department department);
     String deleteDepartment(Long id);
}
