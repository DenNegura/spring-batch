package org.spring.batch.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private Long id;

    private double amount;

    private LocalDate date;

    private String ordNumber;

    private Long userId;
}