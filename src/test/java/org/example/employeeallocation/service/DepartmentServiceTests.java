package org.example.employeeallocation.service;

import org.example.employeeallocation.model.Department;
import org.example.employeeallocation.repository.DepartmentRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentServiceTests {

    public static final long READ_ONLY_AND_MANDATORY_DEPARTMENT_ID = 1L;
    public static final String READ_ONLY_ONLY_AND_MANDATORY_DEPARTMENT_NAME = "Organisation";
    public static final long MANDATORY_DEPARTMENT_ID = 2L;
    public static final String MANDATORY_DEPARTMENT_NAME = "R&D";

    @Mock
    private DepartmentRepository departmentRepository;

    @Spy
    @InjectMocks
    private DepartmentServiceImpl departmentServiceImpl;
    
    private DepartmentService ref;

    private Department readOnlyAndMandatoryDepartment;
    private Department mandatoryDepartment;

    @BeforeEach
    public void setUp() {
        readOnlyAndMandatoryDepartment = Department.builder()
                .id(READ_ONLY_AND_MANDATORY_DEPARTMENT_ID)
                .name(READ_ONLY_ONLY_AND_MANDATORY_DEPARTMENT_NAME)
                .mandatory(true)
                .readOnly(true)
                .build();

        mandatoryDepartment = Department.builder()
                .id(MANDATORY_DEPARTMENT_ID)
                .name(MANDATORY_DEPARTMENT_NAME)
                .mandatory(true)
                .readOnly(false)
                .build();

        ref= new DepartmentServiceImpl(departmentRepository);
        lenient().when(departmentRepository.save(readOnlyAndMandatoryDepartment)).thenReturn(readOnlyAndMandatoryDepartment);
        lenient().doReturn(readOnlyAndMandatoryDepartment).when(departmentServiceImpl).getDepartmentById(READ_ONLY_AND_MANDATORY_DEPARTMENT_ID);
    }

    @Test
    @DisplayName("Create department")
    public void testCreateDepartment_Success() {
        Department savedDepartment = ref.addDepartment(readOnlyAndMandatoryDepartment);

        assertThat(savedDepartment).isNotNull();
    }

    @Test
    @DisplayName("Get all departments")
    public void testGetAllDepartments_Success() {
        given(departmentRepository.findAll()).willReturn(Arrays.asList(readOnlyAndMandatoryDepartment, mandatoryDepartment));

        List<Department> departmentList= ref.getAllDepartments();

        assertThat(departmentList).isNotNull();
        assertThat(departmentList.size()).isGreaterThan(1);
        assertThat(departmentList).contains(readOnlyAndMandatoryDepartment, mandatoryDepartment);
    }

    @Test
    @DisplayName("Get department: successfully fetch by id")
    public void testGetDepartmentById_Success() {
        given(departmentRepository.findById(READ_ONLY_AND_MANDATORY_DEPARTMENT_ID)).willReturn(Optional.of(readOnlyAndMandatoryDepartment));

        Department department= ref.getDepartmentById(READ_ONLY_AND_MANDATORY_DEPARTMENT_ID);

        assertThat(department).isNotNull();
        assertThat(department.getId()).isEqualTo(READ_ONLY_AND_MANDATORY_DEPARTMENT_ID);
    }

    @Test
    @DisplayName("Get department: error as no such department exist with given id")
    public void testGetDepartmentByNonExistentId_ShouldThrowNoSuchElementException() {
        Long nonExistentId = 99L;
        given(departmentRepository.findById(nonExistentId)).willReturn(Optional.empty());

        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> {
            ref.getDepartmentById(nonExistentId);
        });

        assertEquals(ErrorMessages.ERROR_DEPARTMENT_NOT_FOUND + nonExistentId, noSuchElementException.getMessage());
    }

    @Test
    @DisplayName("Update department: invalid id error")
    public void testUpdateDepartmentByInvalidId_ShouldThrowIllegalArgumentException() {
        Department department = Department.builder()
                .name("HR")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ref.updateDepartment(department);
        });

        assertEquals(ErrorMessages.ERROR_INVALID_DEPARTMENT_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Update department: non existent id error")
    public void testUpdateDepartmentByNonExistentId_ShouldThrowNoSuchElementException() {
        Department department = Department.builder()
                .id(99L)
                .name("HR")
                .mandatory(false)
                .readOnly(false)
                .build();

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            ref.updateDepartment(department);
        });

        assertEquals(ErrorMessages.ERROR_DEPARTMENT_NOT_FOUND + department.getId(), exception.getMessage());
    }

    @Test
    @DisplayName("Update department: Department is read only")
    public void testUpdateReadOnlyDepartment_ShouldThrowIllegalArgumentException() {
        Department updatedReadOnlyAndMandatoryDepartmentData = Department.builder()
                .id(READ_ONLY_AND_MANDATORY_DEPARTMENT_ID)
                .name("Updated Organisation")
                .readOnly(true)
                .mandatory(false)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            departmentServiceImpl.updateDepartment(updatedReadOnlyAndMandatoryDepartmentData);
        });

        assertEquals(ErrorMessages.ERROR_DEPARTMENT_IS_READ_ONLY, exception.getMessage());

    }

    @Test
    @DisplayName("Update department: Department updated successfully")
    public void testUpdateDepartment_Success() {
        Department updatedReadOnlyAndMandatoryDepartmentData = Department.builder()
                .id(READ_ONLY_AND_MANDATORY_DEPARTMENT_ID)
                .name("Updated Organisation")
                .readOnly(false)
                .mandatory(false)
                .build();

        when(departmentRepository.save(updatedReadOnlyAndMandatoryDepartmentData)).thenReturn(updatedReadOnlyAndMandatoryDepartmentData);

        Department department = departmentServiceImpl.updateDepartment(updatedReadOnlyAndMandatoryDepartmentData);

        assertThat(department).isNotNull();
        assertEquals(department.getId(), updatedReadOnlyAndMandatoryDepartmentData.getId());
        assertEquals(department.getName(), updatedReadOnlyAndMandatoryDepartmentData.getName());
        assertEquals(department.getMandatory(), updatedReadOnlyAndMandatoryDepartmentData.getMandatory());
        assertEquals(department.getReadOnly(), updatedReadOnlyAndMandatoryDepartmentData.getReadOnly());
    }
}
