package com.epam.app.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import java.util.concurrent.TimeUnit;

@EnableRabbit
@Profile("rabbitmq")
@Configuration
@RequiredArgsConstructor
public class RabbitMQConsumerConfig {
    private final RabbitMQPropertiesConfig rabbitMQPropertiesConfig;

    @Bean
    public MessageRecoverer recoverer() {
        return new RejectAndDontRequeueRecoverer();
    }

    @Bean
    public RetryOperationsInterceptor getRetryOperationsInterceptor() {
        var fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(TimeUnit.SECONDS.toMillis(rabbitMQPropertiesConfig.getRabbitMQConsumerRetryDelay()));
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(rabbitMQPropertiesConfig.getRabbitMQConsumerRetryAttempts())
                .backOffPolicy(fixedBackOffPolicy)
                .recoverer(recoverer())
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerRequestEventContainerFactory(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setConcurrentConsumers(rabbitMQPropertiesConfig.getRabbitMQConsumerConcurrent());
        factory.setMaxConcurrentConsumers(rabbitMQPropertiesConfig.getRabbitMQConsumerMaxConcurrent());
        factory.setAdviceChain(getRetryOperationsInterceptor());
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerDeleteRequestEventContainerFactory(ConnectionFactory connectionFactory, MessageConverter longMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(longMessageConverter);
        factory.setConcurrentConsumers(rabbitMQPropertiesConfig.getRabbitMQConsumerConcurrent());
        factory.setMaxConcurrentConsumers(rabbitMQPropertiesConfig.getRabbitMQConsumerMaxConcurrent());
        factory.setAdviceChain(getRetryOperationsInterceptor());
        return factory;
    }
}
