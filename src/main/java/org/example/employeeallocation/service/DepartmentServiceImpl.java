package org.example.employeeallocation.service;

import jakarta.transaction.Transactional;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.repository.DepartmentRepository;
import org.example.employeeallocation.utility.ErrorMessages;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartment(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if(department.isEmpty()) {
            throw new NoSuchElementException(ErrorMessages.ERROR_DEPARTMENT_NOT_FOUND + id);
        }
        return department.get();
    }

    @Override
    public Department addDepartment(Department department){
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Department department) {
        if(department.getId()==null){
            throw new IllegalArgumentException(ErrorMessages.ERROR_INVALID_DEPARTMENT_ID);
        }

        Department departmentCurrentData = getDepartment(department.getId());
        // if read only in current data is false we can update the department
        if(departmentCurrentData.getReadOnly() && department.getReadOnly()){
            throw new IllegalArgumentException(ErrorMessages.ERROR_DEPARTMENT_IS_READ_ONLY);
        }

        return departmentRepository.save(department);
    }

    @Override
    public String deleteDepartment(Long id) {
        Department departmentData = getDepartment(id);

        if(departmentData.getReadOnly()){
            throw new IllegalArgumentException(ErrorMessages.ERROR_DEPARTMENT_IS_READ_ONLY);
        }
        // Referential integrity constraint violation
        // departmentRepository.deleteById(id);

        departmentRepository.deleteEmployeeDepartmentAssociations(id);
        departmentRepository.delete(departmentData);
        return "Department Deleted";
    }

}
