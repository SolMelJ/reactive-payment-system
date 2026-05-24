package com.example.paymentprovider;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Аннотация @Configuration говорит Спрингу: "Внутри этого класса лежат настройки и инструкции
 * по созданию системных объектов (Бинов), которые нужно запустить при старте приложения".
 */
@Configuration
public class RabbitConfig {

    // Имя нашей будущей очереди. Сделаем его константой, чтобы не ошибиться в буквах.
    public static final String QUEUE_NAME = "payment_queue";

    /**
     * Аннотация @Bean заставляет Спринг выполнить этот метод, взять созданный объект Queue
     * и зарегистрировать его в системе. Спринг сам пойдет в RabbitMQ и создаст там эту очередь!
     */
    @Bean
    public Queue paymentQueue() {
        // true означает "durable" — очередь будет бессмертной (не пропадет при перезапуске RabbitMQ)
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Этот Бин очень важен! По умолчанию RabbitMQ умеет пересылать только сырые байты или текст.
     * Этот конвертер заставит Спринг автоматически превращать наш сложный Java-объект платежа
     * в компактную JSON-строку перед отправкой в очередь.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}