package org.example.employeeallocation.controller;

import org.example.employeeallocation.common.ApiResponse;
import org.example.employeeallocation.dto.CreateEmployeeRequestDTO;
import org.example.employeeallocation.model.Employee;
import org.example.employeeallocation.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Employee>>> getAllEmployee() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ApiResponse<Employee>> getEmployeeById(@PathVariable long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<Employee>>> createEmployees(@RequestBody List<CreateEmployeeRequestDTO> employees) {
        return employeeService.createEmployees(employees);
    }
}
