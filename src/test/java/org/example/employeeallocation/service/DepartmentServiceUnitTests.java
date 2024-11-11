package org.example.employeeallocation.service;

import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.model.Employee;
import org.example.employeeallocation.repository.DepartmentRepository;
import org.example.employeeallocation.repository.EmployeeRepository;
import org.example.employeeallocation.utility.ErrorMessages;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentServiceUnitTests {

    @Mock
    private DepartmentRepository departmentRepository;

    @Spy
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department readOnlyAndMandatoryDepartment;
    private Department mandatoryDepartment;
    private Department departmentBolero;
    private Department departmentWinzor;

    @BeforeEach
    public void setUp() {
        readOnlyAndMandatoryDepartment = Department.builder()
                .id(1L)
                .name("Organisation")
                .mandatory(true)
                .readOnly(true)
                .build();

        mandatoryDepartment = Department.builder()
                .id(2L)
                .name("R&D")
                .mandatory(true)
                .readOnly(false)
                .build();

        departmentBolero = Department.builder()
                .id(3L)
                .name("Bolero")
                .mandatory(false)
                .readOnly(false)
                .build();

        departmentWinzor = Department.builder()
                .id(4L)
                .name("Winzor")
                .mandatory(false)
                .readOnly(false)
                .build();
    }

    @Test
    @DisplayName("Create department")
    @Order(1)
    public void addDepartmentTest() {
        given(departmentRepository.save(readOnlyAndMandatoryDepartment)).willReturn(readOnlyAndMandatoryDepartment);

        Department savedDepartment = departmentService.addDepartment(readOnlyAndMandatoryDepartment);

        assertThat(savedDepartment).isNotNull();
    }

    @Test
    @DisplayName("Get all departments")
    @Order(2)
    public void getAllDepartmentsTest() {
        given(departmentRepository.findAll()).willReturn(Arrays.asList(readOnlyAndMandatoryDepartment, mandatoryDepartment, departmentBolero, departmentWinzor));

        List<Department> departmentList= departmentService.getAllDepartments();

        assertThat(departmentList).isNotNull();
        assertThat(departmentList.size()).isGreaterThan(1);
        assertThat(departmentList).contains(readOnlyAndMandatoryDepartment, mandatoryDepartment, departmentBolero, departmentWinzor);
    }

    @Test
    @DisplayName("Get department: successfully fetch by id")
    @Order(3)
    public void getDepartmentByIdTest() {
        given(departmentRepository.findById(1L)).willReturn(Optional.of(readOnlyAndMandatoryDepartment));

        Department department= departmentService.getDepartmentById(1L);

        assertThat(department).isNotNull();
        assertThat(department.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Get department: error as no such department exist with given id")
    @Order(4)
    public void getDepartmentByNonExistentId_ShouldThrowNoSuchElementExceptionTest() {
        Long nonExistentId = 99L;
        given(departmentRepository.findById(nonExistentId)).willReturn(Optional.empty());

        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            departmentService.getDepartmentById(nonExistentId);
        });

        assertEquals(ErrorMessages.ERROR_DEPARTMENT_NOT_FOUND + nonExistentId, noSuchElementException.getMessage());
    }

    @Test
    @DisplayName("Update department: invalid id error")
    @Order(5)
    public void updateDepartmentByInvalidId_ShouldThrowIllegalArgumentExceptionTest() {
        Department department = Department.builder()
                .name("HR")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            departmentService.updateDepartment(department);
        });

        assertEquals(ErrorMessages.ERROR_INVALID_DEPARTMENT_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Update department: non existent id error")
    @Order(6)
    public void updateDepartmentByNonExistentId_ShouldThrowNoSuchElementExceptionTest() {
        Department department = Department.builder()
                .id(99L)
                .name("HR")
                .mandatory(false)
                .readOnly(false)
                .build();

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            departmentService.updateDepartment(department);
        });

        assertEquals(ErrorMessages.ERROR_DEPARTMENT_NOT_FOUND + department.getId(), exception.getMessage());
    }

    @Test
    @DisplayName("Update department: Department is read only")
    @Order(7)
    public void updateReadOnlyDepartment_ShouldThrowIllegalArgumentExceptionTest() {
        Department updatedReadOnlyAndMandatoryDepartmentData = Department.builder()
                .id(1L)
                .name("Updated Organisation")
                .readOnly(true)
                .mandatory(false)
                .build();

        doReturn(readOnlyAndMandatoryDepartment).when(departmentService).getDepartmentById(updatedReadOnlyAndMandatoryDepartmentData.getId());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            departmentService.updateDepartment(updatedReadOnlyAndMandatoryDepartmentData);
        });

        assertEquals(ErrorMessages.ERROR_DEPARTMENT_IS_READ_ONLY, exception.getMessage());

    }

    @Test
    @DisplayName("Update department: Department updated successfully")
    @Order(7)
    public void updateDepartmentSuccessful() {
        Department updatedReadOnlyAndMandatoryDepartmentData = Department.builder()
                .id(1L)
                .name("Updated Organisation")
                .readOnly(false)
                .mandatory(false)
                .build();

        doReturn(readOnlyAndMandatoryDepartment).when(departmentService).getDepartmentById(updatedReadOnlyAndMandatoryDepartmentData.getId());
        given(departmentRepository.save(updatedReadOnlyAndMandatoryDepartmentData)).willReturn(updatedReadOnlyAndMandatoryDepartmentData);

        Department department = departmentService.updateDepartment(updatedReadOnlyAndMandatoryDepartmentData);

        assertThat(department).isNotNull();
        assertEquals(department.getId(), updatedReadOnlyAndMandatoryDepartmentData.getId());
        assertEquals(department.getName(), updatedReadOnlyAndMandatoryDepartmentData.getName());
        assertEquals(department.getMandatory(), updatedReadOnlyAndMandatoryDepartmentData.getMandatory());
        assertEquals(department.getReadOnly(), updatedReadOnlyAndMandatoryDepartmentData.getReadOnly());
    }
}
