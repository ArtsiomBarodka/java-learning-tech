package com.epam.task4.service;

import java.math.BigDecimal;

public interface SalaryService {
    BigDecimal fetchSalary(Long employeeId);
}
