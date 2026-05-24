/*
package com.example.paymentconsumer;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Мы возвращаем интерфейс MessageConverter, а внутри создаем
        // актуальную реализацию, которая есть в твоей версии Spring.
        return new org.springframework.amqp.support.converter.Jackson2JsonMessageConverter();
    }
}
*/