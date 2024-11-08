package org.example.employeeallocation.service;

import org.example.employeeallocation.exception.BadDataException;
import org.example.employeeallocation.exception.ResourceNotFoundException;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.model.Employee;
import org.example.employeeallocation.repository.DepartmentRepository;
import org.example.employeeallocation.utility.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartment(long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if(department.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_DEPARTMENT_NOT_FOUND + id);
        }
        return department.get();
    }

    public Department addDepartment(Department department){
        validateAddOrUpdateDepartment(department);
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Department department) {
        if(department.getId()==0){
            throw new BadDataException(ErrorMessages.ERROR_INVALID_DEPARTMENT_ID);
        }
        validateAddOrUpdateDepartment(department);

        Department departmentCurrentData = getDepartment(department.getId());
        // if read only in current data is false we can update the department
        if(departmentCurrentData.getReadOnly() && department.getReadOnly()){
            throw new BadDataException(ErrorMessages.ERROR_DEPARTMENT_IS_READ_ONLY);
        }

        return departmentRepository.save(department);
    }

    public String deleteDepartment(long id) {
        Department departmentData = getDepartment(id);

        if(departmentData.getReadOnly()){
            throw new BadDataException(ErrorMessages.ERROR_DEPARTMENT_IS_READ_ONLY);
        }
        departmentRepository.deleteEmployeeDepartmentAssociations(id);
        departmentRepository.delete(departmentData);
        return "Department Deleted";
    }

    private void validateAddOrUpdateDepartment(Department department){
        Optional<Department> departmentOptional = departmentRepository.findByName(department.getName());
        if((department.getId() == 0 && departmentOptional.isPresent())
        || (departmentOptional.isPresent() && departmentOptional.get().getId() != department.getId())){
            throw new BadDataException(ErrorMessages.ERROR_DEPARTMENT_NAME_ALREADY_EXISTS);
        }
        if(department.getName().isBlank()){
            throw new BadDataException(ErrorMessages.ERROR_INVALID_DEPARTMENT_NAME);
        }
    }
}
