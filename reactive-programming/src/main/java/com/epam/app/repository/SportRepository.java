package com.epam.app.repository;

import com.epam.app.model.entity.Sport;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SportRepository extends R2dbcRepository<Sport, Long> {
    @NonNull
    Flux<Sport> findByNameContaining(@NonNull String name);

    @NonNull
    Mono<Boolean> existsByName(@NonNull String name);
}
