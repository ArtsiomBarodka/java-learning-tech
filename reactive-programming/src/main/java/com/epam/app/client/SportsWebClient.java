package com.epam.app.client;

import com.epam.app.config.PropertiesConfig;
import com.epam.app.exception.ClientException;
import com.epam.app.exception.ResourceNotFoundException;
import com.epam.app.model.entity.Sport;
import com.epam.app.model.response.SportClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Slf4j
@Component
public class SportsWebClient implements SportsClient {
    private final WebClient webClient;

    public SportsWebClient(PropertiesConfig propertiesConfig) {
        this.webClient = WebClient.builder()
                .baseUrl(propertiesConfig.getApiSportsUrl())
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                        .build())
                .build();
    }

    @Override
    public Flux<Sport> retrieveSports() {
        return webClient.get().retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new ClientException("Some problems on SportsClient side.")))
                .bodyToMono(SportClientResponse.class)
                .doOnNext(sportClientResponse -> log.info("Sport Response is retrieved. Response: {}", sportClientResponse))
                .flatMapMany(this::getDataItemFlux)
                .filter(withoutNullFields())
                .flatMap(this::getSportMono)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sports are not found from client")))
                .distinct(Sport::getName)
                .doOnError(e -> log.error("Error retrieving sports from client. Error: {}", e.getMessage()));
    }

    private Predicate<SportClientResponse.DataItem> withoutNullFields() {
        return item -> item.getId() != null && item.getAttributes() != null && item.getAttributes().getName() != null;
    }

    private Mono<Sport> getSportMono(SportClientResponse.DataItem dataItem) {
        var sport = new Sport();
        sport.setName(dataItem.getAttributes().getName());
        return Mono.just(sport);
    }

    private Flux<SportClientResponse.DataItem> getDataItemFlux(SportClientResponse response) {
        return Flux.fromIterable(response.getData());
    }
}
