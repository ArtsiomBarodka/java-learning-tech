package com.epam.app.repository;

import com.epam.app.model.dto.Sport;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface SportRepository extends R2dbcRepository<Sport, Integer> {
}
