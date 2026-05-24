package com.example.paymentconsumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/consumer/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;

    // Внедряем репозиторий через конструктор, чтобы ходить в базу данных
    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * GET-запрос для проверки существования платежа по ID
     * Пример вызова: GET http://localhost:8081/api/consumer/payments/check/1
     */
    @GetMapping("/check/{id}")
    public ResponseEntity<String> checkPaymentStatus(@PathVariable Long id) {
        System.out.println("🔍 Запрос статуса: проверяем платеж с ID: " + id);

        // Ищем запись в базе данных по ID
        Optional<PaymentEntity> paymentOpt = paymentRepository.findById(id);

        if (paymentOpt.isPresent()) {
            PaymentEntity payment = paymentOpt.get();
            String successMessage = String.format(
                    "✅ Успех! Платеж найден. ID: %d, Клиент: %s, Сумма: %.2f, Номер: %s",
                    payment.getId(), payment.getCustomerName(), payment.getAmount(), payment.getPaymentNumber()
            );
            System.out.println(successMessage);
            return ResponseEntity.ok(successMessage);
        } else {
            String errorMessage = String.format("❌ Произошла ошибка: платеж с ID %d не создан или не найден в системе.", id);
            System.err.println(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }
}