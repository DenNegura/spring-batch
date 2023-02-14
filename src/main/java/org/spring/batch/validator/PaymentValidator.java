package org.spring.batch.validator;

import org.spring.batch.item.Payment;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

public class PaymentValidator implements Validator<Payment> {

    @Override
    public void validate(Payment payment) throws ValidationException {
        if(payment.getAmount() <= 0) {
            throw new ValidationException("Amount cannot be zero or less than zero.");
        }
    }
}
