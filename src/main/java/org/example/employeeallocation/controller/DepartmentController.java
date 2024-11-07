package org.example.employeeallocation.controller;

import org.example.employeeallocation.common.ApiResponse;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> getDepartmentById(@PathVariable long id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<Department>>> createDepartments(@RequestBody List<Department> departments) {
        return departmentService.createDepartments(departments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> updateDepartment(@PathVariable long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable long id) {
        return departmentService.deleteDepartment(id);
    }

}
