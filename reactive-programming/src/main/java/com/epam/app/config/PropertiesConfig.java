package com.epam.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertiesConfig {
    @Value("${api.sports.url}")
    private String apiSportsUrl;

    @Value("${app.data.load-on-startup}")
    private boolean isLoadDataOnStartup;
}
