package com.epam.app.client;

import com.epam.app.model.entity.Sport;
import reactor.core.publisher.Flux;

public interface SportsClient {
    Flux<Sport> retrieveSports();
}
