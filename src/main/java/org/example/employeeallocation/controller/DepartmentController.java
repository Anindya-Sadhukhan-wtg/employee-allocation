package org.example.employeeallocation.controller;

import jakarta.validation.Valid;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.service.DepartmentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {


    private final DepartmentServiceImpl departmentServiceImpl;

    public DepartmentController(DepartmentServiceImpl departmentServiceImpl) {
        this.departmentServiceImpl = departmentServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return new ResponseEntity<>(departmentServiceImpl.getAllDepartments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable long id) {
        return new ResponseEntity<>(departmentServiceImpl.getDepartment(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Department> addDepartment(@Valid @RequestBody Department department) {
        return new ResponseEntity<>(departmentServiceImpl.addDepartment(department), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Department> updateDepartment(@Valid @RequestBody Department department) {
        return new ResponseEntity<>(departmentServiceImpl.updateDepartment(department), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable long id) {
        return new ResponseEntity<>(departmentServiceImpl.deleteDepartment(id), HttpStatus.OK);
    }

}
