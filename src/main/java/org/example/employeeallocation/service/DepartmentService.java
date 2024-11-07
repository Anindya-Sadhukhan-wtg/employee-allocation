package org.example.employeeallocation.service;

import org.example.employeeallocation.common.ApiResponse;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
        try {
            List<Department> departments = departmentRepository.findAll();
            ApiResponse<List<Department>> response = new ApiResponse<>(departments);
            response.setMessage("Departments fetched successfully");
            if(departments.isEmpty()){
                response.setMessage("No departments found");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            System.err.println("Error retrieving departments: " + ex.getMessage());
            ApiResponse<List<Department>> response = new ApiResponse<>("Failed to retrieve departments due to an internal error.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<Department>> getDepartmentById(long id) {
        try {
            Optional<Department> department = departmentRepository.findById(id);
            if(department.isEmpty()) {
                ApiResponse<Department> response = new ApiResponse<>("Could not find department with id " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ApiResponse<Department> response = new ApiResponse<>(department.get(),"Department fetched successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            System.err.println("Error retrieving department: " + ex.getMessage());
            ApiResponse<Department> response = new ApiResponse<>("Failed to retrieve department due to an internal error.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<List<Department>>> createDepartments(List<Department> departments){
        try {
            List<Department> newDepartments = departmentRepository.saveAll(departments);
            ApiResponse<List<Department>> response = new ApiResponse<>(newDepartments, "Successfully created departments");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            System.err.println("Error creating departments: " + ex.getMessage());
            ApiResponse<List<Department>> response = new ApiResponse<>("Failed to create departments due to an internal error.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<Department>> updateDepartment(long id, Department department) {

        try{
            Optional<Department> departmentCurrentData = departmentRepository.findById(id);
            if(departmentCurrentData.isEmpty()){
                ApiResponse<Department> response = new ApiResponse<>("There is no department with the id " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // If both current data and incoming data has read only attribute set to true then data is not updated
            if(departmentCurrentData.get().getReadOnly() && department.getReadOnly()){
                ApiResponse<Department> response = new ApiResponse<>("Department is read-only");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Department newDepartmentData = departmentRepository.save(
                    Department.builder()
                            .id(id)
                            .name(department.getName())
                            .readOnly(department.getReadOnly())
                            .mandatory(department.getMandatory())
                            .build()
            );
            return new ResponseEntity<>(new ApiResponse<>(newDepartmentData,"Update department"), HttpStatus.OK);
        } catch (Exception ex) {
            System.err.println("Error updating department: " + ex.getMessage());
            ApiResponse<Department> response = new ApiResponse<>("Failed to update department due to an internal error.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteDepartment(long id) {
        try{
            Optional<Department> departmentCurrentData = departmentRepository.findById(id);
            if(departmentCurrentData.isEmpty()){
                return new ResponseEntity<>("There is no department with the id " + id, HttpStatus.NOT_FOUND);
            }

            // If both current data and incoming data has read only attribute set to true then data is not updated
            if(departmentCurrentData.get().getReadOnly()){
                return new ResponseEntity<>("Department is read-only", HttpStatus.BAD_REQUEST);
            }
            departmentRepository.deleteEmployeeDepartmentAssociations(id);
            departmentRepository.deleteById(id);
            return new ResponseEntity<>("Department Deleted", HttpStatus.OK);
        } catch (Exception ex) {
            System.err.println("Error updating department: " + ex.getMessage());
            return new ResponseEntity<>("Failed to delete department due to an internal error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
