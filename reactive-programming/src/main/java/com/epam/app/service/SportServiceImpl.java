package com.epam.app.service;

import com.epam.app.exception.OperationException;
import com.epam.app.exception.ResourceNotFoundException;
import com.epam.app.model.entity.Sport;
import com.epam.app.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SportServiceImpl implements SportService {
    private final SportRepository sportRepository;

    @Override
    @Transactional
    public @NonNull Flux<Sport> saveAll(@NonNull Flux<Sport> sports) {
        return sports.flatMap(sport -> sportRepository.existsByName(sport.getName())
                        .doOnNext(exists -> {
                            if (exists) {
                                log.warn("Sport already exists with name {}", sport.getName());
                            }
                        })
                        .flatMap(exists -> exists ?
                                Mono.error(new OperationException("Error saving Sport. Sport already exists with name %s".formatted(sport.getName()))) :
                                Mono.just(sport)))
                .concatMap(sportRepository::save)
                .doOnError(e -> log.error("Error saving sports: {}, Error: {}", sports, e.getMessage()))
                .doOnNext(savedSport -> log.info("New Sport is saved. Sport {}", savedSport));
    }

    @Override
    @Transactional
    public @NonNull Mono<Sport> save(@NonNull Sport sport) {
        return sportRepository.existsByName(sport.getName())
                .doOnNext(exists -> {
                    if (exists) {
                        log.warn("Sport already exists with name {}", sport.getName());
                    }
                })
                .flatMap(exists -> exists ?
                        Mono.error(new OperationException("Error saving Sport. Sport already exists with name %s".formatted(sport.getName()))) :
                        sportRepository.save(sport))
                .doOnError(e -> log.error("Error saving sport: {}, Error: {}", sport, e.getMessage()))
                .doOnSuccess(savedSport -> log.info("New Sport is saved. Sport {}", savedSport));

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public @NonNull Flux<Sport> findAll() {
        return sportRepository.findAll()
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sports not found")))
                .doOnError(e -> log.error("Error finding sport. Error: {}", e.getMessage()))
                .doOnNext(sport -> log.info("Sports are found. Sport item: {}", sport));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public @NonNull Mono<Sport> findById(@NonNull Long id) {
        return sportRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sport not found with id: %d".formatted(id))))
                .doOnError(e -> log.error("Error finding sport with id: {}. Error: {}", id, e.getMessage()))
                .doOnNext(sport -> log.info("Sport is found by id: {}, Sport: {}", id, sport));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public @NonNull Flux<Sport> findByQuery(@NonNull String query) {
        return sportRepository.findByNameContaining(query)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sport not found with query: %s".formatted(query))))
                .doOnError(e -> log.error("Error finding sport with query: {}. Error: {}", query, e.getMessage()))
                .doOnNext(sport -> log.info("Sport found with query: {}. Sport {}", query, sport));
    }
}
