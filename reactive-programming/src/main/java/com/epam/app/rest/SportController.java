package com.epam.app.rest;

import com.epam.app.model.entity.Sport;
import com.epam.app.model.request.CreateSportRequest;
import com.epam.app.model.response.SportResponse;
import com.epam.app.service.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sports")
@RequiredArgsConstructor
public class SportController {
    private final SportService sportService;

    @GetMapping
    public Flux<SportResponse> findByQuery(@RequestParam(name = "query", required = false) Optional<String> query) {
        return query.map(sportService::findByQuery)
                .orElseGet(sportService::findAll)
                .flatMap(this::toSportResponse);
    }

    @GetMapping("/{id}")
    public Mono<SportResponse> getById(@PathVariable Long id) {
        return sportService.findById(id)
                .flatMap(this::toSportResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<SportResponse> create(@RequestBody CreateSportRequest createSportRequest) {
        var sport = new Sport();
        sport.setName(createSportRequest.getName());

        return sportService.save(sport)
                .flatMap(this::toSportResponse);
    }

    private Mono<SportResponse> toSportResponse(Sport sport) {
        var sportResponse = new SportResponse(sport.getId(), sport.getName());
        return Mono.just(sportResponse);
    }
}
