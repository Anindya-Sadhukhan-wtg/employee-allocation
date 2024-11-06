package org.example.employeeallocation.service;

import org.example.employeeallocation.common.ApiResponse;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public ResponseEntity<ApiResponse<List<Department>>> createDepartments(List<Department> departments){
        List<Department> newDepartments = departmentRepository.saveAll(departments);
        ApiResponse<List<Department>> response = new ApiResponse<>(newDepartments);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
