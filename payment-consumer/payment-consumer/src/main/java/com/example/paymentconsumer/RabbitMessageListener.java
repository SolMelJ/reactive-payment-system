package com.example.paymentconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMessageListener {

    @Autowired
    private PaymentRepository paymentRepository;

    // Инструмент для JSON
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    // Инструмент для XML
    private final XmlMapper xmlMapper = (XmlMapper) new XmlMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @RabbitListener(queues = "payment_queue")
    public void receivePayment(String messageRaw) {
        String trimmedMessage = messageRaw.trim();
        PaymentEntity payment = null;

        try {
            // 1. Проверяем на JSON
            if (trimmedMessage.startsWith("{") || trimmedMessage.contains("{\\\"") || trimmedMessage.startsWith("\"{")) {
                System.out.println("📩 Сервис Б поймал JSON-сообщение!");

                if (trimmedMessage.startsWith("\"") && trimmedMessage.endsWith("\"")) {
                    trimmedMessage = trimmedMessage.substring(1, trimmedMessage.length() - 1).replace("\\\"", "\"");
                }
                payment = objectMapper.readValue(trimmedMessage, PaymentEntity.class);
            }
            // 2. Проверяем на XML
            else if (trimmedMessage.contains("<")) {
                System.out.println("📨 Сервис Б поймал XML-сообщение!");

                if (trimmedMessage.startsWith("\"") && trimmedMessage.endsWith("\"")) {
                    trimmedMessage = trimmedMessage.substring(1, trimmedMessage.length() - 1);
                }
                payment = xmlMapper.readValue(trimmedMessage, PaymentEntity.class);
            }
            // 3. Неизвестный формат
            else {
                System.err.println("❌ Неизвестный формат сообщения: " + trimmedMessage);
                return;
            }

            // 4. Сохранение в базу данных
            System.out.println("Обработка платежа для клиента: " + payment.getCustomerName());

            PaymentEntity savedPayment = paymentRepository.save(payment);
            System.out.println("💾 Успешно сохранено в PostgreSQL! Присвоен ID: " + savedPayment.getId());
            System.out.println("----------------------------------------------");

        } catch (Exception e) {
            System.err.println("Ошибка при десериализации данных: " + e.getMessage());
            e.printStackTrace();
        }
    } // Конец метода receivePayment

} // Конец класса RabbitMessageListener