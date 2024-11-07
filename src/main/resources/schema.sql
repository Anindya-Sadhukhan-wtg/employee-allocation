CREATE TABLE Employee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    mandatory BOOLEAN DEFAULT FALSE NOT NULL,
    read_only BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE Employee_Department (
    employee_id BIGINT,
    department_id BIGINT,
    PRIMARY KEY (employee_id, department_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(id),
    FOREIGN KEY (department_id) REFERENCES Department(id)
);