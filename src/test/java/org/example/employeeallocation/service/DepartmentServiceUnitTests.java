package org.example.employeeallocation.service;

import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.model.Employee;
import org.example.employeeallocation.repository.DepartmentRepository;
import org.example.employeeallocation.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentServiceUnitTests {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;

    @BeforeEach
    public void setUp() {
        department = Department.builder()
                .id(1L)
                .name("Organisation")
                .mandatory(true)
                .readOnly(true)
                .build();

    }

    @Test
    @DisplayName("Create department")
    @Order(1)
    public void addDepartmentTest() {
        //precondition
        given(departmentRepository.save(department)).willReturn(department);

        //action
        Department savedDepartment = departmentService.addDepartment(department);

        assertThat(savedDepartment).isNotNull();
    }
}
