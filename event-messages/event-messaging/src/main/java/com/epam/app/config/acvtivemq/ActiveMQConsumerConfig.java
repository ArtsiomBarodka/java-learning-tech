package com.epam.app.config.acvtivemq;

import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ErrorHandler;

import java.util.concurrent.TimeUnit;

@Slf4j
@Profile("activemq")
@EnableJms
@Configuration
@RequiredArgsConstructor
public class ActiveMQConsumerConfig {
    private static final String CONCURRENCY_PATTERN = "%d-%d";

    private final ActiveMQPropertiesConfig activeMQPropertiesConfig;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerRequestEventContainerFactory(ConnectionFactory activeMQConnectionFactory,
                                                                                      MessageConverter jsonMessageConverter) {
        var containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setConnectionFactory(activeMQConnectionFactory);
        containerFactory.setMessageConverter(jsonMessageConverter);
        containerFactory.setPubSubDomain(false); //use queues (can also use topics if "true")
        containerFactory.setConcurrency(CONCURRENCY_PATTERN.formatted(
                activeMQPropertiesConfig.getActiveMQConsumerConcurrencyMin(),
                activeMQPropertiesConfig.getActiveMQConsumerConcurrencyMax()));
        containerFactory.setErrorHandler(LOGGING_ERROR_HANDLER);

        return containerFactory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerDeleteRequestEventContainerFactory(ConnectionFactory activeMQConnectionFactory) {
        var containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setConnectionFactory(activeMQConnectionFactory);
        containerFactory.setPubSubDomain(false); //use queues (can also use topics if "true")
        containerFactory.setConcurrency(CONCURRENCY_PATTERN.formatted(
                activeMQPropertiesConfig.getActiveMQConsumerConcurrencyMin(),
                activeMQPropertiesConfig.getActiveMQConsumerConcurrencyMax()));
        containerFactory.setErrorHandler(LOGGING_ERROR_HANDLER);

        return containerFactory;
    }

    private static final ErrorHandler LOGGING_ERROR_HANDLER =
            (e) -> log.warn("Error during consuming process. Error: {}", e.getMessage());

    @Bean
    public RetryTemplate jmsRetryTemplate() {
        return RetryTemplate.builder()
                .fixedBackoff(TimeUnit.SECONDS.toMillis(activeMQPropertiesConfig.getActiveMQConsumerRetryDelay()))
                .maxAttempts(activeMQPropertiesConfig.getActiveMQConsumerRetryAttempts())
                .build();
    }
}
