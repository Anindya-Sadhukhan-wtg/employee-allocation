package org.example.employeeallocation.service;

import org.example.employeeallocation.model.Department;

import java.util.List;

public interface DepartmentService {
    public Department getDepartment(long id);
    public List<Department> getAllDepartments();
    public Department addDepartment(Department department);
    public Department updateDepartment(Department department);
    public String deleteDepartment(long id);
}
