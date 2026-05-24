package com.example.paymentprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final RabbitTemplate rabbitTemplate;

    // Создаем мапперы для ручной конвертации перед отправкой
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    private final XmlMapper xmlMapper = (XmlMapper) new XmlMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    public PaymentController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(
            value = "/set",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    // Добавляем @RequestHeader("Content-Type"), чтобы знать, какой формат прислал клиент
    public ResponseEntity<String> setPayment(@RequestBody PaymentDTO payment, @RequestHeader("Content-Type") String contentType) {

        System.out.println("=== ПОЛУЧЕН НОВЫЙ ПЛАТЕЖ ===");
        System.out.println("Имя клиента: " + payment.getCustomerName());
        System.out.println("Формат входящего запроса: " + contentType);

        String transactionId = UUID.randomUUID().toString();
        Object messageToSend = payment; // По умолчанию отправляем объект как есть

        try {
            // Если пришел XML, принудительно перегоняем объект в XML-строку перед отправкой в Rabbit
            if (contentType.contains(MediaType.APPLICATION_XML_VALUE)) {
                messageToSend = xmlMapper.writeValueAsString(payment);
                System.out.println("🔄 Объект сериализован в XML-строку для RabbitMQ");
            }
            // Если пришел JSON, перегоняем в JSON-строку
            else if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                messageToSend = objectMapper.writeValueAsString(payment);
                System.out.println("🔄 Объект сериализован в JSON-строку для RabbitMQ");
            }
        } catch (Exception e) {
            System.err.println("Ошибка сериализации: " + e.getMessage());
        }

        // Отправляем уже готовую строку (JSON или XML) в RabbitMQ
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, messageToSend);

        System.out.println("🚀 Платеж успешно отправлен в RabbitMQ!");
        System.out.println("=============================");

        return ResponseEntity.ok("Payment accepted and queued! Transaction ID: " + transactionId);
    }
}