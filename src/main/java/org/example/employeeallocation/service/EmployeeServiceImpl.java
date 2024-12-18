package org.example.employeeallocation.service;

import jakarta.transaction.Transactional;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.model.Employee;
import org.example.employeeallocation.repository.DepartmentRepository;
import org.example.employeeallocation.repository.EmployeeRepository;
import org.example.employeeallocation.utility.ErrorMessages;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()) {
            throw new NoSuchElementException(ErrorMessages.ERROR_EMPLOYEE_NOT_FOUND + id);
        }
        return employee.get();
    }

    @Override
    public Employee addEmployee(Employee employee) {
        return processEmployeeCreation(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        if(employee.getId() == null){
            throw new IllegalArgumentException(ErrorMessages.ERROR_INVALID_EMPLOYEE_ID);
        }
        getEmployeeById(employee.getId());

        return processEmployeeCreation(employee);
    }

    @Override
    public String deleteEmployee(Long id) {
        getEmployeeById(id);
        employeeRepository.deleteById(id);
        return "Employee deleted";
    }

    private Employee processEmployeeCreation(Employee employee) {
        List<Department> mandatoryDepartments = departmentRepository.findByMandatoryTrue();
        List<Department> employeeDepartmentsFromPayload = employee.getDepartments().stream()
                .map(Department::getId)
                .toList()
                .stream()
                .map(departmentRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        employee.setDepartments(new HashSet<>(mandatoryDepartments));
        employee.getDepartments().addAll(employeeDepartmentsFromPayload);

        return employeeRepository.save(employee);
    }

}
