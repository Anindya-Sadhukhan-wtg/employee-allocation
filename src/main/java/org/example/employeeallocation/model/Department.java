package org.example.employeeallocation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "DEPARTMENT")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEPARTMENT")
    @SequenceGenerator(name = "SEQ_DEPARTMENT", sequenceName = "SEQ_DEPARTMENT", allocationSize = 1)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @Size(min = 1, max = 50)
    private String name;

    @Column(name = "mandatory")
    @Builder.Default
    private Boolean mandatory = false;

    @Column(name = "read_only")
    @Builder.Default
    private Boolean readOnly = false;

    @ManyToMany(mappedBy = "departments")
    @JsonIgnore
    @Builder.Default
    private Set<Employee> employees = new HashSet<>();
}
