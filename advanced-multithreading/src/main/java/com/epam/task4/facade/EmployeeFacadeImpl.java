package com.epam.task4.facade;

import com.epam.task4.client.EmployeeClient;
import com.epam.task4.client.EmployeeInMemoryClient;
import com.epam.task4.model.Employee;
import com.epam.task4.service.SalaryInMemoryService;
import com.epam.task4.service.SalaryService;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class EmployeeFacadeImpl implements EmployeeFacade {
    private final EmployeeClient employeeClient;
    private final SalaryService salaryService;

    public EmployeeFacadeImpl() {
        employeeClient = new EmployeeInMemoryClient();
        salaryService = new SalaryInMemoryService();
    }

    @Override
    public void printHiredEmployeesAsync() {
        long start = System.nanoTime();
        fetchHiredEmployeesAsync()
                .thenComposeAsync(this::fillSalariesAsync)
                .thenAcceptAsync((employees) -> printEmployeesWithSalaries(employees, "The time of async execution: %d \n", start));
    }

    @Override
    public void printHiredEmployees() {
        long start = System.nanoTime();
        var employees = employeeClient.fetchHiredEmployees()
                .stream()
                .map(employee -> {
                    var salary = salaryService.fetchSalary(employee.getId());
                    employee.setSalary(salary);
                    return employee;
                }).toList();

        printEmployeesWithSalaries(employees, "The time of sync execution: %d \n", start);
    }

    private CompletionStage<List<Employee>> fetchHiredEmployeesAsync() {
        return CompletableFuture.supplyAsync(employeeClient::fetchHiredEmployees);
    }

    private CompletionStage<BigDecimal> fetchSalariesAsync(Long employeeId) {
        return CompletableFuture.supplyAsync(() -> salaryService.fetchSalary(employeeId));
    }

    private CompletableFuture<List<Employee>> fillSalariesAsync(List<Employee> employees) {
        List<CompletionStage<Employee>> employeeWithSalaries = employees.stream()
                .map(employee -> fetchSalariesAsync(employee.getId())
                        .thenApplyAsync(salary -> {
                            employee.setSalary(salary);
                            return employee;
                        })).toList();


        return CompletableFuture.allOf(employeeWithSalaries.toArray(new CompletableFuture[employeeWithSalaries.size()]))
                .thenApplyAsync(
                        v -> employeeWithSalaries.stream()
                                .map(CompletionStage::toCompletableFuture)
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList()));
    }

    private void printEmployeesWithSalaries(List<Employee> employees, String text ,long start) {
        employees.forEach(System.out::println);
        System.out.printf(text, System.nanoTime() - start);
    }
}
