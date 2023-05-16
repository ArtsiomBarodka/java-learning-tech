package com.epam.app.rest;

import com.epam.app.exception.OperationException;
import com.epam.app.exception.ResourceNotFoundException;
import com.epam.app.model.entity.Sport;
import com.epam.app.model.request.CreateSportRequest;
import com.epam.app.model.response.ExceptionResponse;
import com.epam.app.model.response.SportResponse;
import com.epam.app.service.SportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(SportController.class)
public class SportControllerTest {
    @MockBean
    private SportService sportService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void findByQueryTest_withQueryParam() {
        var sport = new Sport(1L, "Sport");

        when(sportService.findByQuery(anyString())).thenReturn(Flux.just(sport));

        webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/v1/sports")
                        .queryParam("query", "Cycling")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SportResponse.class)
                .value(List::size, equalTo(1))
                .value(response -> response.get(0).id(), equalTo(sport.getId()))
                .value(response -> response.get(0).name(), equalTo(sport.getName()));
    }

    @Test
    void findByQueryTest_withoutQueryParam() {
        var sport = new Sport(1L, "Sport");

        when(sportService.findAll()).thenReturn(Flux.just(sport));

        webClient.get().uri("/api/v1/sports")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SportResponse.class)
                .value(List::size, equalTo(1))
                .value(response -> response.get(0).id(), equalTo(sport.getId()))
                .value(response -> response.get(0).name(), equalTo(sport.getName()));
    }

    @Test
    void findByQueryTest_resourceNotFound() {
        when(sportService.findAll()).thenThrow(ResourceNotFoundException.class);

        webClient.get().uri("/api/v1/sports")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ExceptionResponse.class);
    }

    @Test
    void getByIdTest() {
        var id = 1L;
        var sport = new Sport(id, "Sport");

        when(sportService.findById(anyLong())).thenReturn(Mono.just(sport));

        webClient.get().uri("/api/v1/sports/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SportResponse.class)
                .value(SportResponse::id, equalTo(sport.getId()))
                .value(SportResponse::name, equalTo(sport.getName()));
    }

    @Test
    void getByIdTest_resourceNotFound() {
        when(sportService.findById(anyLong())).thenThrow(ResourceNotFoundException.class);

        webClient.get().uri("/api/v1/sports/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ExceptionResponse.class);
    }

    @Test
    void createTest() {
        var sportName = "Sport";
        var sport = new Sport(1L, sportName);

        when(sportService.save(any())).thenReturn(Mono.just(sport));

        webClient.post().uri("/api/v1/sports")
                .bodyValue(new CreateSportRequest(sportName))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SportResponse.class)
                .value(SportResponse::id, equalTo(sport.getId()))
                .value(SportResponse::name, equalTo(sport.getName()));
    }

    @Test
    void createTest_operationException() {
        when(sportService.save(any())).thenThrow(OperationException.class);

        webClient.post().uri("/api/v1/sports")
                .bodyValue(new CreateSportRequest("Sport"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ExceptionResponse.class);
    }
}
