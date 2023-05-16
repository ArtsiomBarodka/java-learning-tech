package com.epam.app.bootstrap;

import com.epam.app.client.SportsClient;
import com.epam.app.config.PropertiesConfig;
import com.epam.app.service.SportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContextEventListener {
    private final PropertiesConfig propertiesConfig;
    private final SportsClient sportsClient;
    private final SportService sportService;

    @EventListener(ApplicationReadyEvent.class)
    public void afterApplicationStartup() {
        if (propertiesConfig.isLoadDataOnStartup()) {
            log.info("Start loading data on Startup");
            var loadedData = sportsClient.retrieveSports();

            sportService
                    .saveAll(loadedData)
                    .doOnComplete(() -> log.info("Finish loading data on Startup"))
                    .subscribe(savedData -> log.info("Saved new Spot item: {}", savedData));
        }
    }
}
