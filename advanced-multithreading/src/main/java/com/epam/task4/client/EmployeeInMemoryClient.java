package com.epam.task4.client;

import com.epam.task4.model.Employee;

import java.util.List;

public class EmployeeInMemoryClient implements EmployeeClient {
    private static final List<Employee> HIRED_EMPLOYEES;

    static {
        HIRED_EMPLOYEES = List.of(
                new Employee(1L, "Ron"),
                new Employee(2L, "Kevin"),
                new Employee(3L, "David"),
                new Employee(4L, "Jane"),
                new Employee(5L, "Diana"),
                new Employee(6L, "Ron"),
                new Employee(7L, "Kevin"),
                new Employee(8L, "David"),
                new Employee(9L, "Jane"),
                new Employee(10L, "Diana")
        );
    }

    @Override
    public List<Employee> fetchHiredEmployees() {
        return HIRED_EMPLOYEES;
    }
}
