package org.spring.batch.mapper;

import org.spring.batch.item.Payment;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PaymentFieldSetMapper implements FieldSetMapper<Payment> {

    @Override
    public Payment mapFieldSet(FieldSet fieldSet) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyy");

        Payment payment = new Payment();

        payment.setId(fieldSet.readLong("id"));
        payment.setAmount(fieldSet.readDouble("amount"));
        payment.setDate(LocalDate.parse(fieldSet.readString("date"), formatter));
        payment.setOrdNumber(fieldSet.readString("ord_number"));
        payment.setUserId(fieldSet.readLong("user_id"));

        return payment;
    }
}
