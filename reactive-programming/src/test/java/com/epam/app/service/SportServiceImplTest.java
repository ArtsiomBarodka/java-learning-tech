package com.epam.app.service;

import com.epam.app.exception.OperationException;
import com.epam.app.exception.ResourceNotFoundException;
import com.epam.app.model.entity.Sport;
import com.epam.app.repository.SportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SportServiceImplTest {
    @InjectMocks
    private SportServiceImpl sportService;

    @Mock
    private SportRepository sportRepository;

    @Test
    void saveAllTest() {
        var sportName = "Sport";
        var expected = new Sport(1L, sportName);
        var newSport = new Sport(null, sportName);

        when(sportRepository.existsByName(anyString())).thenReturn(Mono.just(false));
        when(sportRepository.save(any())).thenReturn(Mono.just(expected));

        var result = sportService.saveAll(Flux.just(newSport));

        StepVerifier.create(result)
                .consumeNextWith(actual -> {
                    assertEquals(expected.getId(), actual.getId());
                    assertEquals(expected.getName(), actual.getName());

                    verify(sportRepository, times(1)).existsByName(eq(sportName));
                    verify(sportRepository, times(1)).save(eq(newSport));
                })
                .verifyComplete();
    }

    @Test
    void saveAllTest_sportNameAlreadyExists() {
        when(sportRepository.existsByName(anyString())).thenReturn(Mono.just(true));

        var result = sportService.saveAll(Flux.just(new Sport(null, "Sport")));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof OperationException)
                .verify();
    }

    @Test
    void saveTest() {
        var sportName = "Sport";
        var expected = new Sport(1L, sportName);
        var newSport = new Sport(null, sportName);

        when(sportRepository.existsByName(anyString())).thenReturn(Mono.just(false));
        when(sportRepository.save(any())).thenReturn(Mono.just(expected));

        var result = sportService.save(newSport);

        StepVerifier.create(result)
                .consumeNextWith(actual -> {
                    assertEquals(expected.getId(), actual.getId());
                    assertEquals(expected.getName(), actual.getName());

                    verify(sportRepository, times(1)).existsByName(eq(sportName));
                    verify(sportRepository, times(1)).save(eq(newSport));
                })
                .verifyComplete();
    }

    @Test
    void saveTest_sportNameAlreadyExists() {
        when(sportRepository.existsByName(anyString())).thenReturn(Mono.just(true));

        var result = sportService.save(new Sport(null, "Sport"));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof OperationException)
                .verify();
    }

    @Test
    void findByAllTest() {
        var expected = new Sport(1L, "Sport");

        when(sportRepository.findAll()).thenReturn(Flux.just(expected));

        var result = sportService.findAll();

        StepVerifier.create(result)
                .consumeNextWith(actual -> {
                    assertEquals(expected.getId(), actual.getId());
                    assertEquals(expected.getName(), actual.getName());

                    verify(sportRepository, times(1)).findAll();
                })
                .verifyComplete();
    }

    @Test
    void findByAllTest_sportsNotFound() {
        when(sportRepository.findAll()).thenReturn(Flux.empty());

        var result = sportService.findAll();

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException)
                .verify();
    }

    @Test
    void findByIdTest() {
        var id = 1L;
        var expected = new Sport(id, "Sport");

        when(sportRepository.findById(anyLong())).thenReturn(Mono.just(expected));

        var result = sportService.findById(id);

        StepVerifier.create(result)
                .consumeNextWith(actual -> {
                    assertEquals(expected.getId(), actual.getId());
                    assertEquals(expected.getName(), actual.getName());

                    verify(sportRepository, times(1)).findById(eq(id));
                })
                .verifyComplete();
    }

    @Test
    void findByIdTest_sportNotFound() {
        when(sportRepository.findById(anyLong())).thenReturn(Mono.empty());

        var result = sportService.findById(1L);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException)
                .verify();
    }

    @Test
    void findByQueryTest() {
        var query = "query";
        var expected = new Sport(1L, "Sport");

        when(sportRepository.findByNameContaining(anyString())).thenReturn(Flux.just(expected));

        var result = sportService.findByQuery(query);

        StepVerifier.create(result)
                .consumeNextWith(actual -> {
                    assertEquals(expected.getId(), actual.getId());
                    assertEquals(expected.getName(), actual.getName());

                    verify(sportRepository, times(1)).findByNameContaining(eq(query));
                })
                .verifyComplete();
    }

    @Test
    void findByQueryTest_sportsNotFound() {
        when(sportRepository.findByNameContaining(anyString())).thenReturn(Flux.empty());

        var result = sportService.findByQuery("query");

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException)
                .verify();
    }
}
