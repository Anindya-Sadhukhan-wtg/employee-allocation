package org.example.employeeallocation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateEmployeeRequestDTO {
    private Long id;
    private String name;
    private Set<String> departmentIds;
}
