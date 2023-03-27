package com.epam.task4.client;

import com.epam.task4.model.Employee;

import java.util.List;

public interface EmployeeClient {
    List<Employee> fetchHiredEmployees();
}
