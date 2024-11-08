-- noinspection SqlNoDataSourceInspectionForFile

INSERT INTO DEPARTMENT ( id, name, read_only, mandatory) VALUES (NEXTVAL('SEQ_EMPLOYEE'), 'Organisation', true, true);

INSERT INTO EMPLOYEE ( id, name_first, name_last) VALUES (NEXTVAL('SEQ_DEPARTMENT'), 'John', 'Doe');

INSERT INTO MAP_EMPLOYEE_DEPARTMENT (id_employee, id_department) VALUES (CURRVAL('SEQ_EMPLOYEE'),CURRVAL('SEQ_DEPARTMENT'));
