package com.epam.app.client;

import com.epam.app.exception.ClientException;
import com.epam.app.model.dto.Sport;
import com.epam.app.model.response.SportClientResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Component
public class SportsWebClient implements SportsClient{
    private final WebClient webClient;

    public SportsWebClient(@Value("${api.sports.url}") String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
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
                .flatMapMany(this::getDataItemFlux)
                .flatMap(this::getSportMono)
                .filter(withoutNullFields());
    }

    private Predicate<Sport> withoutNullFields() {
        return sport -> sport.getId() != null && sport.getName() != null;
    }

    private Mono<Sport> getSportMono(SportClientResponse.DataItem dataItem) {
        return Mono.just(new Sport(dataItem.getId(), dataItem.getAttributes().getName()));
    }

    private Flux<SportClientResponse.DataItem> getDataItemFlux(SportClientResponse response) {
        return Flux.fromIterable(response.getData());
    }
}
