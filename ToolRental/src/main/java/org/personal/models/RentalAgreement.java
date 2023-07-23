package org.personal.models;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class RentalAgreement {
    private CheckOut checkOut;
    private LocalDate dueDate;
    private int chargeDays;
    private int preDiscountCharge;
    private double discountAmount;
    private double finalCharge;


}
