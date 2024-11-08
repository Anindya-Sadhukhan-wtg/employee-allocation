package org.example.employeeallocation.model;

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
@Table(name = "EMPLOYEE")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EMPLOYEE")
    @SequenceGenerator(name = "SEQ_EMPLOYEE", sequenceName = "SEQ_EMPLOYEE", allocationSize = 1)
    private Long id;

    @Column(name = "name_first", nullable = false)
    @Size(min = 1, max = 50)
    private String firstName;

    @Column(name = "name_last", nullable = false)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "MAP_EMPLOYEE_DEPARTMENT",
            joinColumns = @JoinColumn(name = "id_employee", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_department", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<Department> departments = new HashSet<>();
}
