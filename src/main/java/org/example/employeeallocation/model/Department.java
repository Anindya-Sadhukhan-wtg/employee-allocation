package org.example.employeeallocation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Builder.Default
    private Boolean mandatory = false;

    @Builder.Default
    private Boolean readOnly = false;

    @ManyToMany(mappedBy = "departments")
    @JsonIgnore
    @Builder.Default
    private Set<Employee> employees = new HashSet<>();
}
