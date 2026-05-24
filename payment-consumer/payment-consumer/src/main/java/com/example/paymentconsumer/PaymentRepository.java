package com.example.paymentconsumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Нам достаточно унаследоваться от JpaRepository, указав тип Сущности (PaymentEntity)
 * и тип её первичного ключа (Long). Писать методы внутри НЕ нужно!
 * Спринг сам предоставит методы .save(), .findById(), .delete() и т.д.
 */
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}