package org.example.employeeallocation.controller;

import org.example.employeeallocation.common.ApiResponse;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.service.DepartmentServiceImpl;
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
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
        return departmentServiceImpl.getAllDepartments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> getDepartmentById(@PathVariable long id) {
        return departmentServiceImpl.getDepartmentById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<Department>>> createDepartments(@RequestBody List<Department> departments) {
        return departmentServiceImpl.createDepartments(departments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> updateDepartment(@PathVariable long id, @RequestBody Department department) {
        return departmentServiceImpl.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable long id) {
        return departmentServiceImpl.deleteDepartment(id);
    }

}
