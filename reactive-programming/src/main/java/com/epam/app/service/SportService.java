package com.epam.app.service;

import com.epam.app.model.entity.Sport;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SportService {
    @NonNull Flux<Sport> saveAll(@NonNull Flux<Sport> sports);
    @NonNull Mono<Sport> save(@NonNull Sport sport);
    @NonNull Flux<Sport> findAll();
    @NonNull Mono<Sport> findById(Long id);
    @NonNull Flux<Sport> findByQuery(@NonNull String query);
}
