package com.epam.app.config.acvtivemq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Profile("activemq")
@Configuration
@RequiredArgsConstructor
public class ActiveMQBasicConfig {
    private final ActiveMQPropertiesConfig activeMQPropertiesConfig;

    @Bean
    public ConnectionFactory activeMQConnectionFactory() {
        var connectionFactory = new ActiveMQJMSConnectionFactory(activeMQPropertiesConfig.getActiveMQUrl());
        connectionFactory.setPassword(activeMQPropertiesConfig.getActiveMQPassword());
        connectionFactory.setUser(activeMQPropertiesConfig.getActiveMQUsername());
        return new CachingConnectionFactory(connectionFactory);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        var mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setTargetType(MessageType.TEXT);
        mappingJackson2MessageConverter.setTypeIdPropertyName("_type");
        mappingJackson2MessageConverter.setObjectMapper(objectMapper());
        return mappingJackson2MessageConverter;
    }

    private ObjectMapper objectMapper() {
        var mapper = Jackson2ObjectMapperBuilder.json().build();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return mapper;
    }
}
