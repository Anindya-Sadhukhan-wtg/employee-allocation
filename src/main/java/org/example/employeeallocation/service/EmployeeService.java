package org.example.employeeallocation.service;

import jakarta.transaction.Transactional;
import org.example.employeeallocation.common.ApiResponse;
import org.example.employeeallocation.dto.CreateEmployeeRequestDTO;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.model.Employee;
import org.example.employeeallocation.repository.DepartmentRepository;
import org.example.employeeallocation.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public ResponseEntity<ApiResponse<List<Employee>>> getAllEmployees() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            ApiResponse<List<Employee>> response = new ApiResponse<>(employees);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            System.err.println("Error retrieving employees: " + ex.getMessage());
            ApiResponse<List<Employee>> response = new ApiResponse<>("Failed to retrieve employees due to an internal error.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<Employee>> getEmployeeById(long id) {
        try {
            Optional<Employee> employee = employeeRepository.findById(id);
            if(employee.isEmpty()) {
                ApiResponse<Employee> response = new ApiResponse<>("Could not find employee with id " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ApiResponse<Employee> response = new ApiResponse<>(employee.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            System.err.println("Error retrieving employees: " + ex.getMessage());
            ApiResponse<Employee> response = new ApiResponse<>("Failed to retrieve employee due to an internal error.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<List<Employee>>> createEmployees(List<CreateEmployeeRequestDTO> employees) {
        List<Employee> savedEmployees = new ArrayList<>();
        employees.forEach(employee -> savedEmployees.add(processEmployeeCreation(employee)));
        ApiResponse<List<Employee>> response = new ApiResponse<>(savedEmployees);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    private Employee processEmployeeCreation(CreateEmployeeRequestDTO employee) {
        List<Department> mandatoryDepartments = departmentRepository.findByMandatoryTrue();
        Set<Department> departments = employee.getDepartmentIds().stream()
                .map(Long::parseLong)
                .map(departmentRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        if(mandatoryDepartments != null) {

        }
        // Create a new employee
        Employee newEmployee = Employee.builder()
                .name(employee.getName())
                .departments(departments)
                .build();

        return employeeRepository.save(newEmployee);
    }
}
