package com.example.paymentprovider;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize; // Добавили импорт
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer; // Добавили импорт

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentDTO {
    private String paymentNumber;
    private BigDecimal amount;

    /**
     * @JsonDeserialize говорит парсеру: "Для чтения этого поля используй стандартный десериализатор дат"
     * @JsonFormat указывает конкретный макет строки, который прилетит к нам в XML/JSON
     */    // Эти аннотации теперь работают в паре с новым модулем и намертво связывают формат даты
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    private String customerName;
}