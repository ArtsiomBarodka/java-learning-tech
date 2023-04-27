package com.epam.app.service;

import com.epam.app.model.dto.Sport;
import com.epam.app.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SportServiceImpl implements SportService {
    private final SportRepository sportRepository;

    @Override
    public @NonNull Flux<Sport> saveAll(@NonNull Flux<Sport> sports) {
        return sportRepository.saveAll(sports);
    }

    @Override
    public @NonNull Mono<Sport> save(@NonNull Sport sport) {
        return sportRepository.save(sport);
    }

    @Override
    public @NonNull Mono<Sport> findById(@NonNull Integer id) {
        return sportRepository.findById(id);
    }
}
