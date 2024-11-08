package org.example.employeeallocation.service;

import jakarta.transaction.Transactional;
import org.example.employeeallocation.exception.BadDataException;
import org.example.employeeallocation.exception.ResourceNotFoundException;
import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.model.Employee;
import org.example.employeeallocation.repository.DepartmentRepository;
import org.example.employeeallocation.repository.EmployeeRepository;
import org.example.employeeallocation.utility.ErrorMessages;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployee(long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_EMPLOYEE_NOT_FOUND + id);
        }
        return employee.get();
    }

    public Employee addEmployee(Employee employee) {
        validateAddOrUpdateEmployee(employee);
        return processEmployeeCreation(employee);
    }

    public Employee updateEmployee(Employee employee) {
        if(employee.getId()==0){
            throw new BadDataException(ErrorMessages.ERROR_INVALID_EMPLOYEE_ID);
        }
        getEmployee(employee.getId());
        validateAddOrUpdateEmployee(employee);

        return processEmployeeCreation(employee);
    }

    public String deleteEmployee(long id) {
        getEmployee(id);
        employeeRepository.deleteById(id);
        return "Employee deleted";
    }

    private void validateAddOrUpdateEmployee(Employee employee){
        if(employee.getName().isBlank()){
            throw new BadDataException(ErrorMessages.ERROR_INVALID_EMPLOYEE_NAME);
        }
    }
    private Employee processEmployeeCreation(Employee employee) {
        List<Department> mandatoryDepartments = departmentRepository.findByMandatoryTrue();
        List<Department> employeeDepartmentsFromPayload = employee.getDepartments().stream()
                .map(Department::getId)
                .toList()
                .stream()
                .map(departmentRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        Set<Department> finalEmployeeDepartmentList = new HashSet<>();
        HashMap<Long,Boolean> departmentIdIsPresentMap = new HashMap<>();

        mandatoryDepartments.forEach(department -> {
            departmentIdIsPresentMap.put(department.getId(), true);
            finalEmployeeDepartmentList.add(department);
        });

        employeeDepartmentsFromPayload.forEach(department -> {
            if(!departmentIdIsPresentMap.containsKey(department.getId())) {
                departmentIdIsPresentMap.put(department.getId(), true);
                finalEmployeeDepartmentList.add(department);
            }
        });

        employee.setDepartments(finalEmployeeDepartmentList);

        return employeeRepository.save(employee);
    }

}
