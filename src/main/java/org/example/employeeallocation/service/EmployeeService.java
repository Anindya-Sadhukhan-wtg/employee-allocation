package org.example.employeeallocation.service;

import org.example.employeeallocation.model.Employee;

import java.util.List;

public interface EmployeeService {
     List<Employee> getAllEmployees();
     Employee getEmployeeById(Long id);
     Employee addEmployee(Employee employee);
     Employee updateEmployee(Employee employee);
     String deleteEmployee(Long id);
}
