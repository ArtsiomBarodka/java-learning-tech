package com.epam.app.config.acvtivemq;

import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

@Profile("activemq")
@Configuration
@RequiredArgsConstructor
public class ActiveMQProducerConfig {

    @Bean
    public JmsTemplate eventJmsTemplate(ConnectionFactory activeMQConnectionFactory,
                                        MessageConverter jsonMessageConverter) {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(activeMQConnectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        template.setPubSubDomain(false); //use queues (can also use topics if "true")
        return template;
    }

    @Bean
    public JmsTemplate deleteEventJmsTemplate(ConnectionFactory activeMQConnectionFactory) {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(activeMQConnectionFactory);
        template.setPubSubDomain(false); //use queues (can also use topics if "true")
        return template;
    }
}
