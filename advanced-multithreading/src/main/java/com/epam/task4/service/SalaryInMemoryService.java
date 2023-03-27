package com.epam.task4.service;

import java.math.BigDecimal;
import java.util.Map;

public class SalaryInMemoryService implements SalaryService {
    private static final Map<Long, BigDecimal> SALARIES_MAP;

    static {
        SALARIES_MAP = Map.of(
                1L, BigDecimal.valueOf(5000L),
                2L, BigDecimal.valueOf(6000L),
                3L, BigDecimal.valueOf(7000L),
                4L, BigDecimal.valueOf(8000L),
                5L, BigDecimal.valueOf(9000L),
                6L, BigDecimal.valueOf(5000L),
                7L, BigDecimal.valueOf(6000L),
                8L, BigDecimal.valueOf(7000L),
                9L, BigDecimal.valueOf(8000L),
                10L, BigDecimal.valueOf(9000L)
        );
    }

    @Override
    public BigDecimal fetchSalary(Long employeeId) {
        return SALARIES_MAP.get(employeeId);
    }
}
