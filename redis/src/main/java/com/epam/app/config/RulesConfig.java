package com.epam.app.config;

import com.epam.app.model.dto.RateLimitRule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Getter
@Setter
@Component
@PropertySource(value = "rateLimitRules.properties")
@ConfigurationProperties("rate")
public class RulesConfig {
    private List<RateLimitRule> rules;
}
