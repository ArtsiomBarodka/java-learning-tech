package com.epam.app.service;

import com.epam.app.model.dto.Sport;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SportService {
    @NonNull Flux<Sport> saveAll(@NonNull Flux<Sport> sports);
    @NonNull Mono<Sport> save(@NonNull Sport sport);
    @NonNull Mono<Sport> findById(Integer id);
}
