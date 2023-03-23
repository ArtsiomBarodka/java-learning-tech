package com.epam.task4;

import com.epam.task4.facade.EmployeeFacadeImpl;

public class Main {
    public static void main(String[] args) {
        var employeeFacade = new EmployeeFacadeImpl();

        employeeFacade.printHiredEmployeesAsync();
        employeeFacade.printHiredEmployees();
    }
}
