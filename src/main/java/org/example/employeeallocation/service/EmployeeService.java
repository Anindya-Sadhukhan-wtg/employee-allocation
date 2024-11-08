package org.example.employeeallocation.service;

import org.example.employeeallocation.model.Employee;

import java.util.List;

public interface EmployeeService {
    public List<Employee> getAllEmployees();
    public Employee getEmployee(long id);
    public Employee addEmployee(Employee employee);
    public Employee updateEmployee(Employee employee);
    public String deleteEmployee(long id);
}
