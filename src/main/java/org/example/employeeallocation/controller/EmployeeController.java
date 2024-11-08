package org.example.employeeallocation.controller;

import jakarta.validation.Valid;
import org.example.employeeallocation.model.Employee;
import org.example.employeeallocation.service.EmployeeServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {


    private final EmployeeServiceImpl employeeServiceImpl;

    public EmployeeController(EmployeeServiceImpl employeeServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployee() {
        return new ResponseEntity<>(employeeServiceImpl.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeServiceImpl.getEmployee(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return new ResponseEntity<>(employeeServiceImpl.addEmployee(employee),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee) {
        return new ResponseEntity<>(employeeServiceImpl.updateEmployee(employee),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeServiceImpl.deleteEmployee(id),HttpStatus.OK);
    }
}
