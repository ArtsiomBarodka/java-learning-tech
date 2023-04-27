package com.epam.app.client;

import com.epam.app.model.dto.Sport;
import reactor.core.publisher.Flux;

public interface SportsClient {
    Flux<Sport> retrieveSports();
}
