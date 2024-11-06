package org.example.employeeallocation.controller;

import org.example.employeeallocation.common.ApiResponse;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<List<Department>>> createDepartments(@RequestBody List<Department> departments) {
        return departmentService.createDepartments(departments);
    }
}
