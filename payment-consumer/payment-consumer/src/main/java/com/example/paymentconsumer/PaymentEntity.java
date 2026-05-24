package com.example.paymentconsumer;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @Entity говорит Спрингу, что этот класс привязан к таблице БД.
 * @Table явно указывает имя таблицы в PostgreSQL.
 */
@Entity
@Table(name = "payments")
@Data
public class PaymentEntity {

    /**
     * @Id задает Primary Key.
     * @GeneratedValue(strategy = GenerationType.IDENTITY) — это аналог IDENTITY колонок
     * или SEQUENCE, то есть автоинкремент (1, 2, 3...) силами самой СУБД.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_number")
    private String paymentNumber;

    private BigDecimal amount;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "customer_name")
    private String customerName;
}