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

import java.util.*;
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
            response.setMessage("Employees fetched successfully");
            if(employees.isEmpty()){
                response.setMessage("No employees found");
            }
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
            ApiResponse<Employee> response = new ApiResponse<>(employee.get(),"Employee fetched successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            System.err.println("Error retrieving employee: " + ex.getMessage());
            ApiResponse<Employee> response = new ApiResponse<>("Failed to retrieve employee due to an internal error.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<List<Employee>>> createEmployees(List<CreateEmployeeRequestDTO> employees) {
        try {
            List<Employee> savedEmployees = new ArrayList<>();
            employees.forEach(employee -> savedEmployees.add(processEmployeeCreation(employee)));
            ApiResponse<List<Employee>> response = new ApiResponse<>(savedEmployees,"Successfully created employees");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception ex) {
            System.err.println("Error creating employees: " + ex.getMessage());
            ApiResponse<List<Employee>> response = new ApiResponse<>("Failed to create employees due to an internal error.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<Employee>> updateEmployeeById(long id, CreateEmployeeRequestDTO employee) {
        try{
            Optional<Employee> employeeCurrentData = employeeRepository.findById(id);
            if(employeeCurrentData.isEmpty()) {
                ApiResponse<Employee> response = new ApiResponse<>("There is no employee with the id " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Employee updatedEmployee = processEmployeeCreation(employee,id);
            return new ResponseEntity<>(new ApiResponse<>(updatedEmployee,"Updated employee"), HttpStatus.OK);
        }catch (Exception ex) {
            System.err.println("Error updating employee: " + ex.getMessage());
            ApiResponse<Employee> response = new ApiResponse<>("Failed to update employee due to an internal error.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteEmployeeById(long id) {
        try{
            Optional<Employee> employeeData = employeeRepository.findById(id);
            if(employeeData.isEmpty()) {
                return new ResponseEntity<>("There is no employee with the id " + id, HttpStatus.NOT_FOUND);
            }

            employeeRepository.deleteById(id);
            return new ResponseEntity<>("Employee deleted", HttpStatus.OK);
        }catch (Exception ex) {
            System.err.println("Error deleting employee: " + ex.getMessage());
            return new ResponseEntity<>("Failed to delete employee due to an internal error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Employee processEmployeeCreation(CreateEmployeeRequestDTO employee){
        return processEmployeeCreation(employee,null);
    }
    private Employee processEmployeeCreation(CreateEmployeeRequestDTO employee, Long employeeId) {
        List<Department> mandatoryDepartments = departmentRepository.findByMandatoryTrue();
        List<Department> employeeDepartmentsFromPayload = employee.getDepartmentIds().stream()
                .map(Long::parseLong)
                .map(departmentRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        Set<Department> employeeDepartmentList = new HashSet<>();
        HashMap<Long,Boolean> departmentIdIsPresentMap = new HashMap<>();

        mandatoryDepartments.forEach(department -> {
            departmentIdIsPresentMap.put(department.getId(), true);
            employeeDepartmentList.add(department);
        });

        employeeDepartmentsFromPayload.forEach(department -> {
            if(!departmentIdIsPresentMap.containsKey(department.getId())) {
                departmentIdIsPresentMap.put(department.getId(), true);
                employeeDepartmentList.add(department);
            }
        });

        Employee newEmployee = Employee.builder()
                .name(employee.getName())
                .departments(employeeDepartmentList)
                .build();

        if(employeeId != null) {
            newEmployee.setId(employeeId);
        }

        return employeeRepository.save(newEmployee);
    }

}
